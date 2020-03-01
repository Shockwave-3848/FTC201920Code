package org.firstinspires.ftc.teamcode.Code;

//imports
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//imports

@Autonomous(name="Get a Block", group="Shockwave")
@Disabled

//name of class
public class getBlock extends LinearOpMode {

    // Version number
    static double version_number = 6.0;

    // Variables --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    GyroTurn internalGyro;

    ElapsedTime runtime = new ElapsedTime();

    // Defining the wheels there motors
    DcMotor left_motor;
    DcMotor right_motor;
    DcMotor right_hand;
    DcMotor left_hand;
    DcMotor lift_motor;

    // Wheel stats
    static final int motor_revcount = 28; //28 ticks per rotation for a 15:1 motor //4 ticks at output for core hex motor (72:1)
    static final int lift_motor_revcount = 2240;
    static final int gear_ratio = 15;
    static final double wheel_diameter = 3.54;
    static final double counts_per_inch = (motor_revcount * gear_ratio) / (wheel_diameter * Math.PI);
    static final double lift_counts_per_inch = (motor_revcount * gear_ratio) / (1.77 * Math.PI);

    // Speed
    static final double basic_speed = 0.20;

    int desiredDistance = 10;






    boolean firstBlock;
    boolean thirdBlock;

    public void moveClaw(int inches, double power){
        int tolerance = 5;
        int new_rh_target;
        int new_lh_target;

        right_hand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_hand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // flw target
        new_lh_target = left_hand.getCurrentPosition() + (int)((inches) * counts_per_inch);
        left_hand.setTargetPosition(new_lh_target);

        // frw target
        new_rh_target = right_hand.getCurrentPosition() + (int)((inches) * counts_per_inch);
        right_hand.setTargetPosition(new_rh_target);

        // sets power
        right_hand.setPower(power);
        left_hand.setPower(power);

        // Turn On RUN_TO_POSITION
        left_hand.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_hand.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        runtime.reset();
        while (opModeIsActive() &&
                !(right_hand.getCurrentPosition() > new_rh_target - tolerance && right_hand.getCurrentPosition() < new_rh_target + tolerance) &&
                !(left_hand.getCurrentPosition() > new_lh_target - tolerance && left_hand.getCurrentPosition() < new_lh_target + tolerance)
                && runtime.seconds() < 3){}

        // Turn Off RUN_TO_POSITION
        left_hand.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right_hand.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_hand.setPower(0.0);
        right_hand.setPower(0.0);

    }

