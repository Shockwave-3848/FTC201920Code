package org.firstinspires.ftc.teamcode.Code;

//imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import java.lang.Math;
//imports

@TeleOp(name = "Shockwave Tank Drive V2 (Tank Control)", group = "Shockwave")
//@Disabled

public class Shockwave_Tank_Drive_Liam_Speed_Control extends OpMode{

    // Version number
    static double version_number = 3.2;

    // Defining the motors
    DcMotor left_hands_motor;
    DcMotor right_hands_motor;
    DcMotor lift_motor;
    DcMotor right_motor;
    DcMotor left_motor;

    // declaring power variables
    private double right_motor_power = 0;
    private double left_motor_power = 0;
    static  double hand_power = 1;
    static  double lift_power = 0.4;
    static  double trickle_power = -0.08;

    // declaring toggling varible(s)
    static int speed_toggle = 1;

    // is lifted?
    //private boolean is_lifted = false;

    // what is the max speed of the robot
    private double max_speed = 0.4; //used to cap the speed
    static  double lift_max_speed_up = 1;// used to cap the speed of the lift going up
    static  double lift_max_speed_down = 0.05;// used to cap the speed of the lift going down

    // true or false statements
    private boolean is_speed_halved = false;
    private boolean is_reversed = false;
    private boolean is_lift_raised = false;

    // used to reverse the robot
    private int flip = 1;

    // gamepad values ----------------------------------------------------------------------------------

    private boolean current_x_gamepad_value;
    private boolean current_a_gamepad_value;
    private boolean current_b_gamepad_value;
    private boolean current_y_gamepad_value;
    private boolean current_left_bumber_gamepad_value;
    private boolean current_right_bumber_gamepad_value;
    private double current_left_trigger_value;
    private double current_right_trigger_value;
    private double current_left_joystick_value;
    private double current_right_joystick_value;

    // gamepad values ----------------------------------------------------------------------------------

    public void get_gamepad_values(){

        current_x_gamepad_value = gamepad2.x;
        current_a_gamepad_value = gamepad2.a;
        current_b_gamepad_value = gamepad2.b;
        current_y_gamepad_value = gamepad2.y;
        current_left_bumber_gamepad_value = gamepad1.left_bumper;
        current_right_bumber_gamepad_value = gamepad1.right_bumper;
        current_left_trigger_value = gamepad2.left_trigger;
        current_right_trigger_value = gamepad2.right_trigger;
        current_left_joystick_value = gamepad1.left_stick_y;
        current_right_joystick_value = gamepad1.right_stick_y;

    }// end of get_gamepad_values

    // gamepad values ----------------------------------------------------------------------------------

    // hand methods ----------------------------------------------------------------------------------------------------

    // move_hand_away method
    public void move_hands_away(){

        left_hands_motor.setPower(-hand_power);
        right_hands_motor.setPower(hand_power);

    }// end of move_hands_away

    public void move_hands_closer(){

        int multiplier = 1;// I figured you would want to close faster than you open

        left_hands_motor.setPower(hand_power * multiplier);
        right_hands_motor.setPower(-hand_power * multiplier);

    }// end of move_hands_closer

    public void move_hands_left(){

        left_hands_motor.setPower(-hand_power);
        right_hands_motor.setPower(-hand_power);

    }//end of move hand left

    public void move_hands_right(){

        left_hands_motor.setPower(hand_power);
        right_hands_motor.setPower(hand_power);

    }//end of move hand left

    public void stop_hands(){

        left_hands_motor.setPower(0);
        right_hands_motor.setPower(0);

    }// end of stop hands

    // hand methods ----------------------------------------------------------------------------------------------------

    // lift methods ----------------------------------------------------------------------------------------------------

    // the trickle power to prevent the robot from falling
    public void trickle_power(){

        if (is_lift_raised == true) {
            lift_motor.setPower(trickle_power);
        }

    }// end of truckle power

    public void raise_lift(){

        lift_power = Math.sqrt(current_right_trigger_value);

        lift_motor.setPower(-lift_power * lift_max_speed_up);

        is_lift_raised = true;// engages trickle power

    }//end of raise_lift

    public void lower_lift(){

        lift_power = Math.sqrt(current_left_trigger_value);

        lift_motor.setPower(lift_power * lift_max_speed_down);

    }//end of lower_lift

    public void stop_lift(){

        if (is_lift_raised == false) {
            lift_motor.setPower(0);
        }

    }// end of stop_lift

    // lift methods ----------------------------------------------------------------------------------------------------

    // what should happen when the users presses the init button
    @Override
    public void init() {

        //sets the name for the motors that the code looks for in the program
        right_motor = hardwareMap.dcMotor.get("right_motor");
        left_motor = hardwareMap.dcMotor.get("left_motor");
        lift_motor = hardwareMap.dcMotor.get("lift_motor");
        left_hands_motor = hardwareMap.dcMotor.get("left_hand");
        right_hands_motor = hardwareMap.dcMotor.get("right_hand");

        //make the motor goes the same way as the left motor
        right_motor.setDirection(DcMotor.Direction.REVERSE);
        right_hands_motor.setDirection(DcMotor.Direction.REVERSE);

        //when zero power is flowing into the motors they should brake instead of being neutral
        right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //shows a message to the user that the code is working and how to drive the robot
        telemetry.addLine("Status: Initialized");
        telemetry.addData("This is a tank drive system, Version Number", version_number);
        telemetry.addLine( "To 1/4 your make speed hold the LEFT bumper. Relase the LEFT to regain max power.");
        telemetry.addLine("To reverse the motors hold the RIGHT bumper. To return them back to normal release the RIGHT bumper");
        telemetry.addLine("Press Y to OPEN the jaws, Press A to CLOSE jaws, Press X to move jaws LEFT, Press B to move jaws RIGHT");
        telemetry.addLine("Press the RIGHT TRIGGER to move the lift UP, Press the LEFT TRIGGER to move the lift DOWN");
        telemetry.addLine("Good Luck! Remember to have fun!");
        telemetry.update();

    }// end of init

