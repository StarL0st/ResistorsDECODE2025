package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.command.DriveCommand;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TelemetrySubsystem;

@TeleOp(name = "DriveOpMode", group = "TeleOp")
public class DriveOpMode extends CommandOpMode {

    private GamepadEx driverOp;

    //subsystems
    private MecanumDriveSubsystem m_drive;
    private TelemetrySubsystem m_telemetry;
    //commands
    private DriveCommand driveCommand;

    @Override
    public void initialize() {
        this.driverOp = new GamepadEx(gamepad1);

        this.m_drive = new MecanumDriveSubsystem(
                hardwareMap,
                telemetry
        );
        this.m_telemetry = new TelemetrySubsystem(this.telemetry);

        this.driveCommand = new DriveCommand(m_drive,
                driverOp::getLeftX,
                driverOp::getLeftY,
                driverOp::getRightX);

        driverOp.getGamepadButton(GamepadKeys.Button.OPTIONS)
                .whenReleased(new InstantCommand(m_drive::resetIMU, m_drive));

        register(this.m_drive, this.m_telemetry);
        this.m_drive.setDefaultCommand(this.driveCommand);
    }

    @Override
    public void initialize_loop() {
        super.initialize_loop();
    }
}
