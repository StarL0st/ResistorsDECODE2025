package org.firstinspires.ftc.teamcode.selection;

public enum ARCSelectorState {
    BROWSING("Select a subsystem to test"),
    CONFIRM_TOGGLE_ENABLE("Enable toggle selection?"),
    CONFIRM_TOGGLE_DISABLE("Disable toggle selection?"),
    CONFIRM_SELECTION("Confirm selection?"),
    CONFIRMED_SELECTION("Confirmed selection of values: ");

    private final String message;

    ARCSelectorState(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