    public void drive(int inches, double power){

        // target
        int new_rw_target; //right wheel
        int new_lw_target; //left wheel
        // int new_test_target; //use for testing only

        // drive tends to undershoot the desired inches so the value bellow will be added to the goal destinations
        int extra_inches = 0;

        int tolerance = 1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // reset encoder
            left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //test_motor.setMode(DcMotor.RunMode.RESET_ENCODERS);

            // flw target
            new_lw_target = left_motor.getCurrentPosition() + (int)((inches + extra_inches) * counts_per_inch);// gives wheels the motor can use
            left_motor.setTargetPosition(new_lw_target);

            // frw target
            new_rw_target = right_motor.getCurrentPosition() + (int)((inches + extra_inches) * counts_per_inch);// gives wheels the motor can use
            right_motor.setTargetPosition(new_rw_target);

            // sets power
            right_motor.setPower(power);
            left_motor.setPower(power);

            // Turn On RUN_TO_POSITION
            left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //test_motor.setMode(DcMotor.RunMode.RESET_ENCODERS);

            //makes sure the robot is not broken
            runtime.reset();
            while (opModeIsActive() &&
                    !(right_motor.getCurrentPosition() > new_rw_target - tolerance && right_motor.getCurrentPosition() < new_rw_target + tolerance) &&
                    !(left_motor.getCurrentPosition() > new_lw_target - tolerance && left_motor.getCurrentPosition() < new_lw_target + tolerance)
                    && runtime.seconds() < 3) {

                telemetry.addLine("Am I working?");
                telemetry.addData("Speed", power);
                telemetry.addData("new_lw_target", new_lw_target);
                telemetry.addData("new_rw_target", new_rw_target);
                telemetry.addData("right wheel currently", right_motor.getCurrentPosition());
                telemetry.addData("left wheel currently", left_motor.getCurrentPosition());
                telemetry.update();

            }//opmode check 2

            // Turn Off RUN_TO_POSITION
            left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            left_motor.setPower(0.0);
            right_motor.setPower(0.0);

        }//opmode check 1

    }//end of drive method

    public void driveGyro(int inches, double power){

        // target
        int new_rw_target; //right wheel
        int new_lw_target; //left wheel
        // int new_test_target; //use for testing only

        // drive tends to undershoot the desired inches so the value bellow will be added to the goal destinations
        int extra_inches = 0;

        int tolerance = 1;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // reset encoder
            left_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            right_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //test_motor.setMode(DcMotor.RunMode.RESET_ENCODERS);

            // flw target
            new_lw_target = left_motor.getCurrentPosition() + (int)((inches + extra_inches) * counts_per_inch);// gives wheels the motor can use
            left_motor.setTargetPosition(new_lw_target);

            // frw target
            new_rw_target = right_motor.getCurrentPosition() + (int)((inches + extra_inches) * counts_per_inch);// gives wheels the motor can use
            right_motor.setTargetPosition(new_rw_target);

            // sets power
            right_motor.setPower(power);
            left_motor.setPower(power);

            // Turn On RUN_TO_POSITION
            left_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //test_motor.setMode(DcMotor.RunMode.RESET_ENCODERS);

            //makes sure the robot is not broken
            runtime.reset();
            while (opModeIsActive() &&
                    !(right_motor.getCurrentPosition() > new_rw_target - tolerance && right_motor.getCurrentPosition() < new_rw_target + tolerance) &&
                    !(left_motor.getCurrentPosition() > new_lw_target - tolerance && left_motor.getCurrentPosition() < new_lw_target + tolerance)
                    && runtime.seconds() < 3) {

                telemetry.addLine("Am I working?");
                telemetry.addData("Speed", power);
                telemetry.addData("new_lw_target", new_lw_target);
                telemetry.addData("new_rw_target", new_rw_target);
                telemetry.addData("right wheel currently", right_motor.getCurrentPosition());
                telemetry.addData("left wheel currently", left_motor.getCurrentPosition());
                telemetry.update();

            }//opmode check 2

            // Turn Off RUN_TO_POSITION
            left_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            right_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            left_motor.setPower(0.0);
            right_motor.setPower(0.0);

        }//opmode check 1

    }//end of drive method

    // arm movement
    public void arm_movement(int inches, double power){

        if (power > 0){

            lift_motor.setPower(0.1);

        }// trickle power

        if (power < 0){

            lift_motor.setPower(0.0);

        }// turn of trickle power

        // target for lift
        int new_arm_target;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            //reset encoder
            lift_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            // arm target
            new_arm_target = lift_motor.getCurrentPosition() + (int)(inches * lift_counts_per_inch);// gives wheels the motor can use
            lift_motor.setTargetPosition(new_arm_target);

            // sets power
            lift_motor.setPower(power);

            // Turn On RUN_TO_POSITION
            lift_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //makes sure the robot is not broken
            runtime.reset();
            while (opModeIsActive() && (lift_motor.isBusy()) && runtime.seconds() < 1) {

                telemetry.addLine("Am I working?");
                telemetry.addData("Speed", power);
                telemetry.addData("new_lw_target", new_arm_target);
                telemetry.addData("lift motor currently", lift_motor.getCurrentPosition());
                telemetry.update();

            }//opmode check 2

        }// end of opModeIsActive

    }// end of arm movement


    public void angle_turn(double angle_I_want) {
        internalGyro.turnDegrees(angle_I_want, left_motor, right_motor, telemetry);
    }//end of find angle

    @Override
    public void runOpMode() {
        left_motor = hardwareMap.dcMotor.get("left_motor");
        right_motor = hardwareMap.dcMotor.get("right_motor");
        lift_motor = hardwareMap.dcMotor.get("lift_motor");

        right_hand = hardwareMap.dcMotor.get("right_hand");
        left_hand = hardwareMap.dcMotor.get("left_hand");



        // make the motors go the same way
        right_motor.setDirection(DcMotor.Direction.FORWARD);
        left_motor.setDirection(DcMotor.Direction.REVERSE);

        lift_motor.setDirection(DcMotor.Direction.REVERSE);

        left_hand.setDirection(DcMotor.Direction.REVERSE);

        // if wheels get zero power what will it do?
        right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        SensorREVColor ColorSensor = new SensorREVColor(hardwareMap);

        waitForStart();// wait for the user to press start


        moveClaw(5, 1);


        drive(20, 0.6); // drive to block

        arm_movement(10, 1); // Lift arm out of the way

        drive(10, 0.2); // get very close

        while(!(ColorSensor.getDistanceOne() < desiredDistance)){
            left_motor.setPower(0.1);
            right_motor.setPower(0.1);
        }

        left_motor.setPower(0);
        right_motor.setPower(0);

        firstBlock = ColorSensor.isYellow(true);

        thirdBlock = ColorSensor.isYellow(false);


        //telemetry.addData("firstBlock", firstBlock);
        //telemetry.addData("thirdBlock", thirdBlock);


        if (firstBlock){
            if (thirdBlock){
                telemetry.addLine("2st block");
            } else {
                telemetry.addLine("3rd block");
            }
        } else {
            if (thirdBlock){
                telemetry.addLine("1st block");
            } else {
                double oneOffset = Math.abs(ColorSensor.getDistanceOne() - desiredDistance);
                double twoOffset = Math.abs(ColorSensor.getDistanceTwo() - desiredDistance);

                if (oneOffset <  twoOffset){
                    telemetry.addLine("1st block");
                } else {
                    telemetry.addLine("3st block");
                }
            }
        }

        telemetry.update();

        while(true){}
    }
}
