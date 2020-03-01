package org.firstinspires.ftc.teamcode.Code.unused;
/*

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Code.Encoder;
import org.firstinspires.ftc.teamcode.Code.GyroTurn;
import org.firstinspires.ftc.teamcode.Code.SensorREVColor;

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

    static final int motor_revcount = 2240; //2240 ticks per rotation for the REV-41-1301 Encoder Specifications. We use the Rev-41-1291, but I could not find the specific values. The REV-41-1301 is similar enough for this to work hopefully. If the robot is not working correctly try other values.
    static final double wheel_diameter = 3.6;
    static final double counts_per_inch = (motor_revcount) / (wheel_diameter * Math.PI);
    @Override public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "lift_motor");
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

        drive(-24, 0.5);
        encoderDrive.drivePID(1, lift_motor, lift_motor, 1, 1);
        lift_motor.setPower(0.1);

        runtime.reset();
        while(runtime.seconds() < 10){}

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
/*
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

    public void drive(double inches, double speed){

        //declaring variables, where is it going?
        int new_left_target;
        int new_right_target;

        // If the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            new_left_target = leftDrive.getCurrentPosition() + (int)(inches * counts_per_inch);
            new_right_target = rightDrive.getCurrentPosition() + (int)(inches * counts_per_inch);

            //set the target
            leftDrive.setTargetPosition(new_left_target);
            rightDrive.setTargetPosition(new_right_target);
            //finished setting the target

            //turn on RUN_TO_POSITION
            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //rstart motion.
            leftDrive.setPower(Math.abs(speed));
            rightDrive.setPower(Math.abs(speed));

            //this loop updates the user about the postion of the wheels
            while (opModeIsActive() && (rightDrive.isBusy()) && (leftDrive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", new_left_target,  new_right_target);
                telemetry.addData("Path2",  "Running at %7d :%7d", leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition());
                telemetry.update();
            }

            //end of setting the target

        }//end of opModeIsActive()

    }//end of drive

}*/
