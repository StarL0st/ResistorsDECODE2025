package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.RevIMU;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

public class MecanumDriveSubsystem extends SubsystemBase {

    private MecanumDrive ftclibMecanumDrive;
    private GamepadEx driverOp;

    private Motor left_back_drive;
    private Motor left_front_drive;
    private Motor right_back_drive;
    private Motor right_front_drive;

    private RevIMU imu;

    static RevHubOrientationOnRobot.LogoFacingDirection[] logoFacingDirections
            = RevHubOrientationOnRobot.LogoFacingDirection.values();
    static RevHubOrientationOnRobot.UsbFacingDirection[] usbFacingDirections
            = RevHubOrientationOnRobot.UsbFacingDirection.values();

    public MecanumDriveSubsystem(Motor left_back_drive, Motor left_front_drive, Motor right_back_drive, Motor right_front_drive, GamepadEx driverOp, RevIMU imu) {
        this.left_back_drive = left_back_drive;
        this.left_front_drive = left_front_drive;
        this.right_back_drive = right_back_drive;
        this.right_front_drive = right_front_drive;
        ftclibMecanumDrive = new MecanumDrive(
                (Motor) left_front_drive,
                (Motor) right_front_drive,
                (Motor) left_back_drive,
                (Motor) right_back_drive
        );
        this.driverOp = driverOp;
        this.imu = imu;
    }

    @Override
    public void periodic() {

    }

    public void driveWithController(double strafeSpeed, double forwardSpeed, double turnSpeed) {
        ftclibMecanumDrive.driveRobotCentric(
                strafeSpeed,
                -forwardSpeed,
                -turnSpeed,
                false
        );
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
