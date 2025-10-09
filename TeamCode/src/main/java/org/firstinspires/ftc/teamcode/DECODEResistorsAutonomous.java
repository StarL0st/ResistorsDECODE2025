package org.firstinspires.ftc.teamcode;

import com.google.gson.Gson;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

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

    private Pose2D[] path = new Pose2D[]{};

    @Override
    public void runOpMode() throws InterruptedException {
        right_drive = hardwareMap.get(DcMotor.class, "right_drive");
        left_drive = hardwareMap.get(DcMotor.class, "left_drive");

        right_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left_drive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        right_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left_drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        right_drive.setDirection(DcMotorSimple.Direction.FORWARD);
        left_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        initialHeading = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);

        telemetry.addData("Starting at",  "%7d :%7d",
                left_drive.getCurrentPosition(),
                right_drive.getCurrentPosition());

        telemetry.addData("Status", "Initialized");

        //load path


        waitForStart();
        telemetry.update();
        /*
        waitForStart();
        runtime.reset();
        telemetry.update();
        if(opModeIsActive()) {


            telemetry.update();
        }
        telemetry.update();
         */

        encoderDrive(DRIVE_SPEED, 400, 400, 2.0);
        encoderDrive(TURN_SPEED, -300, 300, 2.0);
        encoderDrive(DRIVE_SPEED, 700, 700, 2.0);
        encoderDrive(TURN_SPEED, 300, -300, 1.7);
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
    }

    public void encoderDrive(double speed,
                             double leftMM, double rightMM,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = left_drive.getCurrentPosition() + (int)(leftMM * COUNTS_PER_MM);
            newRightTarget = right_drive.getCurrentPosition() + (int)(rightMM * COUNTS_PER_MM);
            left_drive.setTargetPosition(newLeftTarget);
            right_drive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            left_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            right_drive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            left_drive.setPower(Math.abs(speed));
            right_drive.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
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
}
