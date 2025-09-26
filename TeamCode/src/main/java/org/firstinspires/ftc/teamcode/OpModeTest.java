package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "adkasjdoasjdoajsjdkasj", group = "Linear TeleOp")
public class OpModeTest extends LinearOpMode {

    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;


    @Override
    public void runOpMode() throws InterruptedException {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD); //NOTE: left Forward and right Reverse for forward motion, motors are flipped

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            leftDrive.setPower(0.8);
            rightDrive.setPower(0.8);
        }

    }
}
