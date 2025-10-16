package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.util.List;

public class SessionData {
    String timestamp;
    String mode;
    List<Pose2D> path;

    public SessionData(String timestamp, String mode, List<Pose2D> path) {
        this.timestamp = timestamp;
        this.mode = mode;
        this.path = path;
    }
}