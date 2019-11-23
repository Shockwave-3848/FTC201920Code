package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Math;


@Autonomous(name="SKYSTONE Vuforia Nav", group ="Concept")
//@Disabled
public class vuforiaNav extends LinearOpMode {
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    double                  power = 0.25;

    String mode = "Find Target";

    //double[] desiredLocation = {-50.0f, 30.0f};
    //double desiredYaw;

    ElapsedTime     runtime = new ElapsedTime();
    @Override public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "left_motor");
        rightDrive = hardwareMap.get(DcMotor.class, "right_motor");
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);



        GyroTurn internalGyro = new GyroTurn();
        try {
            internalGyro.initGyro(hardwareMap);
        } catch (InterruptedException e) {
            telemetry.addLine("Something went wrong");
        }

        //ConceptVuforiaSkyStoneNavigation vuforiaNavication = new ConceptVuforiaSkyStoneNavigation(hardwareMap);

        ConceptTensorFlowObjectDetection objectDetection = new ConceptTensorFlowObjectDetection(hardwareMap);

        Encoder encoderDrive = new Encoder();

        waitForStart();

        runtime.reset();
        while(runtime.seconds() < 2.5){
            objectDetection.updatTfod();
        }



        turnDegrees(90, internalGyro);


        encoderDrive.drive(8, leftDrive, rightDrive);

        turnDegrees(-90, internalGyro);

        objectDetection.location = 2;

        runtime.reset();
        while(runtime.seconds() < 2.5){
            objectDetection.updatTfod();
        }

        if (objectDetection.firstBlock == 0 || objectDetection.firstBlock == 1 ){
            if (objectDetection.secondBlock == 0 || objectDetection.secondBlock == 1 ) {
                telemetry.addData("Skystone", 3);
            } else {
                telemetry.addData("Skystone", 2);
            }
        } else {
            telemetry.addData("Skystone", 1);
        }
        telemetry.update();

        objectDetection.shutDown();

        while(true){}

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
        /*while (!isStopRequested()) {


            vuforiaNavication.updateVuforia();
            internalGyro.getAngle();

            //desiredYaw = updateDesiredLocation(desiredLocation[0], desiredLocation[1], vuforiaNavication.currentXLocation, vuforiaNavication.currentYLocation);


            if (mode == "Find Target"){
                //leftDrive.setPower(0.15);
                //rightDrive.setPower(-0.15);
                telemetry.addData("curentPower", "turn right");
                if (vuforiaNavication.targetVisible){
                    mode = "Find Angle";
                }
            } else if (mode == "Find Angle") {
                desiredYaw = updateDesiredLocation(desiredLocation[0], desiredLocation[1], vuforiaNavication.currentXLocation, vuforiaNavication.currentYLocation);
                internalGyro.globalAngle = vuforiaNavication.currentVuforiaYaw;
                mode = "Turn to Target";
            } else if (mode == "Turn to Target"){
                if (internalGyro.globalAngle > desiredYaw - 2 && internalGyro.globalAngle < desiredYaw + 2){
                    mode = "Drive to target";
                }
                else if (desiredYaw > internalGyro.globalAngle) {
                    //leftDrive.setPower(-0.15);
                    //rightDrive.setPower(0.15);
                    telemetry.addData("curentPower", "turn left");
                } else if (desiredYaw < internalGyro.globalAngle) {
                    telemetry.addData("curentPower", "turn right");
                    //leftDrive.setPower(0.15);
                    //rightDrive.setPower(-0.15);
                }
            } else if (mode == "Drive to target"){
                telemetry.addData("curentPower", "Forward");
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

    public void turnDegrees(int degreesToTurn, GyroTurn internalGyro){
        PIDController           pidRotate;
        internalGyro.globalAngle = 0;

        pidRotate = new PIDController(0.01, 0.0, 0.01);
        pidRotate.reset();
        pidRotate.setSetpoint(degreesToTurn);
        pidRotate.setInputRange(-180, 180);
        pidRotate.setOutputRange(0, 1);
        pidRotate.setTolerance(5);
        pidRotate.enable();

        //while (!pidRotate.onTarget()){
        while(!(internalGyro.globalAngle <= degreesToTurn + 0.25 && internalGyro.globalAngle >= degreesToTurn - 0.25)){
            internalGyro.getAngle();
            telemetry.addData("Angle", internalGyro.globalAngle);
            power = pidRotate.performPID(internalGyro.globalAngle); // power will be - on right turn.

            leftDrive.setPower(power);
            rightDrive.setPower(-power);

            telemetry.addData("Power", power);
            telemetry.update();
        }
        telemetry.addLine("donw");
        telemetry.update();
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
