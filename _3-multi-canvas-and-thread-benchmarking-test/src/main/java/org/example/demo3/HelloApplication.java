package org.example.demo3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class HelloApplication extends Application {


    private static final int INIT_SCREEN_SIZE_X = 500;
    private static final int INIT_SCREEN_SIZE_Y = 300;

    private static final int TARGET_FRAME_RATE = 60;
    private static final int RENDER_THREADS = 100;

    public static final List<Renderable> objects = new SortedList<>(Comparator.comparing(o -> o.getzIndex()));

    private static volatile double camX = 0;
    private static volatile double camY = 0;
    private static volatile Canvas[] renderGroup1 = new Canvas[RENDER_THREADS];
    private static volatile Canvas[] renderGroup2 = new Canvas[RENDER_THREADS];

    private static final ReentrantLock renderLock = new ReentrantLock();


    public final Group Group1 = new Group();
    public final Scene SCENE1 = new Scene(Group1, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));

    public final Group Group2 = new Group();
    public final Scene SCENE2 = new Scene(Group2, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));

    private boolean isLoading = false;

    private static boolean isFirstScene = true;
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        isFirstScene = true;
        stage.setScene(SCENE1);



        //setup canvas
        for (int i = 0; i < RENDER_THREADS; i++) {
            Canvas canvas1 = new Canvas(INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);
            canvas1.widthProperty().bind(SCENE1.widthProperty());
            canvas1.widthProperty().bind(SCENE1.heightProperty());
            Group1.getChildren().add(canvas1);
            renderGroup1[i] = canvas1;
        }

        for (int i = 0; i < RENDER_THREADS; i++) {
            Canvas canvas2 = new Canvas(INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);
            canvas2.widthProperty().bind(SCENE2.widthProperty());
            canvas2.widthProperty().bind(SCENE2.heightProperty());
            Group2.getChildren().add(canvas2);
            renderGroup2[i] = canvas2;
        }

        //setup update loop

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


        updateLoop.setCycleCount(-1);
        updateLoop.play();

        stage.setOnCloseRequest(event -> {
            updateLoop.stop();
        });
        Affine

        //try to add objects


        for (int i = 0; i < 10000; i++) {

            new Rectangle(
                    new Vector2(Math.random() * 500, Math.random() * 300),
                    new Vector2(Math.random() * 100, Math.random() * 100),
                    Math.random() * 360,
                    Color.color(Math.random(), Math.random(), Math.random())
            );
        }

        stage.show();
    }





    private void render() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        isFirstScene = !isFirstScene;


        Canvas[] currentGroup = isFirstScene ? renderGroup1 : renderGroup2;
        Thread[] threadPile = new Thread[RENDER_THREADS];


        System.out.println("Object count: " + objects.size());
        for (int i = 0; i < RENDER_THREADS; i++) {


            Canvas canvas = currentGroup[i];
            GraphicsContext gc = canvas.getGraphicsContext2D();

            int start = (int) Math.ceil((double) objects.size() * i / RENDER_THREADS);
            int end = (int) Math.ceil((double) objects.size() * (i + 1) / RENDER_THREADS);
            int finalI = i;
            Thread t = new Thread(() -> {
                long s1 = System.nanoTime();
                for (int j = start; j < end; j++) {

                    Renderable obj = objects.get(j);
                    obj.render(gc);
                }
                long dur = System.nanoTime() - s1;
//                System.out.println("Thread: " + finalI + " took: " + dur / 1000000000d + " seconds");

            });
            t.start();
            threadPile[i] = t;

        }

        try {
            for (Thread t : threadPile) {
                t.join();
            }

        } catch (InterruptedException e) {
            System.out.println();
        }

        long s1 = System.nanoTime();

        stage.setScene(isFirstScene ? SCENE1 : SCENE2);

//        for (Canvas canvas : currentGroup) {
//            canvas.snapshot(null, null);
//        }

        long dur = System.nanoTime() - s1;
        System.out.println("Updating took: " + dur / 1000000000d + " seconds");
        isLoading = false;
    }




    public static void main(String[] args) {
        launch();
    }
}