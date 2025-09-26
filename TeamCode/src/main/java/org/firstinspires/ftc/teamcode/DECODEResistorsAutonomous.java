package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous
public class DECODEResistorsAutonomous extends LinearOpMode {

    private DcMotor right_drive;
    private DcMotor left_drive;

    @Override
    public void runOpMode() throws InterruptedException {
        right_drive = hardwareMap.get(DcMotor.class, "front_right_drive");
        left_drive = hardwareMap.get(DcMotor.class, "front_left_drive");

        left_drive.setDirection(DcMotorSimple.Direction.REVERSE);


        telemetry.addData("Gripper Status", "Initialized");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        if(opModeIsActive()) {

        }

    }
}
