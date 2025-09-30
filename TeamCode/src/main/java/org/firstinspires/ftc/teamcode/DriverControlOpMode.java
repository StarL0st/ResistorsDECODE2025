package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "DriverControlOpMode", group = "DECODE Resistors 2025")
public class DriverControlOpMode extends LinearOpMode {

    private DcMotor right_drive;
    private DcMotor left_drive;
    private DcMotor launcher_motor;

    private DcMotor launcher_starter;

    private CRServo launcher_servo;

    private boolean Fastmode= true;

    private boolean launcherMotorToggle = false;
    private boolean launcherStarterToggle = false;
    private boolean launcherServoToggle = false;

    @Override
    public void runOpMode() throws InterruptedException {
        right_drive = hardwareMap.get(DcMotor.class, "right_drive");
        left_drive = hardwareMap.get(DcMotor.class, "left_drive");
        launcher_motor = hardwareMap.get(DcMotor.class, "launcher_motor");
        launcher_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        launcher_starter = hardwareMap.get(DcMotor.class, "launcher_starter");

        launcher_servo = hardwareMap.get(CRServo.class, "launcher_servo");

        left_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Gripper Status", "Initialized");
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        while(opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            //launcher toggle
            if(gamepad2.a) {
                launcherMotorToggle = !launcherMotorToggle;
                telemetry.addData("Launcher Motor Status: ", launcherMotorToggle);
                telemetry.update();
                sleep(200);

                if(launcherMotorToggle) {
                    launcher_motor.setPower(0.7);
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
                    launcher_servo.setPower(10);
                    sleep(300);
                } else{
                    launcher_servo.setPower(0);
                    sleep(300);
                }
            }




            double speed = -gamepad1.right_stick_x;
            double turn = gamepad1.left_stick_y;

            double right = speed - turn;
            double left = speed + turn;

             if (gamepad1.right_bumper) {
                Fastmode = true;
            }
             if (gamepad1.left_bumper) {
                 Fastmode= false;
             }

            if(Fastmode){
                right_drive.setPower(right*1);
                left_drive.setPower(left*1);
            } else {
                right_drive.setPower(right*0.65);
                left_drive.setPower( left*0.65);
            }

        }

    }

}
