package org.example.demo7;

public class UpdateAction {
    void connect() {
        HelloApplication.updateActions.add(this);
    }

    void run(double elapsed) {
    }

    void disconnect() {
//        HelloApplication.updateActions.remove(this);
        HelloApplication.queueRemove.add(this);
    }

    void onDisconnect() {
    }
}
