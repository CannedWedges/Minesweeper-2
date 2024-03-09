package org.example.demo2;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HelloApplication extends Application {

    private static final int INIT_SCREEN_SIZE_X = 500;
    private static final int INIT_SCREEN_SIZE_Y = 300;

    private static final int TARGET_FRAME_RATE = 60;

    private final List<Renderable> objects = new ArrayList<>();


    public final Canvas canvas = new Canvas(INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);
    private final GraphicsContext gc = canvas.getGraphicsContext2D();
    public final Group ROOT = new Group(canvas);
    public final Scene SCENE = new Scene(ROOT, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));



    @Override
    public void start(Stage stage) throws IOException {


        stage.setScene(SCENE);





        stage.show();

        for (int i = 0; i < 1000; i++) {

            objects.add(new Rectangle(
                    new Vector2(Math.random() * 500, Math.random() * 300),
                    new Vector2(Math.random() * 100, Math.random() * 100),
                    Math.random() * 360,
                    Color.color(Math.random(), Math.random(), Math.random())
            ));
        }

        long[] lastUpdate = {System.nanoTime(), 0};
        //render loop:
        Timeline updateLoop = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
//            System.out.printf("%.3f\n", (System.nanoTime() - lastUpdate[0]) / 1000000000d);
            System.out.println("Frame: " + lastUpdate[1]);
            lastUpdate[0] = System.nanoTime();
            lastUpdate[1]++;
            render();
            System.out.printf("update took: %.3f\n", (System.nanoTime() - lastUpdate[0]) / 1000000000d);
        }), new KeyFrame(Duration.seconds(1d / TARGET_FRAME_RATE)));
        updateLoop.play();

        updateLoop.setCycleCount(-1);

        stage.setOnCloseRequest(event -> {
            updateLoop.stop();
        });

//        for (int i = 0; i < 10; i++) {
//            long startNano = System.nanoTime();
//            render();
//            long endNano = System.nanoTime();
//            System.out.println(endNano - startNano);
//        }



    }




    private void render() {
        if (gc != null) {
            gc.clearRect(0, 0, INIT_SCREEN_SIZE_X, 10000);
            long lastNano = System.nanoTime();
            for (Renderable obj : objects) {
//                System.out.println("a");
                obj.render(gc);
            }
            long elapsed = System.nanoTime() - lastNano;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}