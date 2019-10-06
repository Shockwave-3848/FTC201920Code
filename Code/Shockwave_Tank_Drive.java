package org.firstinspires.ftc.teamcode.Code;

//imports
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.Servo;
//imports

@TeleOp(name = "Shockwave Tank Drive", group = "Shockwave")
@Disabled

public class Shockwave_Tank_Drive extends OpMode{

    //declaring the motors
    private DcMotor rightMotor;
    private DcMotor leftMotor;

    //declaring power variables
    private double rightMotorPower = 0;
    private double leftMotorPower = 0;

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
        telemetry.addData("Status", "Initialized");
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

        //controller input
        rightMotorPower = max_speed * gamepad1.right_stick_y + gamepad1.right_stick_x;
        leftMotorPower = max_speed * gamepad1.left_stick_y + gamepad1.left_stick_x;

       /*if(gamepad1.dpad_left) {
           servo.setPosition(0);
       }
       if(gamepad1.dpad_right) {
           servo.setPosition(1);
       }*/

        //sets power to controller input
        rightMotor.setPower(rightMotorPower);
        leftMotor.setPower(leftMotorPower);

        telemetry.update();
    }

    //what should the robot do when the program is supposed to stop
    @Override
    public void stop() {
        rightMotor.setPower(0);
        leftMotor.setPower(0);
    }

}//End of Shockwave_Tank_Drive class

