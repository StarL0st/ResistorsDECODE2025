package org.firstinspires.ftc.teamcode.command;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;

import java.util.function.DoubleSupplier;

public class DriveCommand extends CommandBase {

    private final MecanumDriveSubsystem m_drive;
    private final DoubleSupplier m_strafe;
    private final DoubleSupplier m_forward;
    private final DoubleSupplier m_rotation;

    public DriveCommand(MecanumDriveSubsystem mDrive,
                        DoubleSupplier mStrafe, DoubleSupplier mForward, DoubleSupplier mRotation) {
        m_drive = mDrive;
        m_strafe = mStrafe;
        m_forward = mForward;
        m_rotation = mRotation;
        addRequirements(m_drive);
    }

    @Override
    public void execute() {
        m_drive.driveWithController(m_strafe.getAsDouble(), m_forward.getAsDouble(), m_rotation.getAsDouble());
    }
}
