package org.firstinspires.ftc.teamcode.Code;

//imports

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

//imports

//name of the program
@Autonomous(name= "Nates_Vuforia_Test_Program", group ="Shockwave")

//can this code be pushed to a phone?
@Disabled

//main class
public class Nates_Vuforia_Test_Program extends LinearOpMode {

    //what camera do we want to use? The front of the or the back
    public static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;

    //is the phone in portrait or in landscape
    public static final boolean PHONE_IS_PORTRAIT = false;

    //the vuforia key
    private static final String VUFORIA_KEY = "ASB+lMn/////AAABmSELZ/IC5U5BklpoBPC3MN4Bnga4Xi/C52uy24x/iu+1isv802qak3Cz7DOfClTCJ0WCslWLoV5ClMI7bZ0r9g6woGi76wVh5QT5qCojPJAWQCI/mWexTqyZRHLEsB9i6bVvI6aP2fAOCOy5W2+MkXZ8ATQQXmpCkiCNIM7uijcc1KUbnInxxZwKC249md7EOb8jgY/I0zflVOeMj2wi/SS/V98KfZcDbis0DeMBhOtG3YiNwr9Ak5Zo5rutC5V8gCiJTRQm9acUmHoj1h18klkoRu4yt97kJnoemgnbrbzWiPJZ1iWwGNcFruZSvtCxApi4vMk+H//LZztLJnnpXFejWjVaJL+s437tIFp8w/aV";

    //Since ImageTarget trackables use mm to specifiy their dimensions, please use mm for all the physical dimension.

    //constants; all of these numbers are given by the sample code

    private static final float mm_per_inch = 25.4f; //the number was given from the sample code

    //height of the images
    private static final float height = 6;
    private static final float mm_target_height   = (height) * mm_per_inch;  // "the height of the center of the target image above the floor" - sample code

    // Constant for Stone Targets
    private static final float stoneZ_number = 2.00f;
    private static final float stoneZ = stoneZ_number * mm_per_inch;

    //bridges
    private static final float bridgeZ_number = 6.42f;
    private static final float bridgeZ = bridgeZ_number * mm_per_inch;

    private static final float stoneY_number = 23;
    private static final float stoneY = stoneZ_number * mm_per_inch;

    private static final float stoneX_number = 5.18f;
    private static final float stoneX = stoneZ_number * mm_per_inch;
    //bridges

    //used for angles
    private static final float bridgeRotY = 59;
    private static final float bridgeRotZ = 180;

    //Constants for perimeter targets
    private static final float halfField_number = 72;
    private static final float halfField = halfField_number * mm_per_inch;
    private static final float quadField_number = 36;
    private static final float quadField = quadField_number * mm_per_inch;

    //Class Members
    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;
    private boolean targetVisible = false;
    private float phoneXRotate    = 0;
    private float phoneYRotate    = 0;
    private float phoneZRotate    = 0;

    //constants; all of these numbers are given by the sample code

    @Override
    public void runOpMode() {

        //creates a Vuforia object that allows Vuforia to handle a camera preview resource
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        //grants accsess to to Vuforia
        parameters.vuforiaLicenseKey = VUFORIA_KEY;

        //sets the camera orientation to the one set in the CAMERA_CHOICE variable
        parameters.cameraDirection = CAMERA_CHOICE;

    }//end of runOpMode


}//end of Nates_Vuforia_Test_Program
