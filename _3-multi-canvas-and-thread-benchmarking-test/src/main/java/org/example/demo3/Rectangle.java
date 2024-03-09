package org.example.demo3;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Rectangle extends Renderable {

    private final Vector2 size;
    private final Vector2 position;
    private final double rotation;

    private int borderSize;

    private Color borderColor;

    private Color color;

    private Rotate r;

    public Rectangle(Vector2 size, Vector2 position, double rotation) {
        super(1);
        this.size = size;
        this.position = position;

        this.rotation = rotation;

        updateCorners();
    }

    public Rectangle(Vector2 size, Vector2 position, double rotation, Color color) {
        super(1);
        this.size = size;
        this.position = position;

        this.rotation = rotation;
        this.color = color;

        updateCorners();
    }

    private void updateCorners() {

        r = new Rotate(rotation, position.getX(), position.getY());
    }


    @Override
    public void render(GraphicsContext gc) {
        //render self

        gc.save();

        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(color);
        gc.fillRect(position.getX() - size.getX() / 2, position.getY() - size.getY() / 2, size.getX(), size.getY());

        gc.restore();
    }


}
