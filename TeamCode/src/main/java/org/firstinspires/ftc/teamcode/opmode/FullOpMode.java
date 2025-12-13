package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.teamcode.command.DriveCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.tuning.PedroConstants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TelemetrySubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

@TeleOp(name = "Full TeleOp", group = "TeleOp")
public class FullOpMode extends CommandOpMode {
    private Follower follower;

    private GamepadEx driverOp, turretOp;

    //subsystems
    private MecanumDriveSubsystem m_drive;
    private IntakeSubsystem m_intake;
    private SorterSubsystem m_sorter;
    private TurretSubsystem m_turret;

    private TelemetrySubsystem m_telemetry;


    private DriveCommand driveCommand;



    @Override
    public void initialize() {
        this.follower = PedroConstants.createFollower(hardwareMap);
        super.reset();

        this.driverOp = new GamepadEx(gamepad1);
        RobotState.INSTANCE.init();

        this.m_telemetry = new TelemetrySubsystem(this.telemetry);

        this.m_drive = new MecanumDriveSubsystem(
                hardwareMap,
                telemetry
        );
        //super.reset();

        //this.m_intake = new IntakeSubsystem(hardwareMap, telemetry);
        //this.m_sorter = new SorterSubsystem(hardwareMap, telemetry);
        this.m_turret = new TurretSubsystem(hardwareMap, telemetry);


        this.driveCommand = new DriveCommand(m_drive,
                driverOp::getLeftX,
                driverOp::getLeftY,
                driverOp::getRightX);

        //driverOp.getGamepadButton(GamepadKeys.Button.A)
        //                .whenHeld(new InstantCommand(m_intake::runSimple, m_intake))
        //                        .whenReleased(new InstantCommand(m_intake::stop, m_intake));

        driverOp.getGamepadButton(GamepadKeys.Button.X)
                        .whenHeld(new InstantCommand(m_sorter::runSimple, m_sorter))
                                .whenReleased(new InstantCommand(m_sorter::stop, m_sorter));

        driverOp.getGamepadButton(GamepadKeys.Button.OPTIONS)
                        .whenReleased(new InstantCommand(m_drive::resetIMU, m_drive));

        driverOp.getGamepadButton(GamepadKeys.Button.BACK)
                        .whenReleased(new InstantCommand(m_turret::takeDebugSnapshot, m_turret));

        driverOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER)
                .whenReleased(new InstantCommand(m_turret::limitTestLeft, m_turret));

        driverOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER)
                .whenReleased(new InstantCommand(m_turret::limitTestRight, m_turret));

        driverOp.getGamepadButton(GamepadKeys.Button.Y)
                        .whenReleased(new InstantCommand(m_turret::turretRestPosition, m_turret));

        register(this.m_drive, this.m_intake, this.m_sorter, this.m_turret, this.m_telemetry);
        this.m_drive.setDefaultCommand(this.driveCommand);
    }



    @Override
    public void end() {

    }
}
