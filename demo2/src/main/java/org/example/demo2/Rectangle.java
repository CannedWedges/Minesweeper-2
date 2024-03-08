package org.example.demo2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Instance implements Renderable  {

    private Vector2 size;
    private Vector2 position;

    private double rotation;

    private int borderSize;

    private Transform2 transform2 = Transform2.identity;

    private Color borderColor;

    private Color color;

    private double[] px;
    private double[] py;

    private Rotate r;

    public Rectangle(Vector2 size, Vector2 position, double rotation) {

        if (rotation != 0) {
            transform2 = Transform2.identity.rotate(Math.toRadians(rotation));
        }
        this.size = size;
        this.position = position;

        this.rotation = rotation;

        updateCorners();
    }

    public Rectangle(Vector2 size, Vector2 position, double rotation, Color color) {

        if (rotation != 0) {
            transform2 = Transform2.identity.rotate(Math.toRadians(rotation));
        }
        this.size = size;
        this.position = position;

        this.color = color;

        this.rotation = rotation;

        updateCorners();
    }

    private void updateCorners() {

//        Vector2 sizeOff1 = transform2.transform(size).multiply(.5);
//        Vector2 sizeOff2 = transform2.transform(size.flipX()).multiply(.5);
        r = new Rotate(rotation, position.getX(), position.getY());

//        System.out.println(transform2);
//
//        PrintHelper.print(sizeOff1.toDouble());
//        PrintHelper.print(sizeOff2.toDouble());



        //maybe optimise later?

//        this.px = new double[] {position.getX() - sizeOff1.getX(), position.getX() - sizeOff2.getX(), position.getX() + sizeOff1.getX(), position.getX() + sizeOff2.getX()};
//
//        this.py = new double[] {position.getY() - sizeOff1.getY(), position.getY() - sizeOff2.getY(), position.getY() + sizeOff1.getY(), position.getY() + sizeOff2.getY()};

//        PrintHelper.print(px);
//        PrintHelper.print(py);
    }

    

    @Override
    public void render(GraphicsContext gc) {
        //render self



        gc.save();
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        gc.setFill(color);
        gc.fillRect(position.getX() - size.getX() / 2, position.getY() - size.getY() / 2, size.getX(), size.getY());

//        System.out.println(position.getX() + " " + position.getY() + " " + rotation);
//        System.out.println((position.getX() - size.getX() / 2) + " " + (position.getY() - size.getY() / 2) + " " + size.getX() + " " + size.getY());
        gc.restore();
//        gc.fillPolygon(px, py, 4);




        /*
        //render children
        for (Instance child : getChildren()) {
            if (child instanceof Renderable) {
                ((Renderable) child).render(gc);
            }
        }*/
    }


}
