package org.firstinspires.ftc.teamcode.Code.unused;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.lang.Math;



//@Disabled
// Class
public class Encoder {
    double                  maxTime = 2.5;
    double                  startPower;
    // for compitition robot
    final double            HD_MOTOR_TICK_COUNT = 30;

    double                  gearRatio = 15;
    double                  wheelDiamiter = 3.54;
    /*

    static final double            HD_MOTOR_TICK_COUNT = 4;

    static double                  gearRatio = 72;
    static double                  wheelDiamiter = 3.54;
*/
    int                     ticksPerInch;

    Encoder(){
        updateTickCount();
    }

    public void updateTickCount(){
        ticksPerInch = (int) ((HD_MOTOR_TICK_COUNT * gearRatio) / (wheelDiamiter * Math.PI));
    }

    public void driveNormal(double inchsToRole, DcMotor leftDrive, DcMotor rightDrive, int driveForwardLeft, int driveForwardRight) {
        ElapsedTime runtime = new ElapsedTime();
        startPower = 1.0;

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        int ticksToMove = (int) inchsToRole * ticksPerInch;
        leftDrive.setTargetPosition((ticksToMove + leftDrive.getCurrentPosition()) * driveForwardLeft);
        rightDrive.setTargetPosition((ticksToMove + rightDrive.getCurrentPosition()) * driveForwardRight);


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

    public void drivePID(double inchsToRole, DcMotor leftDrive, DcMotor rightDrive, int driveForwardLeft, int driveForwardRight) {
        ElapsedTime runtime = new ElapsedTime();
        startPower = 0.5;
        maxTime = 5;
        int tolerance = 5;

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        int ticksToMove = (int) inchsToRole * ticksPerInch;
        int halfsToMove = (int) Math.floor(ticksToMove/(HD_MOTOR_TICK_COUNT/2));

        for (int i = 0; i < halfsToMove + 1; i++) {

            if (i != halfsToMove){
                leftDrive.setTargetPosition((int)(HD_MOTOR_TICK_COUNT/2) + leftDrive.getCurrentPosition());
                rightDrive.setTargetPosition((int)(HD_MOTOR_TICK_COUNT/2) + rightDrive.getCurrentPosition());
            } else {
                leftDrive.setTargetPosition(ticksToMove - ((int)(HD_MOTOR_TICK_COUNT/2) * halfsToMove) + leftDrive.getCurrentPosition());
                rightDrive.setTargetPosition(ticksToMove - ((int)(HD_MOTOR_TICK_COUNT/2) * halfsToMove)+ rightDrive.getCurrentPosition() );
            }



            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            leftDrive.setPower(driveForwardLeft * startPower);
            rightDrive.setPower(driveForwardRight * startPower);

            runtime.reset();
            while (((leftDrive.getCurrentPosition() * driveForwardLeft) < (int)(HD_MOTOR_TICK_COUNT/2) + tolerance)
                    && ((rightDrive.getCurrentPosition() * driveForwardRight) < (int)(HD_MOTOR_TICK_COUNT/2) + tolerance)
                    && (runtime.seconds() < maxTime)) {
            }
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);

        // Turn off RUN_USING_ENCODER
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
