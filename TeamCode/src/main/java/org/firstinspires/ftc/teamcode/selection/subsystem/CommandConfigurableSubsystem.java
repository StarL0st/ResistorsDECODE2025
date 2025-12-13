package org.firstinspires.ftc.teamcode.selection.subsystem;

import com.seattlesolvers.solverslib.command.Command;

import org.firstinspires.ftc.teamcode.selection.ARCSelectorContext;

public interface CommandConfigurableSubsystem {

    Command createDefaultCommand(ARCSelectorContext ctx);

    default void configureBindings(ARCSelectorContext ctx) {
        //no op
    }
}
