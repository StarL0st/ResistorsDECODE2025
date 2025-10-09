package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "DECODEResistorsPathRecording", group = "DECODE Resistors 2025")
public class DECODEResistorsPathRecording extends LinearOpMode {
    private DcMotor right_drive;
    private DcMotor left_drive;

    private Pose2D initialPose;
    private Pose2D currentPose;

    private ElapsedTime runtime = new ElapsedTime();


    //constants
    static final double     COUNTS_PER_MOTOR_REV    = 28 ;    // HD Hex Motor
    static final double     DRIVE_GEAR_REDUCTION    = 20.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_MM   = 76.2 ;     // For figuring circumference
    static final double     COUNTS_PER_MM         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_MM * 3.1415);
    static final double     DRIVE_SPEED             = 1;
    static final double     TURN_SPEED              = 0.5;

    //recording data
    private double recordingInterval = 1; //SECONDS
    private double lastRecordedTime;
    private boolean recordingActive = false;

    private List<Pose2D> path = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void runOpMode() throws InterruptedException {
        right_drive = hardwareMap.get(DcMotor.class, "right_drive");
        left_drive = hardwareMap.get(DcMotor.class, "left_drive");

        right_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        right_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left_drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right_drive.setDirection(DcMotorSimple.Direction.REVERSE);
        left_drive.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Ready to record");

        initialPose = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);
        this.currentPose = initialPose;
        updatePath(this.initialPose);




        //interval recording
        telemetry.update();
        waitForStart();
        runtime.reset();
        while(opModeIsActive()) {
            if(gamepad1.a) {
                recordingActive = !recordingActive;
                telemetry.addData("Status", "Starting Recording");
            }
            if(recordingActive) {
                if(recordingInterval >= (runtime.now(TimeUnit.SECONDS) - lastRecordedTime)) {
                    //TODO: Add IMU Heading;
                    this.currentPose = new Pose2D(DistanceUnit.MM, left_drive.getCurrentPosition(), right_drive.getCurrentPosition(), AngleUnit.DEGREES, 0);
                    File dir = new File(Environment.getExternalStorageDirectory(), "FIRST/encoder_data_test");
                    if (!dir.exists()) dir.mkdirs();

                    String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                    File file = new File(dir, "encoder_log_" + timestamp + ".json");
                    try(FileWriter writer = new FileWriter(file, true)) {
                        writer.write((int) this.currentPose.getX(DistanceUnit.MM) + "," + (int) this.currentPose.getY(DistanceUnit.MM) + "\n");
                        //TODO: GSON STORAGE
                    } catch (Exception e) {
                        telemetry.addData("File Error", e.getMessage());
                    }
                    updatePath(this.currentPose);
                }

            }
            double speed = -gamepad1.left_stick_x;
            double turn = gamepad1.left_stick_y;

            double right = speed - turn;
            double left = speed + turn;
            right_drive.setPower(right * 0.7);
            left_drive.setPower(left * 0.7);
            //update current pose
            //telemetry.addData("Currently at",  " at %7d :%7d", currentPose.getX(DistanceUnit.MM), currentPose.getY(DistanceUnit.MM));
            telemetry.update();
        }
    }

    public void updatePath(Pose2D pose2D) {
        path.add(pose2D);
    }
}


