package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.SensorRevColorV3;
import com.seattlesolvers.solverslib.util.LUT;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem extends SubsystemBase {
    private Telemetry telemetry;
    private DcMotor intakeMotor;
    private SensorRevColorV3 colorSensor;

    public IntakeSubsystem(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.intakeMotor = hwMap.get(DcMotor.class, "intakeMotor");
        this.intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //this.colorSensor = new SensorRevColorV3(hwMap, "intakeColorSensor");
    }

    @Override
    public void periodic() {

    }


    public void runSimple() {
        this.intakeMotor.setPower(1);
    }

    public void stop() {
        this.intakeMotor.setPower(0);
    }

}
