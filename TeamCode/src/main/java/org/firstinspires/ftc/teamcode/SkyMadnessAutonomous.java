package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class SkyMadnessAutonomous extends LinearOpMode {

    private DcMotor right_drive;
    private DcMotor left_drive;
    private Servo servo;

    static final double SERVO_POS_OPEN = 0;
    static final double SERVO_POS_CLOSE_NUT = 0.20;

    static final double MOTOR_FORWARD = 0.6;
    static final double MOTOR_REVERSE = -0.6;


    @Override
    public void runOpMode() throws InterruptedException {

        right_drive = hardwareMap.get(DcMotor.class, "front_right_drive");
        left_drive = hardwareMap.get(DcMotor.class, "front_left_drive");

        servo = hardwareMap.get(Servo.class, "servo");

        left_drive.setDirection(DcMotorSimple.Direction.REVERSE);

        if(opModeInInit()) {
            servo.setPosition(SERVO_POS_OPEN);
            sleep(2000);
            servo.setPosition(SERVO_POS_CLOSE_NUT);
        }

        telemetry.addData("Gripper Status", "Initialized");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        if(opModeIsActive()) {
            right_drive.setPower(MOTOR_FORWARD);
            left_drive.setPower(MOTOR_FORWARD + 0.1);
            sleep(2300);
            right_drive.setPower(MOTOR_REVERSE);
            left_drive.setPower(MOTOR_FORWARD);
            sleep(253);
            right_drive.setPower(MOTOR_FORWARD);
            left_drive.setPower(MOTOR_FORWARD + 0.1);
            sleep(680);

            right_drive.setPower(0);
            left_drive.setPower(0);
            sleep(400);

            servo.setPosition(0);
            sleep(500);

            right_drive.setPower(MOTOR_REVERSE);
            left_drive.setPower(MOTOR_REVERSE - 0.1);
            sleep(700);

            right_drive.setPower(MOTOR_FORWARD);
            left_drive.setPower(MOTOR_REVERSE - 0.1);
            sleep(240);

            right_drive.setPower(MOTOR_REVERSE);
            left_drive.setPower(MOTOR_REVERSE);
            sleep(980);



            //right_drive.setPower(MOTOR_FORWARD);
            //left_drive.setPower(MOTOR_REVERSE - 0.1);
            //sleep(420);

            //right_drive.setPower((MOTOR_REVERSE + 0.2));
            //left_drive.setPower((MOTOR_REVERSE + 0.2) - 0.1);
            //sleep(1200);
        }

    }
}
