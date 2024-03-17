package org.example.demo7;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ImageSprite {


    double x;
    double y;
    double width;
    double height;

    double r = 0;

    Image img;

    private DrawAction action;



    public ImageSprite(double width, double height) {
        this.width = width;
        this.height = height;
        action = new DrawAction() {
            @Override
            public void run(GraphicsContext gc) {
//                gc.setFill(Color.rgb(0, 0, 0));


                gc.save();

//                gc.fillRect(0, 0, 10, 10);
                if (r != 0) {
                    gc.translate(x, y);
                    gc.rotate(r);
                    gc.drawImage(img, -width / 2, -height / 2);
                } else {
                    gc.drawImage(img, x - width / 2, y - height / 2);
                }


                gc.restore();
            }
        };
        action.connect();
    }

    public ImageSprite(double width, double height, Image img) {
        this(width, height);
        this.img = img;
    }

    public ImageSprite(double width, double height, Image img, double x, double y) {
        this(width, height);
        this.x = x;
        this.y = y;
        this.img = img;
    }

    public void setImage(Image img) {
        this.img = img;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public void destroy() {
        action.disconnect();
    }



}
