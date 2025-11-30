package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.RevIMU;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class MecanumDriveSubsystem extends SubsystemBase {

    private Follower follower;
    private Telemetry telemetry;

    private IMU imu;

    private Motor left_back_drive;
    private Motor left_front_drive;
    private Motor right_back_drive;
    private Motor right_front_drive;

    public MecanumDriveSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        //follower = PedroConstants.createFollower(hardwareMap);
        this.telemetry = telemetry;

        this.left_back_drive = new Motor(hardwareMap, "leftBackDrive");
        this.left_front_drive = new Motor(hardwareMap, "leftFrontDrive");
        this.right_back_drive = new Motor(hardwareMap, "rightBackDrive");
        this.right_front_drive = new Motor(hardwareMap, "rightFrontDrive");

        this.left_back_drive.setInverted(true);
        this.left_front_drive.setInverted(true);

        this.imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        this.imu.initialize(new IMU.Parameters(revOrientation));
    }

    //TODO: Replace with pedropathing localizer pose location
    public double getHeading(AngleUnit unit) {
        return this.imu.getRobotYawPitchRollAngles().getYaw(unit);
    }

    /*
    Resets the IMU heading to 0;
     */
    public void resetIMU() {
        this.imu.resetYaw();
    }

    @Override
    public void periodic() {
        telemetry.addData("Heading", getHeading(AngleUnit.DEGREES));
        //telemetry.update();
    }

    public void driveWithController(double strafeSpeed, double forwardSpeed, double turnSpeed) {
        double y =  forwardSpeed;
        double x = strafeSpeed;
        double rx = turnSpeed;

        double frontLeftPower = y + x + rx;
        double backLeftPower = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower = y + x - rx;

        double maxPower = Math.max(1.0, Math.max(Math.abs(frontLeftPower),
                Math.max(Math.abs(backLeftPower),
                        Math.max(Math.abs(frontRightPower), Math.abs(backRightPower)))));

        left_front_drive.set(frontLeftPower / maxPower);
        left_back_drive.set(backLeftPower / maxPower);
        right_front_drive.set(frontRightPower / maxPower);
        right_back_drive.set(backRightPower / maxPower);
    }

    //TODO: Pedro Pathing & Odometry
    public void driveTo() {

    }

    public Motor getLeftBackDrive() {
        return left_back_drive;
    }

    public Motor getLeftFrontDrive() {
        return left_front_drive;
    }

    public Motor getRightBackDrive() {
        return right_back_drive;
    }

    public Motor getRightFrontDrive() {
        return right_front_drive;
    }
}
