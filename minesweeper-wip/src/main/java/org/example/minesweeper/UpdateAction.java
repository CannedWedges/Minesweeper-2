package org.example.minesweeper;

public class UpdateAction {

    public UpdateAction() {

    }

    public void onUpdate(double lastUpdate) {
    };

    public final void bind() {
        HelloApplication.updateActions.add(this);
    }

    public final void disconnect() {
        HelloApplication.updateActions.remove(this);
    };
}
