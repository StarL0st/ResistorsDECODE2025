package org.firstinspires.ftc.teamcode.opmode;

import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

public class FullOpMode extends CommandOpMode {

    private GamepadEx driverOp, turretOp;

    //subsystems
    private MecanumDriveSubsystem m_drive;
    private IntakeSubsystem m_intake;


    @Override
    public void initialize() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

    }
}
