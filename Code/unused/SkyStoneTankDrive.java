package org.firstinspires.ftc.teamcode.Code.unused;

//imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//imports

@TeleOp(name = "ShockwaveTankDrive", group = "Shockwave")
@Disabled

public class SkyStoneTankDrive extends OpMode{

    //declaring the motors
    private DcMotor rightMotor;
    private DcMotor leftMotor;

    //declaring power variables
    private double right_motor_power = 0;
    private double left_motor_power = 0;

    //what should happen when the users presses the init button
    @Override
    public void init() {

        //sets the name the program looks for in the program
        rightMotor = hardwareMap.dcMotor.get("right_motor");
        leftMotor = hardwareMap.dcMotor.get("left_motor");

        //make the motor goes the same way as the left motor
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        //servo = hardwareMap.servo.get("servo");

        //shows a message to the user that the code is working
        telemetry.addLine("Status: Initialized");
        telemetry.update();

        //when zero power is flowing into the motors they should brake instead of being neutral
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //because all the code is done in the init() method this is redundant however we do not want to freak out the program so we keep this in
    @Override
    public void init_loop() {

    }

    //what should the program do when the code is active
    @Override
    public void loop() {

        //what is the max speed of the robot
        double max_speed = 1.0; // 0 < max_speed <= 1 : to change max speed of robot
        double max_speed_change = 0.1;

        //controller input --------------------------------------------------------------------------------------------
        right_motor_power = max_speed * gamepad1.right_stick_y + gamepad1.right_stick_x;
        left_motor_power = max_speed * gamepad1.left_stick_y + gamepad1.left_stick_x;

        //increase max speed
        if((gamepad1.dpad_up) && (max_speed < 1)) {
            max_speed += max_speed_change;
        }

        //increase max speed
        if((gamepad1.dpad_down) && (max_speed > 0)) {
            max_speed -= max_speed_change;
        }

        //sets power to controller input
        rightMotor.setPower(right_motor_power);
        leftMotor.setPower(left_motor_power);

        //informs drivers about the power to the rob
        telemetry.addData("Power to the right wheel: ", right_motor_power);
        telemetry.addData("Power to the left wheel: ", left_motor_power);
        telemetry.addData("Max Speed: ", max_speed);
        telemetry.update();

    }//end of loop

    //what should the robot do when the program is supposed to stop
    @Override
    public void stop() {
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }

}//End of Shockwave_Tank_Drive class


