package org.example.demo5;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class HelloApplication extends Application {


    private static final int INIT_SCREEN_SIZE_X = 800;

    private static final int INIT_SCREEN_SIZE_Y = 800;
    private static final int TARGET_FRAME_RATE = 60;

    public final Group GROUP = new Group();
    public final Scene SCENE = new Scene(GROUP, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));




    @Override
    public void start(Stage stage) {


        stage.setTitle("Hello!");
        stage.setScene(SCENE);
        stage.show();

        List<Rectangle> obj = new ArrayList<>(10000);

        for (int i = 0; i < 10000; i++) {
            Rectangle rectangle = new Rectangle(Math.random() * 300, Math.random() * 800, Math.random() * 1000, Math.random() * 10);

            rectangle.setFill(Color.color(Math.random(), Math.random(), Math.random()));
            rectangle.setRotate(50);

            obj.add(rectangle);
            GROUP.getChildren().add(rectangle);
        }

        AtomicLong before = new AtomicLong(System.nanoTime());

        List<Double> a = new ArrayList<>();

        Timeline updateLoop = new Timeline(new KeyFrame(Duration.seconds(1d / TARGET_FRAME_RATE), event -> {

            for (Rectangle rectangle : obj) {
                rectangle.setX(rectangle.getX() + 1);
                rectangle.setRotate(rectangle.getRotate() + (rectangle.getX() * Math.random() / 10));

            }
            a.add(0,1000000000d / (System.nanoTime() - before.get()));
            before.set(System.nanoTime());

            if (a.size() > 10) {
                a.remove(10);
            }


            double sum = 0;
            for (double v : a) {
                sum += v;
            }

            System.out.println(sum / a.size() + " updates per second");

        }));

        updateLoop.setCycleCount(-1);
        updateLoop.play();

        stage.setOnCloseRequest(event -> {
            updateLoop.stop();
        });

    }

    public static void main(String[] args) {
        launch();
    }
}