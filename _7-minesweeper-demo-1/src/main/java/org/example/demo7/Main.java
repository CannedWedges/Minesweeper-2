package org.example.demo7;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main {
    public static void start(Stage stage, Group root, Canvas canvas) {


        root.setOnMouseClicked(mouseEvent -> {

            for (int i = 0; i < 10; i++) {
                Rectangle rectangle = new Rectangle(5, 5);
                rectangle.setX(mouseEvent.getX());
                rectangle.setY(mouseEvent.getY());

                rectangle.setFill(randColor());

                root.getChildren().add(rectangle);


                ObjectParticle.asParticle(rectangle);
//                System.out.println(rectangle);
            }

        });






    }


    private static Color randColor() {
        return Color.hsb(random(0, 360), random(.9, 1), 1);
    }

    private static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
