package org.example.demo7;

import javafx.scene.canvas.GraphicsContext;

public class DrawAction {
    void connect() {
        HelloApplication.drawActions.add(this);
    }

    void run(GraphicsContext gc) {
    }

    void disconnect() {
        HelloApplication.drawActionRemove.add(this);
    }

    void onDisconnect() {
    }
}
