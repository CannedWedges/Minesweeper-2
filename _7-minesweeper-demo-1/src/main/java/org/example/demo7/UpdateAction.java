package org.example.demo7;

public class UpdateAction {
    final void connect() {
        HelloApplication.updateActions.add(this);
    }

    void run(double elapsed) {
    }

    final void disconnect() {
        HelloApplication.updateActionRemove.add(this);
    }

    void onDisconnect() {
    }
}
