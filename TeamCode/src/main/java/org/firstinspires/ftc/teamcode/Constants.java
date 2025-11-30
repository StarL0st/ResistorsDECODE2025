package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class Constants {

    public static Pose2D START_FAR_SIDE = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);
    public static Pose2D START_CLOSE_SIDE = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);

    public enum AUTO_START_POS {
        FAR_SIDE,
        CLOSE_SIDE
    }
}
