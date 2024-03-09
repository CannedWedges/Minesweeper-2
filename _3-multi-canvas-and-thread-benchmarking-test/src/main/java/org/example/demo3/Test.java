package org.example.demo3;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Test extends Application {
    private static final double SIZE = 200;

    public void start(Stage stage) {
        Canvas canvas = new Canvas(SIZE, SIZE);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        event -> drawShapes(canvas.getGraphicsContext2D())
                ),
                new KeyFrame(Duration.seconds(1/60d))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stage.setScene(new Scene(new Group(canvas)));
        stage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.clearRect(0, 0, SIZE, SIZE);
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        double widthOval = 5;
        double heightOval = 5;
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                if (Math.random() < 0.5) {
                    gc.fillOval(i * widthOval, j * heightOval, widthOval, heightOval);
                }
                else {
                    gc.strokeOval(i * widthOval, j * heightOval, widthOval, heightOval);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}