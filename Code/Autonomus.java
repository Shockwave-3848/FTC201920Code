package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.lang.Math;


@Autonomous(name="Little Drive", group ="Concept")
//@Disabled
public class Autonomus extends LinearOpMode {
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
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
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);


        GyroTurn internalGyro = new GyroTurn();
        try {
            internalGyro.initGyro(hardwareMap);
        } catch (InterruptedException e) {
            telemetry.addLine("Something went wrong w/ gyro");
        }


        Encoder encoderDrive = new Encoder();

        encoderDrive.updateTickCount();



        //encoderDrive.drivePID(20, leftDrive, rightDrive, 1, 1);

        //internalGyro.turnDegrees(90, leftDrive,rightDrive);
    }

}
