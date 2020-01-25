package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;


public class GyroTurn
{
    BNO055IMU               imu;
    Orientation             lastAngles = new Orientation();
    double                  globalAngle = 0;
    double                  maxTime = 3;

    GyroTurn() {}

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

    public void test(Telemetry telemetry){
        while (true) {
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            telemetry.addData("Angles", angles);
            telemetry.update();
        }
    }

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

    public void turnDegrees(double degreesToTurn, DcMotor leftDrive, DcMotor rightDrive, Telemetry telemetry){
        ElapsedTime runtime = new ElapsedTime();
        double power = 0.4;
        PIDController           pidRotate;
        globalAngle = 0;

        //pidRotate = new PIDController(0.01, 0.0, 0.01);
        pidRotate = new PIDController(0.02, 0.0, 0.03);
        pidRotate.reset();
        pidRotate.setSetpoint(degreesToTurn);
        pidRotate.setInputRange(-180, 180);
        pidRotate.setOutputRange(0, power);
        pidRotate.setTolerance(1);
        pidRotate.enable();

        //while (!pidRotate.onTarget()){
        runtime.reset();
        while(!(globalAngle <= degreesToTurn + 1 && globalAngle >= degreesToTurn - 1) && runtime.seconds() < 2){
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