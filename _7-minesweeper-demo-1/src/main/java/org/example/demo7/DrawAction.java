package org.example.demo7;

import javafx.scene.canvas.GraphicsContext;

public class DrawAction {
    final void connect() {
        HelloApplication.drawActions.add(this);
    }

    void run(GraphicsContext gc) {
    }

    final void disconnect() {
        HelloApplication.drawActionRemove.add(this);
    }

    void onDisconnect() {
    }
}
