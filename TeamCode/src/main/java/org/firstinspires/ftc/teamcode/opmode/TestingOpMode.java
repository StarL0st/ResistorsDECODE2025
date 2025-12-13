package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.selection.ARCSelector;
import org.firstinspires.ftc.teamcode.selection.ARCSelectorContext;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumDriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.SorterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TelemetrySubsystem;
import org.firstinspires.ftc.teamcode.subsystems.TurretSubsystem;

import java.lang.reflect.InvocationTargetException;

@TeleOp(name = "TestingOpMode", group = "TeleOp")
public class TestingOpMode extends CommandOpMode {
    private GamepadEx driverOp;
    private GamepadEx toolOp;

    private TelemetrySubsystem m_telemetry;
    private ARCSelector selector;

    private boolean hasSelected = false;


    @Override
    public void initialize() {
        this.driverOp = new GamepadEx(gamepad1);
        this.toolOp = new GamepadEx(gamepad2);

        this.m_telemetry = new TelemetrySubsystem(telemetry);

        this.selector = new ARCSelector(hardwareMap, telemetry,
                IntakeSubsystem.class,
                SorterSubsystem.class,
                MecanumDriveSubsystem.class,
                TurretSubsystem.class
        );
    }

    @Override
    public void initialize_loop() {
        if(!this.hasSelected) {
            try {
                selector.handleInputs(gamepad1);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            selector.renderTelemetry();
            if(selector.isSelectionFinalized()) {
                selector.configureCommands(new ARCSelectorContext(driverOp, toolOp));
                this.hasSelected = true;
            }
        }
    }
}
