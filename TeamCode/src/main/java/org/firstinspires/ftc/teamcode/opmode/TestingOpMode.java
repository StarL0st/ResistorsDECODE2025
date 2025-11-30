package org.firstinspires.ftc.teamcode.opmode;

import com.pedropathing.telemetry.SelectableOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.WebcamSubsystem;

@TeleOp(name = "TestingOpMode", group = "TeleOp")
public class TestingOpMode extends CommandOpMode {

    private GamepadEx driverOp;

    private MecanumDriveSubsystem m_drive;
    private IntakeSubsystem m_intake;
    private SorterSubsystem m_sorter;
    private WebcamSubsystem m_webcam;



    @Override
    public void initialize() {
        this.driverOp = new GamepadEx(gamepad1);

        this.m_drive = new MecanumDriveSubsystem(
                hardwareMap,
                telemetry
        );



        this.m_intake = new IntakeSubsystem(hardwareMap, telemetry);
        this.m_sorter = new SorterSubsystem(hardwareMap, telemetry);
        this.m_webcam = new WebcamSubsystem(hardwareMap, telemetry);

        driverOp.getGamepadButton(GamepadKeys.Button.A)
                .whenHeld(new InstantCommand(m_intake::runSimple, m_intake))
                .whenReleased(new InstantCommand(m_intake::stop, m_intake));

        driverOp.getGamepadButton(GamepadKeys.Button.B)
                .whenHeld(new InstantCommand(m_sorter::runSimple, m_sorter))
                        .whenReleased(new InstantCommand(m_sorter::stop, m_sorter));

        driverOp.getGamepadButton(GamepadKeys.Button.X)
                .whenReleased(new InstantCommand(m_sorter::logEncoderPos, m_sorter));


        register(this.m_intake, this.m_webcam, this.m_sorter);
    }

    @Override
    public void run() {
        super.run();
    }
}
