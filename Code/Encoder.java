package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.lang.Math;



//@Autonomous(name="Encoder", group ="Concept")
//@Disabled
public class Encoder {
    static double                  maxTime = 2.5;
    static double                  startPower;
    // for compitition robot
    static final double            HD_MOTOR_TICK_COUNT = 30;

    static double                  gearRatio = 15;
    static double                  wheelDiamiter = 3.54;
    /*

    static final double            HD_MOTOR_TICK_COUNT = 4;

    static double                  gearRatio = 72;
    static double                  wheelDiamiter = 3.54;
*/
    static int                     ticksPerInch = (int) ((HD_MOTOR_TICK_COUNT * gearRatio) / (wheelDiamiter * Math.PI));

    public static void driveNormal(float inchsToRole, DcMotor leftDrive, DcMotor rightDrive) {
        ElapsedTime runtime = new ElapsedTime();
        startPower = 1.0;

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        int ticksToMove = (int) inchsToRole * ticksPerInch;
        leftDrive.setTargetPosition(ticksToMove + leftDrive.getCurrentPosition());
        rightDrive.setTargetPosition(ticksToMove + rightDrive.getCurrentPosition());


        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(startPower);
        rightDrive.setPower(startPower);

        runtime.reset();
        while((leftDrive.isBusy() && rightDrive.isBusy()) && !(leftDrive.getPower() < 0.1 && rightDrive.getPower() < 0.1 ) && (runtime.seconds() < maxTime)){};

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        // Turn off RUN_TO_POSITION
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static void drivePID(double inchsToRole, DcMotor leftDrive, DcMotor rightDrive, int driveForward) {
        ElapsedTime runtime = new ElapsedTime();
        startPower = 0.5;
        maxTime = 5;
        int tolerance = 5;

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        int ticksToMove = (int) inchsToRole * ticksPerInch;
        leftDrive.setTargetPosition(ticksToMove + leftDrive.getCurrentPosition());
        rightDrive.setTargetPosition(ticksToMove + rightDrive.getCurrentPosition());


        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftDrive.setPower(driveForward * startPower);
        rightDrive.setPower(driveForward * startPower);

        runtime.reset();
        while(((leftDrive.getCurrentPosition() * driveForward) < ticksToMove + tolerance)
                && ((rightDrive.getCurrentPosition() * driveForward) < ticksToMove + tolerance)
                && (runtime.seconds() < maxTime))
        {};

        leftDrive.setPower(0);
        rightDrive.setPower(0);

        // Turn off RUN_USING_ENCODER
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
