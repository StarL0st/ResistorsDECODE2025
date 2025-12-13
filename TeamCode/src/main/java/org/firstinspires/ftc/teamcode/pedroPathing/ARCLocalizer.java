package org.firstinspires.ftc.teamcode.pedroPathing;

import android.util.Size;

import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector;
import com.pedropathing.util.NanoTimer;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.RobotState;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class ARCLocalizer implements Localizer {

    private Limelight3A limelight;
    private IMU imu;
    //private GoBildaPinpointDriver odo;

    private Pose startPose;
    private Pose currentPose;
    private Pose lastPose;
    private Pose currentVelocity;

    //total
    private NanoTimer timer;
    private long deltaTimeNano;

    private double totalHeading;

    //AprilTag tracking for Turret
    private double tagTx;
    private double tagTy;
    private double tagTa;

    public ARCLocalizer(HardwareMap map, ARCLocalizerConstants arcLocalizerConstants) {
        this(map, arcLocalizerConstants, new Pose());
    }

    public ARCLocalizer(HardwareMap map, ARCLocalizerConstants arcLocalizerConstants, Pose startPose) {
        this.imu = map.get(IMU.class, arcLocalizerConstants.imuName);
        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        this.imu.initialize(new IMU.Parameters(revOrientation));

        this.limelight = map.get(Limelight3A.class, arcLocalizerConstants.llName);
        this.limelight.setPollRateHz(100);
        this.limelight.start();

        //if(RobotState.INSTANCE.get)

        setStartPose(startPose);
        totalHeading = 0;
        currentPose = startPose;
        currentVelocity = new Pose();

    }

    @Override
    public Pose getPose() {
        return this.currentPose;
    }

    @Override
    public Pose getVelocity() {
        return this.currentVelocity;
    }

    @Override
    public Vector getVelocityVector() {
        return this.currentVelocity.getAsVector();
    }

    @Override
    public void setStartPose(Pose setStart) {
        this.startPose = setStart;
    }

    @Override
    public void setPose(Pose setPose) {
        this.lastPose = this.currentPose;
        this.currentPose = setPose;
    }

    @Override
    public void update() {
        //update limelight tracking & MegaTag 2 (IMU), pinpoint updating too.
        //this.imu.getRobotYawPitchRollAngles();
    }

    @Override
    public double getTotalHeading() {
        return 0;
    }

    @Override
    public double getForwardMultiplier() {
        return 0;
    }

    @Override
    public double getLateralMultiplier() {
        return 0;
    }

    @Override
    public double getTurningMultiplier() {
        return 0;
    }

    @Override
    public void resetIMU() throws InterruptedException {
        this.imu.resetYaw();
    }

    @Override
    public double getIMUHeading() {
        return this.imu.getRobotYawPitchRollAngles().getYaw();
    }

    @Override
    public boolean isNAN() {
        return false;
    }
}
