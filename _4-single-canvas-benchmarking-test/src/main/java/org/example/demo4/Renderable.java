package org.example.demo4;

import javafx.scene.canvas.GraphicsContext;

abstract public class Renderable {

    public int zIndex;

    abstract void render(GraphicsContext gc);


    public Renderable(int zIndex) {
        this.zIndex = zIndex;
        HelloApplication.objects.add(this);
    }

    public int getzIndex() {
        return this.zIndex;
    }
}
