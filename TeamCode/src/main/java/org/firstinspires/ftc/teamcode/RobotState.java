package org.firstinspires.ftc.teamcode;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;

public class RobotState {
    private GamepadEx driver;

    private final RobotState instance;

    public RobotState(RobotState instance) {
        this.instance = instance;

    }

    public RobotState getInstance() {
        return instance;
    }
}
