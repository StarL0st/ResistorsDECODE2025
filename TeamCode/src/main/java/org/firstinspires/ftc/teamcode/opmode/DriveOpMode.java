package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.RevIMU;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.teamcode.command.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

@TeleOp(name = "DriveOpMode", group = "TeleOp")
public class DriveOpMode extends CommandOpMode {

    private GamepadEx driverOp;

    //subsystems
    private MecanumDriveSubsystem mecanumDrive;
    //commands
    private DriveCommand driveCommand;

    @Override
    public void initialize() {
        this.driverOp = new GamepadEx(gamepad1);

        this.mecanumDrive = new MecanumDriveSubsystem(
                hardwareMap,
                telemetry
        );

        this.driveCommand = new DriveCommand(mecanumDrive,
                driverOp::getLeftX,
                driverOp::getLeftY,
                driverOp::getRightX);
        register(this.mecanumDrive);
        this.mecanumDrive.setDefaultCommand(this.driveCommand);
    }

}
