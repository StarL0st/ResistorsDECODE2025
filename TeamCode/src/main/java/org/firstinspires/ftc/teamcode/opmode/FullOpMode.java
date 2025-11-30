package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.Motor;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptTelemetry;
import org.firstinspires.ftc.teamcode.command.DriveCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.tuning.PedroConstants;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WebcamSubsystem;

@TeleOp(name = "Full TeleOp", group = "TeleOp")
public class FullOpMode extends CommandOpMode {


    private GamepadEx driverOp, turretOp;

    //subsystems
    private MecanumDriveSubsystem m_drive;
    private IntakeSubsystem m_intake;
    private SorterSubsystem m_sorter;
    private WebcamSubsystem m_webcam;

    private DriveCommand driveCommand;



    @Override
    public void initialize() {
        this.driverOp = new GamepadEx(gamepad1);
        this.m_drive = new MecanumDriveSubsystem(
                hardwareMap,
                telemetry
        );
        //super.reset();

        this.m_intake = new IntakeSubsystem(hardwareMap, telemetry);
        this.m_sorter = new SorterSubsystem(hardwareMap, telemetry);
        //this.m_webcam = new WebcamSubsystem(hardwareMap, telemetry);

        this.driveCommand = new DriveCommand(m_drive,
                driverOp::getLeftX,
                driverOp::getLeftY,
                driverOp::getRightX);

        driverOp.getGamepadButton(GamepadKeys.Button.A)
                        .whenHeld(new InstantCommand(m_intake::runSimple, m_intake))
                                .whenReleased(new InstantCommand(m_intake::stop, m_intake));

        driverOp.getGamepadButton(GamepadKeys.Button.X)
                        .whenHeld(new InstantCommand(m_sorter::runSimple, m_sorter))
                                .whenReleased(new InstantCommand(m_sorter::stop, m_sorter));

        register(this.m_drive, this.m_webcam, this.m_intake);
        this.m_drive.setDefaultCommand(this.driveCommand);
    }
}
