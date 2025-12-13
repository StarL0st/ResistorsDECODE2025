package org.firstinspires.ftc.teamcode;

public class RobotState {
    public static final RobotState INSTANCE = new RobotState();

    public enum Mode {
        AUTONOMOUS,
        TELEOP,
        ENDGAME
    }

    private Mode robotMode;

    private double yaw = 0;

    public RobotState() {
        this.init();
    }

    public void init() {


    }

    public Mode getRobotMode() {
        return robotMode;
    }

    public void setRobotMode(Mode robotMode) {
        this.robotMode = robotMode;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }


}
