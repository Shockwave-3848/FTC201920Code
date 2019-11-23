package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.lang.Math;



//@Autonomous(name="Encoder", group ="Concept")
//@Disabled
public class Encoder {
    static double                  startPower = 0.25;
    /* for compitition robot
    final double            HD_MOTOR_TICK_COUNT = 30;

    double                  gearRatio = 15;
    double                  wheelDiamiter = 3.54;
    */

    static final double            HD_MOTOR_TICK_COUNT = 4;

    static double                  gearRatio = 72;
    static double                  wheelDiamiter = 3.54;

    static int                     ticksPerInch = (int) ((HD_MOTOR_TICK_COUNT * gearRatio) / (wheelDiamiter * Math.PI));

    public static void drive(float inchsToRole, DcMotor leftDrive, DcMotor rightDrive) {

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        int ticksToMove = (int) inchsToRole * ticksPerInch;
        leftDrive.setTargetPosition(ticksToMove + leftDrive.getCurrentPosition());
        rightDrive.setTargetPosition(ticksToMove + leftDrive.getCurrentPosition());


        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(startPower);
        rightDrive.setPower(startPower);

        while(leftDrive.isBusy() && rightDrive.isBusy()){}

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        // Turn off RUN_TO_POSITION
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
