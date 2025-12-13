package org.firstinspires.ftc.teamcode.selection;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import com.seattlesolvers.solverslib.command.Subsystem;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.selection.subsystem.CommandConfigurableSubsystem;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ARCSelector<T> {
    private Telemetry telemetry;
    private HardwareMap hwMapTemp; //for instance creation
    private int currentIndex;
    private Class<T> selectedValueClass;

    private Map<Integer, T> selectedValuesInstances;
    private T selectedValueInstance;

    private Map<Integer, Class<T>> selectables;

    private HashMap<Integer, Class<T>> toggleSelections;
    private boolean toggleSelectionEnabled = false;
    private boolean toggleSelectionModeConfirmation = false;
    private boolean toggleSelectionConfirmation = false;
    private boolean selectionConfirmationMode = false;


    private ARCSelectorState state = ARCSelectorState.BROWSING;

    public ARCSelector(HardwareMap map, Telemetry telemetry, Class<T>... selectables) {
        this.hwMapTemp = map;
        this.telemetry = telemetry;
        this.selectables = new LinkedHashMap<>();
        this.toggleSelections = new HashMap<>();
        this.selectedValuesInstances = new HashMap<>();
        this.populate(selectables);
    }

    private void populate(Class<T>... selectables) {
        if(selectables.length == 0) return;
        for(int i = 0; i < selectables.length; i++) {
            this.selectables.put(i, selectables[i]);
        }
    }

    public void incrementIndex() {
        if(this.selectables.size() <= (this.currentIndex + 1)) {
            this.currentIndex = 0;
            return;
        }
        this.currentIndex++;
    }

    public void decrementIndex() {
        if((this.currentIndex - 1) < 0) {
            this.currentIndex = this.selectables.size() - 1;
            return;
        }
        this.currentIndex--;
    }


    public void onSelect() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if(!this.isToggleSelectionEnabled()) {
            this.selectedValueClass = this.selectables.get(this.currentIndex);
            assert this.selectedValueClass != null;
            this.selectedValueInstance = this.selectedValueClass.getDeclaredConstructor(HardwareMap.class, Telemetry.class).newInstance(hwMapTemp, telemetry);
        } else {
            this.toggleSelections.put(this.currentIndex, this.selectables.get(this.currentIndex));
            if(toggleSelectionConfirmation) {
                this.toggleSelections.forEach((k, v) -> {
                    try {
                        //TODO: Dynamic parameterized creation (for commands, other things, etc)
                        T instance = v.getDeclaredConstructor(
                                HardwareMap.class,
                                Telemetry.class
                        ).newInstance(hwMapTemp, telemetry);
                        this.selectedValuesInstances.put(k, instance);
                    } catch (IllegalAccessException | InstantiationException |
                             InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
                this.toggleSelectionConfirmation = false;
            }
        }
    }

    public void handleInputs(Gamepad gamepad) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        //navigation
        if(gamepad.dpadDownWasPressed()) incrementIndex();
        if(gamepad.dpadUpWasPressed()) decrementIndex();

        //state machine
        switch (getState()) {
            case BROWSING:
                if(gamepad.aWasPressed() && isToggleSelectionEnabled()) {
                    setState(ARCSelectorState.CONFIRM_SELECTION);
                }

                if(gamepad.leftBumperWasPressed()) {
                    setState(
                            isToggleSelectionEnabled() ? ARCSelectorState.CONFIRM_TOGGLE_DISABLE : ARCSelectorState.CONFIRM_TOGGLE_ENABLE
                    );
                }

                if(gamepad.rightBumperWasPressed()) {
                    onSelect();
                    if(!this.isToggleSelectionEnabled()) {
                        setState(ARCSelectorState.CONFIRMED_SELECTION);
                    }
                }
                break;
            case CONFIRM_TOGGLE_ENABLE:
                if(gamepad.rightBumperWasPressed()) {
                    enableToggleSelection(true);
                    setState(ARCSelectorState.BROWSING);
                }

                if(gamepad.leftBumperWasPressed()) {
                    setState(ARCSelectorState.BROWSING);
                }
                break;
            case CONFIRM_TOGGLE_DISABLE:
                if(gamepad.rightBumperWasPressed()) {
                    enableToggleSelection(false);
                    clearToggleSelections();
                    setState(ARCSelectorState.BROWSING);
                }
                if(gamepad.leftBumperWasPressed()) {
                    setState(ARCSelectorState.BROWSING);
                }
                break;
            case CONFIRM_SELECTION:
                if(gamepad.rightBumperWasPressed()) {
                    setSelectionConfirmation(true);
                    setState(ARCSelectorState.CONFIRMED_SELECTION);
                    onSelect();
                }
                if(gamepad.leftBumperWasPressed()) {
                    setSelectionConfirmation(false);
                    setState(ARCSelectorState.BROWSING);
                }
                break;
            case CONFIRMED_SELECTION:

                break;
        }
    }

    public boolean isSelectionFinalized() {
        return state == ARCSelectorState.CONFIRMED_SELECTION;
    }

    public void renderTelemetry() {
        telemetry.clearAll();
        if(state == ARCSelectorState.CONFIRMED_SELECTION) {
            telemetry.clearAll();
            telemetry.addData("[ARC SELECTOR]", getStateMessage());
            if(isToggleSelectionEnabled()) {
                this.toggleSelections.forEach((k, v) -> {
                    telemetry.addLine(v.getSimpleName());
                });
            } else {
                telemetry.addLine(this.selectedValueClass.getSimpleName());
            }
        } else {
            telemetry.addData("[ARC SELECTOR]", getStateMessage());
            getLines().forEach(telemetry::addLine);
        }
        telemetry.update();
    }

    public String getStateMessage() {
        return state.getMessage();
    }

    public void clearToggleSelections() {
        this.toggleSelections.clear();
    }

    public List<String> getLines() {
        List<String> lines = new ArrayList<>();
        lines.add("------------");
        if(isToggleSelectionEnabled()) {
            this.selectables.forEach((k, v) -> {
                if(this.currentIndex == k) {
                    if(!this.toggleSelections.isEmpty() && this.toggleSelections.containsKey(k)) {
                        lines.add("> (X) [" + k + "] -> " + v.getSimpleName());
                    } else {
                        lines.add("> ( ) [" + k + "] -> " + v.getSimpleName());
                    }
                } else {
                    if(!this.toggleSelections.isEmpty() && this.toggleSelections.containsKey(k)) {
                        lines.add("(X) [" + k + "] -> " + v.getSimpleName());
                    } else {
                        lines.add("( ) [" + k + "] -> " + v.getSimpleName());
                    }
                }
            });
        } else {

            this.selectables.forEach((k, v) -> {
                if(this.currentIndex == k) {
                    lines.add("> [" + k + "] -> " + v.getSimpleName());
                } else {
                    lines.add("[" + k + "] -> " + v.getSimpleName());
                }

            });
        }
        return lines;
    }

    public void configureCommands(ARCSelectorContext ctx) {
        if(!isSelectionFinalized()) return;
        if(!isToggleSelectionEnabled()) {
            configureSubsystem(this.selectedValueInstance, ctx);
        } else {
            this.toggleSelections.forEach((k, clazz) -> {
                T instance = this.selectedValuesInstances.get(k);
                assert instance != null;
                configureSubsystem(instance, ctx);
                telemetry.addData("debug", "configuring subsystem: " + instance.getClass().getSimpleName());
            });
        }
    }

    public void configureSubsystem(T subsystem, ARCSelectorContext ctx) {
        if(!(subsystem instanceof CommandConfigurableSubsystem)) return;
        CommandConfigurableSubsystem configurable = (CommandConfigurableSubsystem) subsystem;
        CommandScheduler.getInstance().registerSubsystem((Subsystem) subsystem);

        Command defaultCmd = configurable.createDefaultCommand(ctx);
        if(defaultCmd != null && ((SubsystemBase) subsystem).getDefaultCommand() == null) {
            ((SubsystemBase) subsystem).setDefaultCommand(defaultCmd);
        }
        configurable.configureBindings(ctx);

    }

    //TODO: Add folder navigation?

    public Collection<Class<T>> getSelectables() {
            return this.selectables.values();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getSize() {
        return this.selectables.size();
    }

    public T getSelectedValue() {
        return selectedValueInstance;
    }

    public boolean isToggleSelectionEnabled() {
        return toggleSelectionEnabled;
    }

    public void enableToggleSelection(boolean toggleSelectionEnabled) {
        this.toggleSelectionEnabled = toggleSelectionEnabled;
    }

    public boolean isInToggleConfirmationMode() {
        return toggleSelectionModeConfirmation;
    }

    public void toggleConfirmationMode(boolean toggleSelectionConfirmation) {
        this.toggleSelectionModeConfirmation = toggleSelectionConfirmation;
    }

    public boolean hasConfirmedSelection() {
        return toggleSelectionConfirmation;
    }

    public void setSelectionConfirmation(boolean toggleSelectionConfirmation) {
        this.toggleSelectionConfirmation = toggleSelectionConfirmation;
    }

    public boolean isInConfirmationMode() {
        return selectionConfirmationMode;
    }

    public void setSelectionConfirmationMode(boolean selectionConfirmationMode) {
        this.selectionConfirmationMode = selectionConfirmationMode;
    }

    public ARCSelectorState getState() {
        return state;
    }

    public void setState(ARCSelectorState state) {
        this.state = state;
    }
}
