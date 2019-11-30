package org.firstinspires.ftc.teamcode.Code;

// imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;
// imports

@TeleOp(name = "Sky Stone Teleop", group = "Shockwave")
//@Disabled

public class Teleop extends OpMode {

    // Version number
    static double version_number = 2.0;

    // Defining the motors
    DcMotor left_hands_motor;
    DcMotor right_hands_motor;
    DcMotor left_lift_motor;
    DcMotor right_lift_motor;

    // Power
    static final double jaw_power = 1;
    static final double lift_power = 0.5;

    // hand methods ----------------------------------------------------------------------------------------------------

    public void move_hands_away(){
        left_hands_motor.setPower(-jaw_power);
        right_hands_motor.setPower(jaw_power);
    }

    public void move_hands_closer(){
        left_hands_motor.setPower(jaw_power);
        right_hands_motor.setPower(-jaw_power);
    }

    public void slew_right(){

        left_hands_motor.setPower(-jaw_power);
        right_hands_motor.setPower(-jaw_power);

    }

    public void slew_left(){

        left_hands_motor.setPower(jaw_power);
        right_hands_motor.setPower(jaw_power);

    }

    public void stop_hands(){

        left_hands_motor.setPower(0);
        right_hands_motor.setPower(0);

    }// end of stop hands

    // hand methods ----------------------------------------------------------------------------------------------------

    // lift methods ----------------------------------------------------------------------------------------------------

    public void raise_lift(){

        left_lift_motor.setPower(lift_power);
        right_lift_motor.setPower(lift_power);

    }//end of raise_lift

    public void lower_lift(){

        left_lift_motor.setPower(-lift_power);
        right_lift_motor.setPower(-lift_power);

    }//end of lower_lift

    public void stop_lift(){

        left_lift_motor.setPower(0);
        right_lift_motor.setPower(0);

    }//end of stop_lift

    // lift methods ----------------------------------------------------------------------------------------------------

    // where the actual task happen ------------------------------------------------------

    //what should happen when the users presses the init button
    @Override
    public void init() {

        //sets the name for the motors looks for in the program
        right_hands_motor = hardwareMap.dcMotor.get("right hand motor");
        left_hands_motor = hardwareMap.dcMotor.get("left hand motor");
        //left_lift_motor = hardwareMap.dcMotor.get("left lift motor");
        //right_lift_motor = hardwareMap.dcMotor.get("right lift motor");

        //make the motor goes the same way as the left motor
        right_hands_motor.setDirection(DcMotor.Direction.REVERSE);
        //right_lift_motor.setDirection(DcMotor.Direction.REVERSE);

        //when zero power is flowing into the motors they should brake instead of being neutral
        right_hands_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_hands_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //left_lift_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //right_lift_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //shows messages to the user that the code is working
        telemetry.addData("This is version", version_number);// what version is this?
        telemetry.addLine("Status: Initialized");
        telemetry.addLine("This code controls the arm");
        telemetry.addLine("To raise the lift use the RIGHT TRIGGER");
        telemetry.addLine("To lower the lift use the LIFT TRIGGER");
        telemetry.addLine("To open the jaw use the X BUTTON");
        telemetry.addLine("To close the jaw use the B BUTTON");
        telemetry.addLine("Good Luck! Remember to have fun!");
        telemetry.update();

    }// end of init

    // what should the program do when the code is active
    @Override
    public void loop() {

        // at default, the lift and hands should not move
        stop_hands();
        //stop_lift();

        // jaw movement ----------------------------------------------------------------------------------

        // slew right
        if (gamepad1.b) {

            slew_right();

            telemetry.addLine("The jaw is going right"); // Message to Driver
            telemetry.update();

        } else // end of if b

        // slew left
        if (gamepad1.x){

            slew_left();

            telemetry.addLine("The jaw is going left"); // Message to Driver
            telemetry.update();

        } else // end of if x

        // open
        if (gamepad1.y) {

            move_hands_away();

            telemetry.addLine("The jaw is opening"); // Message to Driver
            telemetry.update();

        } else // end of if b

        // slew left
        if (gamepad1.a){

            move_hands_closer();

            telemetry.addLine("The jaw is closing"); // Message to Driver
            telemetry.update();

        }// end of if x

        // jaw movement ----------------------------------------------------------------------------------

        /*
        // lift movement ---------------------------------------------------------------------------------

        // Raise the lift, makes sure that the other triger is not pressed
        if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0){

            raise_lift();

            telemetry.addLine("The lift is raising"); // Message to Driver
            telemetry.update();

        }// end of > 0

        // Lower the lift, makes sure that the other triger is not pressed
        if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0){

            lower_lift();

            telemetry.addLine("The lift is lowering"); // Message to Driver
            telemetry.update();

        }// end of > 0

        // lift movement ---------------------------------------------------------------------------------
        */


    }// end of loop

}// end of Arm Control