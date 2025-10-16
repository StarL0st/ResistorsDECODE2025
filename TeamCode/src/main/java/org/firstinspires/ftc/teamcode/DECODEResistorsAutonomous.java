package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous
public class DECODEResistorsAutonomous extends LinearOpMode {

    private DcMotor right_drive;
    private DcMotor left_drive;

    private Pose2D initialHeading;

    private ElapsedTime runtime = new ElapsedTime();

    //constants
    static final double     COUNTS_PER_MOTOR_REV    = 28 ;    // HD Hex Motor
    static final double     DRIVE_GEAR_REDUCTION    = 20.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_MM   = 76.2 ;     // For figuring circumference
    static final double     COUNTS_PER_MM         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_MM * 3.1415);
    static final double     DRIVE_SPEED             = 1;
    static final double     TURN_SPEED              = 0.7;

    private static final File CONFIG_FILE = new File(Environment.getExternalStorageDirectory(), "FIRST/encoder_data_test/config.json");

    private ConfigData config;

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

        right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        left_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        right_drive.setDirection(DcMotorSimple.Direction.FORWARD);
        left_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        initialHeading = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);

        telemetry.addData("Starting at",  "%7d :%7d",
                left_drive.getCurrentPosition(),
                right_drive.getCurrentPosition());

        telemetry.addData("Status", "Initialized controls");

        //config
        loadConfig();

        //load path
        loadPath();

        telemetry.update();



        waitForStart();
        runtime.reset();
        telemetry.update();
        while(opModeIsActive()) {
            sleep(500);
            telemetry.addData("Path", "Starting to replay path: " + config.selectedPath);
            telemetry.update();
            //path replay
            for(Pose2D pose : this.path) {
                encoderDrive(1, -pose.getX(DistanceUnit.MM), -pose.getY(DistanceUnit.MM), 15.0);
                sleep(400);
            }


            telemetry.update();
        }
        telemetry.update();


        //encoderDrive(DRIVE_SPEED, 400, 400, 2.0);
        //encoderDrive(TURN_SPEED, -300, 300, 2.0);
        //encoderDrive(DRIVE_SPEED, 700, 700, 2.0);
        //encoderDrive(TURN_SPEED, 300, -300, 1.7);
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    public void loadPath() {
        File dir = new File(Environment.getExternalStorageDirectory(), "FIRST/encoder_data_test");
        File pathFile = new File(dir, config.selectedPath);

        try (FileReader reader = new FileReader(pathFile)) {
            SessionData session = GSON.fromJson(reader, SessionData.class);
            path = session.path;
            telemetry.addData("Path", "Loaded " + path.size() + " poses");
        } catch (Exception e) {
            telemetry.addData("Path", "Error loading: " + e.getMessage());
        }
    }

    public void encoderDrive(double speed,
                             double leftMM, double rightMM,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = (int) leftMM;
            newRightTarget = (int) rightMM;
            left_drive.setTargetPosition(newLeftTarget);
            right_drive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            left_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            left_drive.setPower(Math.abs(speed));
            right_drive.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (left_drive.isBusy() && right_drive.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        left_drive.getCurrentPosition(), right_drive.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            left_drive.setPower(0);
            right_drive.setPower(0);

            // Turn off RUN_TO_POSITION
            left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }

    //TODO: UX and UI details, better path selection and similar

    private void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            config = GSON.fromJson(reader, ConfigData.class);
            telemetry.addData("Config", "Loaded: " + config.selectedPath);
        } catch (Exception e) {
            telemetry.addData("Config", "Error loading: " + e.getMessage());
            // Default config if missing
            config = new ConfigData("encoder_log_default.json");
            saveConfig(); // auto-create it
        }
    }

    private void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE, false)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            telemetry.addData("Config", "Save error: " + e.getMessage());
        }
    }
}
