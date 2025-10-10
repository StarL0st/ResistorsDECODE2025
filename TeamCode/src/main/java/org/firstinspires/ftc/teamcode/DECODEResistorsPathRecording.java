package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "DECODEResistorsPathRecording", group = "DECODE Resistors 2025")
public class DECODEResistorsPathRecording extends LinearOpMode {
    private DcMotor right_drive;
    private DcMotor left_drive;

    private DcMotor launcher_motor;

    private DcMotor launcher_starter;

    private CRServo launcher_servo;

    private boolean FastMode = true;


    private boolean launcherMotorToggle = false;
    private boolean launcherStarterToggle = false;
    private boolean launcherServoToggle = false;

    private Pose2D initialPose;
    private Pose2D currentPose;
    private Pose2D lastPose;

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
    private boolean endedRecording = false;

    private List<Pose2D> path = new ArrayList<>();

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

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
            this.lastPose = this.currentPose;
            //TODO: Add IMU Heading;
            this.currentPose = new Pose2D(DistanceUnit.MM, left_drive.getCurrentPosition(), right_drive.getCurrentPosition(), AngleUnit.DEGREES, 0);
            if(gamepad1.x) {
                sleep(200);
                if(recordingActive) {
                    endedRecording = true;
                    recordingActive = false;
                    continue;
                }
                recordingActive = true;
                telemetry.addData("Status", "Starting Recording");
                telemetry.update();
                sleep(200);
            }
            if(recordingActive) {
                if((runtime.now(TimeUnit.SECONDS) - lastRecordedTime) >= recordingInterval) {
                    double dx = Math.abs(this.currentPose.getX(DistanceUnit.MM) - this.lastPose.getX(DistanceUnit.MM));
                    double dy = Math.abs(this.currentPose.getY(DistanceUnit.MM) - this.lastPose.getY(DistanceUnit.MM));

                    if (dx > 1.0 || dy > 1.0) {
                        updatePath(this.currentPose);
                        this.lastPose = this.currentPose;
                    }
                    telemetry.addData("Recording", "Added path data");
                    telemetry.update();
                    lastRecordedTime = runtime.now(TimeUnit.SECONDS);
                }

            } else if(endedRecording) {
                sleep(200);
                telemetry.addData("Status", "Ending Recording");
                telemetry.update();
                File dir = new File(Environment.getExternalStorageDirectory(), "FIRST/encoder_data_test");
                if (!dir.exists()) dir.mkdirs();

                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
                File file = new File(dir, "encoder_log_" + timestamp + ".json");

                SessionData sessionData = new SessionData(timestamp, "autonomous", this.path);

                try(FileWriter writer = new FileWriter(file, false)) {
                    GSON.toJson(sessionData, writer);
                } catch (Exception e) {
                    telemetry.addData("File Error", e.getMessage());
                }
                endedRecording = false;
            }

            if(gamepad2.a) {
                launcherMotorToggle = !launcherMotorToggle;
                telemetry.addData("Launcher Motor Status: ", launcherMotorToggle);
                telemetry.update();
                sleep(200);

                if(launcherMotorToggle) {
                    launcher_motor.setPower(0.6);
                    sleep(200);
                } else {
                    launcher_motor.setPower(0);
                    sleep(200);
                }
            }

            if(gamepad2.b){
                launcherStarterToggle = !launcherStarterToggle;
                telemetry.addData("Launcher Starter Status: ", launcherStarterToggle);
                telemetry.update();
                sleep(300);
                if(launcherStarterToggle) {
                    launcher_starter.setPower(1);
                    sleep(300);
                } else {
                    launcher_starter.setPower(0);
                    sleep(300);
                }
            }

            if(gamepad2.right_bumper) {
                launcherServoToggle = !launcherServoToggle;
                telemetry.addData("Launcher Servo Status: ", launcherServoToggle);
                telemetry.update();
                sleep(300);
                if(launcherServoToggle) {
                    launcher_servo.setPower(8);
                    sleep(300);
                } else{
                    launcher_servo.setPower(0);
                    sleep(300);
                }
            }

            double speed = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;

            double right = speed - turn;
            double left = speed + turn;

            if (gamepad1.right_bumper) {
                FastMode = true;
            }
            if (gamepad1.left_bumper) {
                FastMode = false;
            }

            if(FastMode){
                right_drive.setPower(right*1);
                left_drive.setPower(left*1);
            } else {
                right_drive.setPower(right*0.50);
                left_drive.setPower( left*0.50);
            }
            //update current pose
            telemetry.addData("Currently at",  " at %7d :%7d", (int) this.currentPose.getX(DistanceUnit.MM), (int) currentPose.getY(DistanceUnit.MM));
            telemetry.update();
        }
    }

    public void updatePath(Pose2D pose2D) {
        path.add(pose2D);
    }
}


