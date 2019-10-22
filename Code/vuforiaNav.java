package org.firstinspires.ftc.teamcode.Code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


@TeleOp(name="SKYSTONE Vuforia Nav", group ="Concept")
//@Disabled
public class vuforiaNav extends LinearOpMode {
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    double[] desiredLocation = {60.0f, 60.0f};
    double desiredYaw;
    @Override public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        GyroTurn internalGyro = new GyroTurn();

        ConceptVuforiaSkyStoneNavigation vuforiaNavication = new ConceptVuforiaSkyStoneNavigation();

        if (desiredLocation[0] > 0 && desiredLocation[1] > 0){
            desiredYaw = Math.atan(desiredLocation[1] / desiredLocation[0]);
        } else if (desiredLocation[0] < 0 && desiredLocation[1] > 0){
            desiredYaw = Math.atan((desiredLocation[1] * -1) / desiredLocation[0]) + 90;
        } else if (desiredLocation[0] < 0 && desiredLocation[1] < 0){
            desiredYaw = Math.atan((desiredLocation[1] * -1) / desiredLocation[0]) - 90;
        } else {
            desiredYaw = Math.atan(desiredLocation[1] / desiredLocation[0]);
        }


        // My code
        // currentXLocation
        // currentYLocation
        // currentVuforiaYaw
        // desiredLocation[0] = x
        // desiredLocation[1] = y
        // desiredYaw

        // internalGyro.getAngle(); update angle
        // internalGyro.globalAngle; the angle

        //    vuforiaNavication.currentXLocation;
        //    vuforiaNavication.currentYLocation;
        //    vuforiaNavication.currentVuforiaYaw;
        while (!isStopRequested()) {
            vuforiaNavication.updateVuforia();
            internalGyro.getAngle();
            telemetry.addData("currentXLocation: ", vuforiaNavication.currentXLocation);
            telemetry.addData("currentYLocation: ", vuforiaNavication.currentYLocation);
            telemetry.addData("currentVuforiaYaw: ", vuforiaNavication.currentVuforiaYaw);
            telemetry.update();
        }
    }

}
