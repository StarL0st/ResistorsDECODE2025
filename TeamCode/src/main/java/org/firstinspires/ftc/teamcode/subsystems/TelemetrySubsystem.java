package org.firstinspires.ftc.teamcode.subsystems;

import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TelemetrySubsystem extends SubsystemBase {
    private Telemetry telemetry;

    public TelemetrySubsystem(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void periodic() {
        this.telemetry.update();
    }
}
