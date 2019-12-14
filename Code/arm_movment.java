package org.firstinspires.ftc.teamcode.Code;

// imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;
// imports

@Autonomous(name="arm_movment", group ="Concept")
//@Disabled

public class arm_movment extends LinearOpMode {

    // Defining the motors
    DcMotor left_hands_motor;
    DcMotor right_hands_motor;

    // Power
    static final double jaw_power = 1;


    // hand methods ----------------------------------------------------------------------------------------------------

    // where the actual task happen ------------------------------------------------------

    //what should happen when the users presses the init button
    @Override
    public void runOpMode() {

        right_hands_motor = hardwareMap.dcMotor.get("right hand motor");
        left_hands_motor = hardwareMap.dcMotor.get("left hand motor");

        right_hands_motor.setDirection(DcMotor.Direction.REVERSE);

        right_hands_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_hands_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Encoder encoderArm = new Encoder();

        encoderArm.gearRatio = 9.7;

        encoderArm.wheelDiamiter = 0.1;
        encoderArm.updateTickCount();

        moveArm(encoderArm, 2.0, 0.5, "Close");

    }// end of init

    public void moveArm(Encoder encoderArm, double inches, double power, String directions){
        encoderArm.startPower = power;
        if (directions == "Close") {
            encoderArm.drivePID(inches, left_hands_motor, right_hands_motor, 1, -1);
        } else if (directions == "Open"){
            encoderArm.drivePID(inches, left_hands_motor, right_hands_motor, -1, 1);
        } else if (directions == "Right"){
            encoderArm.drivePID(inches, left_hands_motor, right_hands_motor, 1, 1);
        } else if (directions == "Left"){
            encoderArm.drivePID(inches, left_hands_motor, right_hands_motor, -1, -1);
        }
    }

}// end of Arm Control