package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;


// Class used to turn the robot a specified number of degrees using a PID algorithm
// Heavily modified from https://stemrobotics.cs.pdx.edu/node/7265
public class GyroTurn
{
    BNO055IMU               imu; // Internal Measurement Unit
    Orientation             lastAngles = new Orientation(); // Update orientation in case REV hub turned
    double                  globalAngle = 0; // Current robot angle
    double                  maxTime = 2; // Upper time limit so we do not get stuck
    public double           power; // Power to give to motors

    GyroTurn() {}

    // "Turn on" gyro
    public void initGyro(HardwareMap hardwareMap) throws InterruptedException
    {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);
    }

    // Method to test of IMU is working
    public void test(Telemetry telemetry){
        while (true) {
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("Angles", angles);
            telemetry.update();
        }
    }

    // Update globalAngle
    public void getAngle()
    {
        // We experimentally determined the Z axis is the axis we want to use for heading angle.
        // We have to process the angle because the imu works in euler angles so the Z axis is
        // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
        // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;
    }

    // Main function
    public void turnDegrees(double degreesToTurn, DcMotor leftDrive, DcMotor rightDrive, Telemetry telemetry){
        ElapsedTime runtime = new ElapsedTime(); // Used for time
        PIDController           pidRotate; // Create PID instances
        int tolerance = 1;

        globalAngle = 0;
        power = 0.3;

        //Pid setup
        pidRotate = new PIDController(0.02, 0.0, 0.03); // Trial and error turned numbers
        pidRotate.reset();
        pidRotate.setSetpoint(degreesToTurn);
        pidRotate.setInputRange(-180, 180);
        pidRotate.setOutputRange(0, power);
        pidRotate.setTolerance(tolerance);
        pidRotate.enable();

        runtime.reset();
        while(!(globalAngle <= degreesToTurn + tolerance && globalAngle >= degreesToTurn - tolerance) && runtime.seconds() < maxTime){
            getAngle();
            telemetry.addData("angle I want", degreesToTurn);
            telemetry.addData("angle I have", globalAngle);
            telemetry.update();
            power = pidRotate.performPID(globalAngle); // power will be - on right turn.

            leftDrive.setPower(-power);
            rightDrive.setPower(power);
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }
}