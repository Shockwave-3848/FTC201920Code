package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.Math;


@Autonomous(name="SKYSTONE Vuforia Nav", group ="Concept")
@Disabled
public class vuforiaNav extends LinearOpMode {
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor lift_motor = null;
    double                  power = 0.25;

    String mode = "Find Target";

    boolean firstBlock;
    boolean secondBlock;



    double[] desiredLocation = {-60.0f, 35.0f};
    double desiredYaw;

    ElapsedTime     runtime = new ElapsedTime();
    @Override public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        lift_motor = hardwareMap.dcMotor.get("lift_motor");
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);



        GyroTurn internalGyro = new GyroTurn();
        try {
            internalGyro.initGyro(hardwareMap);
        } catch (InterruptedException e) {
            telemetry.addLine("Something went wrong w/ gyro");
        }

        //ConceptVuforiaSkyStoneNavigation vuforiaNavication = new ConceptVuforiaSkyStoneNavigation(hardwareMap);



        Encoder encoderDrive = new Encoder();

        SensorREVColor ColorSensor = new SensorREVColor(hardwareMap);

        waitForStart();

        encoderDrive.drivePID(20, leftDrive, rightDrive, 1, 1);
        encoderDrive.drivePID(1, lift_motor, lift_motor, 1, 1);
        lift_motor.setPower(0);

        while(!(ColorSensor.getDistanceOne() < 15)){
            leftDrive.setPower(0.25);
            rightDrive.setPower(0.25);
            telemetry.addLine("Inside");
            telemetry.update();
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);

        // 1 inch = 2.75 cm

        for (int i = 0; i < 3; i++) {
            encoderDrive.drivePID(2.75 * (ColorSensor.getDistanceOne() - 18), leftDrive, rightDrive, 1, 1);
        }

        firstBlock = ColorSensor.isYellow(true);

        secondBlock = ColorSensor.isYellow(false);


        telemetry.addData("firstBlock", firstBlock);
        telemetry.addData("secondBlock", secondBlock);
        if (firstBlock){
            if (secondBlock) {
                telemetry.addData("Skystone", 2);
            } else {
                telemetry.addData("Skystone", 3);
            }
        } else {
            telemetry.addData("Skystone", 1);
        }
        telemetry.update();

        //telemetry.addData("Distance", ColorSensor.getDistance());

        //telemetry.update();
        while(opModeIsActive()){}
        /*

        // My code
        // currentXLocation
        // currentYLocationdegreesToTurn
        // currentVuforiaYaw
        // desiredLocation[0] = x
        // desiredLocation[1] = y
        // desiredYaw

        // internalGyro.getAngle(); update angle
        // internalGyro.globalAngle; the angle

        //    vuforiaNavication.currentXLocation;
        //    vuforiaNavication.currentYLocation;
        //    vuforiaNavication.currentVuforiaYaw;
        while (!isStopRequested()) {


            vuforiaNavication.updateVuforia();
            internalGyro.getAngle();

            if (mode == "Find Target"){
                runtime.reset();
                while(runtime.seconds() < 0.2){};
                telemetry.addData("curentPower", "turn right");
                if (vuforiaNavication.targetVisible) {
                    mode = "Find Angle";
                } else {
                    internalGyro.turnDegrees(40, leftDrive, rightDrive);
                }
            } else if (mode == "Find Angle") {
                desiredYaw = updateDesiredLocation(desiredLocation[0], desiredLocation[1], vuforiaNavication.currentXLocation, vuforiaNavication.currentYLocation);
                telemetry.addData("X", vuforiaNavication.currentXLocation);
                telemetry.addData("Y", vuforiaNavication.currentYLocation);
                telemetry.update();
                internalGyro.globalAngle = vuforiaNavication.currentVuforiaYaw;
                runtime.reset();
                while(runtime.seconds() < 20){};
                mode = "Turn to Target";
            } else if (mode == "Turn to Target"){
                internalGyro.turnDegrees(desiredYaw, leftDrive, rightDrive);
                mode = "Drive to target";

            } else if (mode == "Drive to target"){

                encoderDrive.drivePID(Math.sqrt(Math.pow(vuforiaNavication.currentXLocation - desiredLocation[0], 2) + Math.pow(vuforiaNavication.currentYLocation - desiredLocation[1], 2)), leftDrive, rightDrive, 1, 1);

                telemetry.addData("curentPower", "Forward");
                mode = "Done";
                //leftDrive.setPower(0);
                //rightDrive.setPower(0);
            }


            telemetry.addData("desiredYaw", desiredYaw);
            telemetry.addData("Angle", internalGyro.globalAngle);
            telemetry.addData("currentVuforiaYaw", vuforiaNavication.currentVuforiaYaw);
            telemetry.addData("currentStat", mode);
            telemetry.update();

        }
        vuforiaNavication.deactivateVuforia();

         */
    }

    public double updateDesiredLocation(double wantedX, double wantedY, double currentX, double currentY){
        double[] desiredLocation = {0.0f, 0.0f};
        desiredLocation[0] = wantedX - currentX;
        desiredLocation[1] = wantedY - currentY;
        telemetry.addData("desiredXLocation", desiredLocation[0]);
        telemetry.addData("desiredYLocation", desiredLocation[1]);

        if (desiredLocation[0] > 0 && desiredLocation[1] >= 0){
            return Math.toDegrees(Math.atan(desiredLocation[1] / desiredLocation[0]));
        } else if (desiredLocation[0] <= 0 && desiredLocation[1] > 0){
            return Math.toDegrees(Math.atan((desiredLocation[0] * -1) / desiredLocation[1])) + 90;
        } else if (desiredLocation[0] <= 0 && desiredLocation[1] < 0){
            return Math.toDegrees(Math.atan((desiredLocation[1] * -1) / desiredLocation[0])) - 90;
        } else {
            return Math.toDegrees(Math.atan(desiredLocation[1] / desiredLocation[0]));
        }
    }

}
