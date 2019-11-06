package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;



@Autonomous(name="Encoder", group ="Concept")
//@Disabled
public class Encoder extends LinearOpMode {
    private DcMotor         leftDrive = null;
    private DcMotor         rightDrive = null;
    double                  power = 0.25;
    @Override public void runOpMode() throws InterruptedException {
        leftDrive = hardwareMap.get(DcMotor.class, "left_motor");
        rightDrive = hardwareMap.get(DcMotor.class, "right_motor");
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // reset encoder count kept by left motor.
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        leftDrive.setTargetPosition(500);
        rightDrive.setTargetPosition(500);

        // set left motor to run to target encoder position and stop with brakes on.
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        leftDrive.setPower(power);
        rightDrive.setPower(power);

        // wait while opmode is active and left motor is busy running to position.

        while (opModeIsActive() && leftDrive.isBusy() && rightDrive.isBusy())
        {
            telemetry.addData("left encoder-fwd", leftDrive.getCurrentPosition());
            telemetry.addData("right encoder-fwd", rightDrive.getCurrentPosition());
            telemetry.update();
            idle();
        }

        // set motor power to zero to turn off motors. The motors stop on their own but
        // power is still applied so we turn off the power.

        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
    }
}
