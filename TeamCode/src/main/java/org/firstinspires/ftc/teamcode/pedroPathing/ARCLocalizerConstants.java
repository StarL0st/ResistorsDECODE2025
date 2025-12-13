package org.firstinspires.ftc.teamcode.pedroPathing;

public class ARCLocalizerConstants {

    public String llName;
    public String imuName;


    public ARCLocalizerConstants limelightName(String name) {
        this.llName = name;
        return this;
    }

    public ARCLocalizerConstants imuName(String name) {
        this.imuName = name;
        return this;
    }

    public void defaults() {

    }
}
