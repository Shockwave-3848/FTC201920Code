package org.firstinspires.ftc.teamcode.Code;

//imports
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//imports

@Autonomous(name="Sky_Stone_Endcoder_Auto_Drive", group="Shockwave")
//@Disabled

//name of class
public class Autonomus extends LinearOpMode {

    // Version number
    static double version_number = 3.9;

    // Variables --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    GyroTurn internalGyro;

    ElapsedTime runtime = new ElapsedTime();

    // Defining the wheels there motors
    DcMotor left_motor;
    DcMotor right_motor;

    // Defining the IMU sensor object=
    BNO055IMU imu;

    // Used for IMU turning
    Orientation angles;

    // A little forgiveness for the robot's turning
    static double tolerance = 0;

    // Wheel stats
    static final int motor_revcount = 28; //28 ticks per rotation for a 15:1 motor //4 ticks at output for core hex motor (72:1)
    static final int gear_ratio = 15;
    static final double wheel_diameter = 3.54;
    static final double counts_per_inch = (motor_revcount * gear_ratio) / (wheel_diameter * Math.PI);

    // Speed
    static final double speed = 0.20;

    // Speed stats for turning
    static double turn_drive_speed = 0.5;
    static double turn_top_speed = 0.5;
    static double turn_bottom_speed = 0.2;

    // Can allies go to foundation?
    static String can_allies_move_foundation = "not inputed";

    // Has the pos_inputed
    static String up_or_down = "not inputed";

    // Red or blue?
    static String red_or_blue = "not inputed";

    // Which block?
    static String block_num = "not inputed";

    // Confirm
    static String confirm = "not inputed";

    // How many inches from the wall should the robot go if aiming low on the bridge
    static int pull_away_inches = 4;

    // Variables --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // where are we
    public void where_are_we() {

        telemetry.addData("Hello drivers, this is the autonomous parking program version", version_number); // what version is this?

        //Can our allies move foundation?
        telemetry.addLine("Is our alliance partner going to move foundation?");
        telemetry.addLine( "Press Y if YES");
        telemetry.addLine("Press A if NO");
        telemetry.update();

        // Are we blue or red?
        while (can_allies_move_foundation == "not inputed") {
            if (gamepad1.y){
                can_allies_move_foundation = "Yes";
            }

            if (gamepad1.a){
                can_allies_move_foundation = "No";
            }

        }// finished setting red or blue

        telemetry.addLine("Please input color. Press X if we are Blue and press B if we are Red");
        telemetry.update();

        // Are we blue or red?
        while (red_or_blue == "not inputed") {
            if (gamepad1.x){
                red_or_blue = "Blue";
            }

            if (gamepad1.b){
                red_or_blue = "Red";
            }

        }// finished setting red or blue

        // High or low message
        telemetry.addLine("Do we want to park up or down? Press the Dpad UP if we should park high and press the Dpad DOWN if we should park low");
        telemetry.update();

        // Do we want to park high or low?
        while (up_or_down == "not inputed") {
            if (gamepad1.dpad_up){
                up_or_down = "Up";
            }

            if (gamepad1.dpad_down){
                up_or_down = "Down";
            }

        }// finished setting up or down

        // Block number message
        telemetry.addLine("Which block do we start in?");
        telemetry.addLine("Press X if Block One");
        telemetry.addLine("Press A if Block Two");
        telemetry.addLine("Press B if block Three");
        telemetry.addLine("Press Y if block Four");
        telemetry.addLine("Remember Block One starts under the building site and so on");
        telemetry.update();

        // Which block are starting on? Remember that blocks start from the building site down four blocks
        while (block_num == "not inputed") {
            if (gamepad1.x){
                block_num = "1";
            }

            if (gamepad1.a){
                block_num = "2";
            }

            if (gamepad1.b){
                block_num = "3";
            }

            if (gamepad1.y){
                block_num = "4";
            }

        }// finished setting block num

        telemetry.addLine("Does this look correct?");
        telemetry.addData("Are our ally going to move the foundation?", can_allies_move_foundation);
        telemetry.addData("We are on team", red_or_blue); // is it set to the right team?
        telemetry.addData("We want to park", up_or_down); // is it set to the disired parking spot?
        telemetry.addData("We start on block", block_num); // is it set to the starting block?
        telemetry.addLine("Press LEFT_BUMBER to confirm, Press RIGHT_BUMBER to start over");
        telemetry.update();

        //Confirmation
        while (confirm == "not inputed") {

            if (gamepad1.left_bumper){
                confirm = "ready to go!";
            }

            if (gamepad1.right_bumper){
                confirm = "reset";
            }

        }// all set to go

        if (confirm == "reset"){

            //Reset varibles ---------

            // Can allies go to foundation?
            can_allies_move_foundation = "not inputed";

            // Has the pos_inputed
            up_or_down = "not inputed";

            // Red or blue?
            red_or_blue = "not inputed";

            // Which block?
            block_num = "not inputed";

            // Confirm
            confirm = "not inputed";

            //Reset varibles ---------

            where_are_we();//reset

        }//resets everything

        telemetry.addLine("Have fun drivers! Remember that is why we do this. Now go kick their butt!");
        telemetry.update();
    }

