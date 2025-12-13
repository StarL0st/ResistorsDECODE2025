package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Configurable
public class Constants {

    public static final Pose2D START_FAR_SIDE = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);
    public static final Pose2D START_CLOSE_SIDE = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0);

    public static double turretPositionCoefficient = 0.05;
    public static double turretPositionTolerance = 10;

    public static boolean resetTurretEncoder = false;

    public enum AUTO_START_POS {
        FAR_SIDE,
        CLOSE_SIDE
    }
}
