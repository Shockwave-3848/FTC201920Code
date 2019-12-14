package org.firstinspires.ftc.teamcode.Code;

//imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;
//imports

@TeleOp(name = "Shockwave_Drive", group = "Shockwave")
//@Disabled

public class Teleop extends OpMode{

    // Version number
    static double version_number = 2.3;

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
    static  double lift_power = 1;

    // what is the max speed of the robot
    private double max_speed = 0.75; //used to cap the speed
    private boolean is_speed_halved = false;
    private boolean is_reversed = false;
    private boolean from_up = false;

    // used to reverse the robot
    private int flip = 1;

    // hand methods ----------------------------------------------------------------------------------------------------

    // move_hand_away method
    public void move_hands_away(){

        left_hands_motor.setPower(-hand_power);
        right_hands_motor.setPower(hand_power);

    }// end of move_hands_away

    public void move_hands_closer(){

        int multiplier = 2;// I figured you would want to close faster than you open

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

    public void raise_lift(){

        lift_motor.setPower(lift_power);

    }//end of raise_lift

    public void lower_lift(){

        lift_motor.setPower(-lift_power);

    }//end of lower_lift

    public void stop_lift_up(){

        lift_motor.setPower(0.2);

    }//end of stop_lift

    public void stop_lift(){

        lift_motor.setPower(0);

    }//end of stop_lift

    // lift methods ----------------------------------------------------------------------------------------------------

    // what should happen when the users presses the init button
    @Override
    public void init() {

        //sets the name for the motors that the code looks for in the program
        right_motor = hardwareMap.dcMotor.get("right_drive");
        left_motor = hardwareMap.dcMotor.get("left_drive");
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
        left_hands_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right_hands_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

        //controller input --------------------------------------------------------------------------------------------

        /* Formula explained

        max_speed is the cap on how fast the robot can go

        gamepad1.right_stick_y is the right joy stick y pos

        gamepad1.right_stick_y is the left joy stick y pos

        flip controls which direction the robot is going
         */

        right_motor_power = max_speed * gamepad1.right_stick_y * flip;
        left_motor_power = max_speed * gamepad1.left_stick_y * flip;
        //controller input --------------------------------------------------------------------------------------------

        //the robot is not reversed
        if((gamepad1.right_bumper == true) && (is_reversed == false)){

            //it is now reversed
            is_reversed = true;

            //this reverses the motor
            flip = 1;

        }//the robot is now reversed

        //the robot is reversed
        if((gamepad1.right_bumper == false) && (is_reversed == true)){

            //it is now not reversed
            is_reversed = false;

            //this reverses the motor
            flip = -1;

        }//the robot is now not reversed

        //speed is not halved; half it
        if((gamepad1.left_bumper == false) && (is_speed_halved == false)){
            is_speed_halved = true;
            max_speed = 0.25;
        }

        //speed is halved; double it
        if((gamepad1.left_bumper == true) && (is_speed_halved == true)){
            is_speed_halved = false;
            max_speed = 0.75;
        }

        // jaw movement ----------------------------------------------------------------------------------

        // at default, the lift and hands should not move
        if (gamepad1.y == false && gamepad1.a == false && gamepad1.x == false && gamepad1.b == false){

            stop_hands();

        }// end of stop hands

        // move the jaw open
        if (gamepad1.y) {

            move_hands_away();

            telemetry.addLine("The jaw is opening"); // Message to Driver
            telemetry.update();

        }// end of if x

        // move the jaw close
        if (gamepad1.a){

            move_hands_closer();

            telemetry.addLine("The jaw is closing"); // Message to Driver
            telemetry.update();

        }// end of if b

        // move the jaw left
        if (gamepad1.x){

            move_hands_left();

            telemetry.addLine("The jaw is moving less"); // Message to Driver
            telemetry.update();

        }// end of if b

        // move the jaw right
        if (gamepad1.b){

            move_hands_right();

            telemetry.addLine("The jaw is moving right"); // Message to Driver
            telemetry.update();

        }// end of if b

        // jaw movement ----------------------------------------------------------------------------------

        // lift movement ---------------------------------------------------------------------------------

        // at default, the lift and hands should not move
        if (gamepad1.right_trigger == 0 && gamepad1.left_trigger == 0){
            if(from_up){
                stop_lift_up();
            } else {
                stop_lift();
            }

        }// end of stop lift

        // Raise the lift, makes sure that the other triger is not pressed
        if (gamepad1.right_trigger > 0 && gamepad1.left_trigger == 0){

            raise_lift();
            from_up = true;

            telemetry.addLine("The lift is raising"); // Message to Driver
            telemetry.update();

        }// end of > 0

        // Lower the lift, makes sure that the other triger is not pressed
        if (gamepad1.left_trigger > 0 && gamepad1.right_trigger == 0){

            lower_lift();
            from_up = false;

            telemetry.addLine("The lift is lowering"); // Message to Driver
            telemetry.update();

        }// end of > 0

        // lift movement ---------------------------------------------------------------------------------

        //sets power to controller input
        right_motor.setPower(right_motor_power);
        left_motor.setPower(left_motor_power);

        //informs drivers about the power to the robot
        telemetry.addData("Power to the right wheel", right_motor_power);
        telemetry.addData("Power to the left wheel", left_motor_power);
        telemetry.addData("Current speed cap", max_speed);
        telemetry.update();

    }//end of loop

    //what should the robot do when the program is supposed to stop
    @Override
    public void stop() {
        right_motor.setPower(0);
        left_motor.setPower(0);
        stop_hands();
        stop_lift();

    }// end of stop

}//End of Shockwave_Tank_Drive_Liam_Speed_Control class