    // drive method
    public void drive(int inches){

        // target
        int new_rw_target; //right wheel
        int new_lw_target; //left wheel
        // int new_test_target; //use for testing only

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            //reset encoder
            left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //test_motor.setMode(DcMotor.RunMode.RESET_ENCODERS);

            //flw target
            new_lw_target = left_motor.getCurrentPosition() + (int)(inches * counts_per_inch);//gives wheels the motor can use
            left_motor.setTargetPosition(new_lw_target);

            //frw target
            new_rw_target = right_motor.getCurrentPosition() + (int)(inches * counts_per_inch);//gives wheels the motor can use
            right_motor.setTargetPosition(new_rw_target);

            //test target
            //new_test_target = test_motor.getCurrentPosition() + (int) (inches * counts_per_inch);//gives wheels the motor can use
            //test_motor.setTargetPosition(new_test_target);

            //sets power
            right_motor.setPower(speed);
            left_motor.setPower(speed);

            // Turn On RUN_TO_POSITION
            left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //test_motor.setMode(DcMotor.RunMode.RESET_ENCODERS);

            //makes sure the robot is not broken
            runtime.reset();
            while (opModeIsActive() && (left_motor.isBusy() || right_motor.isBusy()) && runtime.seconds() < 3) {

                telemetry.addLine("Am I working?");
                telemetry.addData("Speed", speed);
                telemetry.addData("new_lw_target", new_lw_target);
                telemetry.addData("new_rw_target", new_rw_target);
                telemetry.addData("right wheel currently", right_motor.getCurrentPosition());
                telemetry.addData("left wheel currently", left_motor.getCurrentPosition());
                //telemetry.addData("new_test_motor:", test_motor);
                telemetry.update();

            }//opmode check 2

            // Turn Off RUN_TO_POSITION
            left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            left_motor.setPower(0.0);
            right_motor.setPower(0.0);

        }//opmode check 1

    }//end of drive method

    //start of init_gyro
    public void init_gyro() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU; //what sensor is it
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES; // what unit of turn do we use
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC; //what unit of speed to we use
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu"); // defines the imu

        imu.initialize(parameters);//start this baby up

    }//end of init_gyro

    public void angle_turn(double angle_I_want) {
        //internalGyro.test(telemetry);
        internalGyro.turnDegrees(angle_I_want, left_motor, right_motor, telemetry);
        //telemetry.addData("angle", internalGyro.globalAngle);

        runtime.reset();
        while(runtime.seconds() < 5){
          //  telemetry.update();
        }
        /*
        boolean finding_angle = true;

        //finds angles
        init_gyro();
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        telemetry.addData("Angle We Are", angles);
        telemetry.addData("Angle We Want", angle_I_want);
        telemetry.update();

        while (finding_angle == true) {

            //finds angles
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            //The bigger this is the closer it has to be to slow down
            double multiplier = 1;
            double angle_diff;


            angles.firstAngle = xy plane
            angles.secondAngle = tilt
            angles.thirdAngle = roll


            //correct the angel----------

            if (Math.round(angles.thirdAngle) - tolerance > angle_I_want) {

                //Slows down before finding correct angle to prevent the robot from over shoting it
                angle_diff = (angles.thirdAngle - angle_I_want);
                turn_drive_speed = Math.min(Math.max((angle_diff * multiplier), turn_top_speed), turn_bottom_speed);

                left_motor.setPower(-turn_drive_speed);
                right_motor.setPower(turn_drive_speed);

            }

            else if (Math.round(angles.thirdAngle) + tolerance < angle_I_want) {

                //Slows down before finding correct angle to prevent the robot from over shoting it
                angle_diff = (angle_I_want - angles.thirdAngle);
                turn_drive_speed = Math.min(Math.max((angle_diff * multiplier), turn_top_speed), turn_bottom_speed);

                left_motor.setPower(turn_drive_speed);
                right_motor.setPower(-turn_drive_speed);

            }

            //correct the angel----------

            else {

                //Tells user the robot found the right angle
                //telemetry.addData("On", "track!!!");
                //telemetry.update();

                //Stops all motion
                left_motor.setPower(0);
                right_motor.setPower(0);

                //FOUND ANGLE :)
                finding_angle = false;

            }

            //Updates user
            telemetry.addData("First angle is", Math.round(angles.firstAngle));
            telemetry.addData("The angle I am going to",angle_I_want);
            telemetry.addData("Turn speed", turn_drive_speed);
            telemetry.update();

        }*/

    }//end of find angle

    public void turn_around(int num_of_turns){

        //turn the amount of times specified
        for (int i = 0; i < num_of_turns ; i++){

            angle_turn(90);

        }// end of for loop

    }// end of turn around

    //where the actual task happen
    @Override
    public void runOpMode() {
        internalGyro = new GyroTurn();
        try {
            internalGyro.initGyro(hardwareMap);
        } catch (InterruptedException e) {
            telemetry.addLine("Something went wrong w/ gyro");
        }

        // names the motors
        left_motor = hardwareMap.dcMotor.get("left_drive");
        right_motor = hardwareMap.dcMotor.get("right_drive");
        //test_motor = hardwareMap.dcMotor.get("test");

        //reset varibles -----------------------------------------------------------------------------

        // Can allies go to foundation?
        can_allies_move_foundation = "not inputed";

        // Has the pos_inputed
        up_or_down = "not inputed";

        // Red or blue?
        red_or_blue = "not inputed";

        // Which block?
        block_num = "not inputed";

        // Confirm
        confirm = "not inputed";

        //reset varibles -----------------------------------------------------------------------------

        // make the motors go the same way
        right_motor.setDirection(DcMotor.Direction.FORWARD);
        left_motor.setDirection(DcMotor.Direction.REVERSE);

        //init_gyro();// initialized gyro

        where_are_we();// where are we on the field?

        // For testing
        // telemetry.addLine("All Set!");
        // telemetry.update();

        waitForStart();// wait for the user to press start

        // For testing
        ///*

        //if allies can move foundation
        if(can_allies_move_foundation == "Yes") {

            // BLUE ------------------------------------------------------------------------------------------

            if (red_or_blue == "Blue") {

                // UP -----------------------------------------------

                if (up_or_down == "Up") {

                    if (block_num == "1") {
                        telemetry.addLine("In Park - hi");

                        drive(24);// drive up

                        angle_turn(-90);// turn left

                        drive(38);// park under bridge

                    }// first block

                    if (block_num == "2") {

                        drive(24);// drive up

                        angle_turn(-90);// turn left

                        drive(15);// park under bridge

                    } // second block

                    if (block_num == "3") {

                        drive(24);// drive up

                        angle_turn(90);// turn right

                        drive(15);// park under bridge

                    } // third block

                    if (block_num == "4") {

                        drive(24);// drive up

                        angle_turn(90);// turn right

                        drive(38);// park under bridge

                    } // forth block


                }// if up

                // UP -----------------------------------------------

                // DOWN ---------------------------------------------

                if (up_or_down == "Down") {

                    if (block_num == "1") {

                        drive(pull_away_inches);// GET AWAY

                        angle_turn(-90);

                        drive(38);

                    }// first block

                    if (block_num == "2") {

                        drive(pull_away_inches);

                        angle_turn(-90);

                        drive(15);

                    } // second block

                    if (block_num == "3") {

                        drive(pull_away_inches);

                        angle_turn(90);

                        drive(15);

                    } // third block

                    if (block_num == "4") {

                        drive(pull_away_inches);

                        angle_turn(90);

                        drive(38);

                    } // forth block


                }// if down

                // DOWN ---------------------------------------------

            }// if blue

            // BLUE ------------------------------------------------------------------------------------------

            // RED ------------------------------------------------------------------------------------------

            if (red_or_blue == "Red") {

                // UP -----------------------------------------------

                if (up_or_down == "Up") {

                    if (block_num == "1") {

                        drive(24);

                        angle_turn(90);

                        drive(38);

                    }// first block

                    if (block_num == "2") {

                        drive(24);

                        angle_turn(90);

                        drive(15);

                    } // second block

                    if (block_num == "3") {

                        drive(24);

                        angle_turn(-90);

                        drive(15);

                    } // third block

                    if (block_num == "4") {

                        drive(24);

                        angle_turn(-90);

                        drive(38);

                    } // forth block


                }// if up

                // UP -----------------------------------------------

                // DOWN ---------------------------------------------

                if (up_or_down == "Down") {

                    if (block_num == "1") {

                        drive(pull_away_inches);

                        angle_turn(90);

                        drive(38);

                    }// first block

                    if (block_num == "2") {

                        drive(pull_away_inches);

                        angle_turn(90);

                        drive(15);

                    } // second block

                    if (block_num == "3") {

                        drive(pull_away_inches);

                        angle_turn(-90);

                        drive(15);

                    } // third block

                    if (block_num == "4") {

                        drive(pull_away_inches);

                        angle_turn(-90);

                        drive(38);

                    } // forth block

                }// if down

                // DOWN ---------------------------------------------

            }// if RED

            // RED ------------------------------------------------------------------------------------------

        }// end of non moving foundation code

        // if allies cannot move foundation
        if (can_allies_move_foundation == "No") {

            // BLUE ------------------------------------------------------------------------------------------

            // we are blue
            if (red_or_blue == "Blue") {

                // we want to park up
                if (up_or_down == "Up"){

                    if (block_num == "1") {
                        telemetry.addLine("In Foundation");

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn right

                        drive(14);// drive toward bridge

                        angle_turn(90);// turn left

                        // Add two inches to annoy others
                        drive(38);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        // Add 3 inches because of foundation pushing
                        drive(-40); // drive backwards to push the foundations apart

                        drive(5);// back off a little

                        angle_turn(-90);// turn to face the foundations

                        drive(48);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                    }

                    if (block_num == "2") {

                        drive(24);// drive toward fountation

                        angle_turn(90);// turn toward build site wall

                        drive(12);// drive toward build site wall

                        angle_turn(0);// turn to face opposite team

                        drive(36);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(36);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                    }

                    if (block_num == "3") {

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn left

                        drive(36);// drive toward bridge

                        angle_turn(90);// turn right

                        drive(24);// get in the middle of the foundations

                        angle_turn(90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                    }

                    if (block_num == "4") {

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn left

                        drive(48);// drive toward bridge

                        angle_turn(90);// turn right

                        drive(24);// get in the middle of the foundations

                        angle_turn(90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                    }

                }// end of parking up

                // we want to park down
                if (up_or_down == "Down"){

                    if (block_num == "1") {

                        drive(24);// drive toward foundations

                        angle_turn(90);// turn right

                        drive(12);// drive toward bridge

                        angle_turn(-90);// turn left

                        drive(24);// get in the middle of the foundations

                        angle_turn(90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(90);// turn

                        drive(12);// park lower

                    }

                    if (block_num == "2") {

                        drive(12);// drive toward fountation

                        angle_turn(-90);// turn toward build site wall

                        drive(6);// drive toward build site wall

                        angle_turn(90);// turn to face opposite team

                        drive(36);// get in the middle of the foundations

                        angle_turn(90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(90);// turn

                        drive(12);// park lower

                    }

                    if (block_num == "3") {

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn left

                        drive(36);// drive toward bridge

                        angle_turn(90);// turn right

                        drive(24);// get in the middle of the foundations

                        angle_turn(90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(90);// turn

                        drive(12);// park lower

                    }

                    if (block_num == "4") {

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn left

                        drive(48);// drive toward bridge

                        angle_turn(90);// turn right

                        drive(24);// get in the middle of the foundations

                        angle_turn(90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(-90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(90);// turn

                        drive(12);// park lower

                    }

                }// end of parking down

            }// end of if blue

            // BLUE ------------------------------------------------------------------------------------------

            // Red -------------------------------------------------------------------------------------------

            // we are blue
            if (red_or_blue == "Red") {

                // we want to park up
                if (up_or_down == "Up"){

                    if (block_num == "1") {

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn left

                        drive(12);// drive toward bridge

                        angle_turn(90);// turn right

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24);// drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                    }

                    if (block_num == "2") {

                        drive(12);// drive toward foundation

                        angle_turn(90);// turn toward build site wall

                        drive(6);// drive toward build site wall

                        angle_turn(-90);// turn to face opposite team

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                    }

                    if (block_num == "3") {

                        drive(24);// drive toward foundations

                        angle_turn(90);// turn

                        drive(36);// drive toward bridge

                        angle_turn(-90);// turn

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                    }

                    if (block_num == "4") {

                        drive(24);// drive toward foundations

                        angle_turn(90);// turn

                        drive(48);// drive toward bridge

                        angle_turn(-90);// turn

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                    }

                }// end of parking up

                // we want to park down
                if (up_or_down == "Down"){

                    if (block_num == "1") {

                        drive(24);// drive toward foundations

                        angle_turn(-90);// turn left

                        drive(12);// drive toward bridge

                        angle_turn(90);// turn right

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24);// drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(-90);// turn

                        drive(12);// park lower

                    }

                    if (block_num == "2") {

                        drive(12);// drive toward foundation

                        angle_turn(90);// turn toward build site wall

                        drive(6);// drive toward build site wall

                        angle_turn(-90);// turn to face opposite team

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(-90);// turn

                        drive(12);// park lower

                    }

                    if (block_num == "3") {

                        drive(24);// drive toward foundations

                        angle_turn(90);// turn

                        drive(36);// drive toward bridge

                        angle_turn(-90);// turn

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(-90);// turn

                        drive(12);// park lower

                    }

                    if (block_num == "4") {

                        drive(24);// drive toward foundations

                        angle_turn(90);// turn

                        drive(48);// drive toward bridge

                        angle_turn(-90);// turn

                        drive(24);// get in the middle of the foundations

                        angle_turn(-90);// turn around so point is facing foundations

                        drive(-24); // drive backwards to push the foundations apart

                        angle_turn(-90);// turn to face the foundations

                        drive(30);// push the foundation toward build site

                        drive(-3);// back off a little

                        angle_turn(90);// turn to face the bridge

                        drive(50);// park

                        angle_turn(-90);// turn

                        drive(12);// park lower

                    }

                }// end of parking down

            }// end of if red

            // Red -------------------------------------------------------------------------------------------

        }// end of moving foundation code

        telemetry.addLine("Have a good day user");// thanks the user
        telemetry.update();

        // For testing
        // */

    }// end of runOpMode

}// end of public class