package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.RobotState;

public class TurretSubsystem extends SubsystemBase {
    private Motor turretBaseMotor;
    private Motor turretFlywheelMotor;

    private ServoEx turretRampServo;
    private Limelight3A limelight;

    //tracking
    private LLResult currentResult;

    //telemetry
    private Telemetry telemetry;

    //limiting
    private static final int RIGHT_SIDE_TURRET_ENCODER_LIMIT = -240;
    private static final int LEFT_SIDE_TURRET_ENCODER_LIMIT = 240;

    private double lastPosCoeff = 0;
    private double lastPosTolerance = 0;

    public TurretSubsystem(HardwareMap hwMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        this.turretBaseMotor = new Motor(hwMap, "turretBaseMotor");
        this.turretBaseMotor.setRunMode(Motor.RunMode.PositionControl);

        this.turretBaseMotor.setPositionCoefficient(Constants.turretPositionCoefficient);
        this.turretBaseMotor.setPositionTolerance(Constants.turretPositionTolerance);

        this.turretBaseMotor.setTargetPosition(0);
        this.turretBaseMotor.set(0);
        //this.turretBaseMotor.setDistancePerPulse();

        this.lastPosCoeff = Constants.turretPositionCoefficient;
        this.lastPosTolerance = Constants.turretPositionTolerance;

        this.turretFlywheelMotor = new Motor(hwMap, "turretFlywheelMotor");

        this.turretRampServo = new ServoEx(hwMap, "turretRampServo");

        this.limelight = hwMap.get(Limelight3A.class, "limelight");
        this.limelight.setPollRateHz(100);
        this.limelight.start();

        this.limelight.pipelineSwitch(0); //default pipeline
    }

    @Override
    public void periodic() {
        this.limelight.updateRobotOrientation(RobotState.INSTANCE.getYaw());
        this.currentResult = this.limelight.getLatestResult();
        if(this.currentResult != null) {
            double staleness = this.currentResult.getStaleness();
            if(staleness < 100) {
                telemetry.addData("LL3A Data Last Update", staleness + "ms");
            }  else {
                telemetry.addData("LL3A Data", "Data is Old! " + staleness + "ms");
            }
        }
        if(this.currentResult.isValid()) {
            telemetry.addData("Tx", this.currentResult.getTx());
            telemetry.addData("Ty", this.currentResult.getTy());
        }


        telemetry.addData("turret pos", this.turretBaseMotor.getCurrentPosition());
        telemetry.addData("position coefficient", Constants.turretPositionCoefficient);
        telemetry.addData("turret tolerance", Constants.turretPositionTolerance);
        turretControlLoop();
    }

    public void resetEncoder() {
        this.turretBaseMotor.resetEncoder();
    }

    public void turretRestPosition() {
        this.turretBaseMotor.setTargetPosition(0);
    }

    public void limitTestRight() {
        this.turretBaseMotor.setTargetPosition(RIGHT_SIDE_TURRET_ENCODER_LIMIT);
    }

    public void limitTestLeft() {
        this.turretBaseMotor.setTargetPosition(LEFT_SIDE_TURRET_ENCODER_LIMIT);
    }

    public void driveWithPower(double power) {
        this.turretBaseMotor.setRunMode(Motor.RunMode.RawPower);
        this.turretBaseMotor.set(power * 0.2);
    }

    public void turretControlLoop() {
        if(Constants.resetTurretEncoder) {
            this.resetEncoder();
            Constants.resetTurretEncoder = false;
        }
        if(lastPosCoeff != Constants.turretPositionCoefficient) {
            this.lastPosCoeff = this.turretBaseMotor.getPositionCoefficient();
            this.turretBaseMotor.setPositionCoefficient(Constants.turretPositionCoefficient);
        }

        while (!this.turretBaseMotor.atTargetPosition()) {
            this.turretBaseMotor.set(0.4);
            this.telemetry.addData("Turret Status", "Going to position");
        }

        this.turretBaseMotor.stopMotor();
    }


    public void takeDebugSnapshot() {
        if(this.limelight.isRunning()) this.limelight.captureSnapshot("auto_pov_10s");
    }
}
