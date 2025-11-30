package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.SensorDistanceEx;
import com.seattlesolvers.solverslib.hardware.SensorRevColorV3;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;
import com.seattlesolvers.solverslib.util.LUT;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SorterSubsystem extends SubsystemBase {

    private DcMotor sorterSpindexerMotor;
    private ServoEx sorterOuttakeServo;

    private SensorRevColorV3 sorterColorSensor;
    private SensorDistanceEx sorterDistanceSensor;

    private Telemetry telemetry;

    private static final double COUNTS_PER_REV = 290;

    LUT<Integer, Integer> spindexerPositions = new LUT<Integer, Integer>() {{
        add(1, 25); // KEY = Set Position, OUT = Encoder Value
        add(2, 125);
        add(3, 223);
    }};


    public SorterSubsystem(HardwareMap map, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.sorterSpindexerMotor = map.get(DcMotor.class, "sorterSpindexerMotor");
        //this.sorterSpindexerMotor.setTargetPosition(spindexerPositions.get(1));
        this.sorterSpindexerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.sorterColorSensor = new SensorRevColorV3(map, "sorterColorSensor");
    }

    public void logEncoderPos() {
        this.telemetry.addData("Encoder Pos", this.sorterSpindexerMotor.getCurrentPosition());
        this.telemetry.update();
    }

    @Override
    public void periodic() {
        super.periodic();
    }

    public void runSimple() {
        this.sorterSpindexerMotor.setPower(0.5);
    }

    public void stop() {
        this.sorterSpindexerMotor.setPower(0);
    }
}