    //what should the program do when the code is active
    @Override
    public void loop() {

        get_gamepad_values();// updates controller values

        //controller input --------------------------------------------------------------------------------------------

        /* Formula explained

        max_speed is the cap on how fast the robot can go

        gamepad1.right_stick_y is the right joy stick y pos

        gamepad1.right_stick_y is the left joy stick y pos

        flip controls which direction the robot is going
         */

        if(flip == 1) {
            right_motor_power = max_speed * current_right_joystick_value * flip;
            left_motor_power = max_speed * current_left_joystick_value * flip;
        }

        if(flip == -1) {
            left_motor_power = max_speed * current_right_joystick_value * flip;
            right_motor_power = max_speed * current_left_joystick_value * flip;
        }

        //controller input --------------------------------------------------------------------------------------------

        //the robot is not reversed
        if((current_right_bumber_gamepad_value == true) && (is_reversed == false)){

            // it is now reversed
            is_reversed = true;

            // this reverses the motor
            flip = flip * -1;

        }//the robot is now reversed

        // the robot is reversed
        if((current_right_bumber_gamepad_value == false) && (is_reversed == true)){

            // it is now not reversed
            is_reversed = false;

        }// the robot is now not reversed

        // speed is not halved; half it
        if((current_left_bumber_gamepad_value == true) && (is_speed_halved == false)){

            // halved speed
            is_speed_halved = true;

            // toggle
            speed_toggle = speed_toggle * -1;

            // different speeds
            if (speed_toggle == -1){

                max_speed = 0.4;
            }

            if (speed_toggle == 1){

                max_speed = 0.75;
            }
            // end of different speeds

        }// end of speed halving

        //speed is halved; double it
        if((current_left_bumber_gamepad_value == false) && (is_speed_halved == true)){
            is_speed_halved = false;
        }

        // jaw movement ----------------------------------------------------------------------------------


        // at default, the hands should not move or have trickle power
        if (current_y_gamepad_value == false && current_a_gamepad_value == false && current_x_gamepad_value == false && current_b_gamepad_value == false){

            stop_hands();

        }// end of stop hands

        // move the jaw open
        if (current_y_gamepad_value) {

            move_hands_away();

            telemetry.addLine("The jaw is opening"); // Message to Driver
            telemetry.update();

        }// end of if y

        // move the jaw close
        if (current_a_gamepad_value){

            move_hands_closer();

            telemetry.addLine("The jaw is closing"); // Message to Driver
            telemetry.update();

        }// end of if a

        // move the jaw left
        if (current_x_gamepad_value){

            move_hands_left();

            telemetry.addLine("The jaw is moving less"); // Message to Driver
            telemetry.update();

        }// end of if x

        // move the jaw right
        if (current_b_gamepad_value){

            move_hands_right();

            telemetry.addLine("The jaw is moving right"); // Message to Driver
            telemetry.update();

        }// end of if b

        // jaw movement ----------------------------------------------------------------------------------

        // lift movement ---------------------------------------------------------------------------------

        // at default, the lift should not move
        if (current_right_trigger_value == 0 && current_left_trigger_value == 0){

            stop_lift();

            // or

            trickle_power();

        }// end of stop lift

        // Raise the lift, makes sure that the other triger is not pressed
        if (current_right_trigger_value > 0 && current_left_trigger_value == 0){

            raise_lift();

            telemetry.addLine("The lift is raising"); // Message to Driver
            telemetry.addData("Power to Lift", lift_power);
            telemetry.update();

        }// end of > 0

        // Lower the lift, makes sure that the other triger is not pressed
        if (current_left_trigger_value > 0 && current_right_trigger_value == 0){

            lower_lift();

            telemetry.addLine("The lift is lowering"); // Message to Driver
            telemetry.addData("Power to Lift", lift_power);
            telemetry.update();

        }// end of > 0

        // lift movement ---------------------------------------------------------------------------------

        //sets power to controller input
        right_motor.setPower(right_motor_power);
        left_motor.setPower(left_motor_power);

        //sets power to controller input
        telemetry.addData("Current speed cap", max_speed);
        telemetry.addData("Current power to the RIGHT motor",right_motor_power);
        telemetry.addData("Current power to the LEFT motor", left_motor_power);

        if(flip == 1) {
            telemetry.addLine("Forward Direction: Claw");
        }
        if(flip == -1) {
            telemetry.addLine("Forward Direction: Point");
        }

        telemetry.update();

    }//end of loop

    //what should the robot do when the program is supposed to stop
    @Override
    public void stop() {
        right_motor.setPower(0);
        left_motor.setPower(0);
        //stop_hands();
        //stop_lift();

    }// end of stop

}//End of Shockwave_Tank_Drive_Nate_Speed_Control class