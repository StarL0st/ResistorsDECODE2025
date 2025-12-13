package org.firstinspires.ftc.teamcode.selection;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;

public class ARCSelectorContext {
    public final GamepadEx driverOp;
    public final GamepadEx toolOp;

    public ARCSelectorContext(GamepadEx driverOp, GamepadEx toolOp) {
        this.driverOp = driverOp;
        this.toolOp = toolOp;
    }

    public boolean hasToolOp() {
        return this.toolOp != null;
    }
}
