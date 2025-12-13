package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.RevIMU;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.teamcode.command.DriveCommand;
import org.firstinspires.ftc.teamcode.selection.ARCSelectorContext;
import org.firstinspires.ftc.teamcode.selection.subsystem.CommandConfigurableSubsystem;

public class MecanumDriveSubsystem extends SubsystemBase implements CommandConfigurableSubsystem {
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

        this.left_back_drive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.left_front_drive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.right_back_drive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.right_front_drive.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        this.right_back_drive.setInverted(true);
        this.right_front_drive.setInverted(true);

        this.imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
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
        //RobotState.INSTANCE.setYaw(this.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Heading", this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));

        //telemetry.update();
    }

    public void driveWithController(double strafeSpeed, double forwardSpeed, double turnSpeed) {
        //follower.startTeleOpDrive();

        double y = -forwardSpeed;
        double x = -strafeSpeed;
        double rx = -turnSpeed;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        left_front_drive.set(frontLeftPower);
        left_back_drive.set(backLeftPower);
        right_front_drive.set(frontRightPower);
        right_back_drive.set(backRightPower);
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

    @Override
    public Command createDefaultCommand(ARCSelectorContext ctx) {
        return new DriveCommand(
                this,
                ctx.driverOp::getLeftX,
                ctx.driverOp::getLeftY,
                ctx.driverOp::getRightX
        );
    }

    @Override
    public void configureBindings(ARCSelectorContext ctx) {
        ctx.driverOp.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenReleased(new InstantCommand(this::resetIMU, this));
    }
}
