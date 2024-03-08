package org.example.demo4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class HelloApplication extends Application {


    private static final int INIT_SCREEN_SIZE_X = 800;
    private static final int INIT_SCREEN_SIZE_Y = 800;

    private static final int TARGET_FRAME_RATE = 60;
    private static final int RENDER_THREADS = 100;

    public static final List<Renderable> objects = new SortedList<>(Comparator.comparing(o -> o.getzIndex()));

    private static volatile double camX = 0;
    private static volatile double camY = 0;
    private static volatile Canvas canvas1 = new Canvas(INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);
    private static volatile Canvas canvas2 = new Canvas(INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);

//    private static final ReentrantLock renderLock = new ReentrantLock();


    public final Group Group1 = new Group(canvas1);
    public final Scene SCENE1 = new Scene(Group1, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));

    public final Group Group2 = new Group(canvas2);
    public final Scene SCENE2 = new Scene(Group2, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));

    private boolean isLoading = false;

    private static boolean isFirstScene = true;
    private Stage stage;

    long[] lastUpdate = {System.nanoTime(), 0};

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        isFirstScene = true;
        stage.setScene(SCENE1);



        //setup canvas


        canvas1.widthProperty().bind(SCENE1.widthProperty());
        canvas1.widthProperty().bind(SCENE1.heightProperty());

        canvas2.widthProperty().bind(SCENE2.widthProperty());
        canvas2.widthProperty().bind(SCENE2.heightProperty());

        //setup update loop




        //render loop:
        Timeline updateLoop = new Timeline(new KeyFrame(Duration.seconds(1d / TARGET_FRAME_RATE), event -> {
//            System.out.printf("%.3f\n", (System.nanoTime() - lastUpdate[0]) / 1000000000d);
            render();
        }), new KeyFrame(Duration.seconds(0), event -> {

            /* update loop */
            Vector2 translation = new Vector2(0, 1)
;
            for (Renderable v : objects) {
                ((Rectangle) v).setPosition(((Rectangle) v).getPosition().add(translation));
            }
        }));


        updateLoop.setCycleCount(-1);
        updateLoop.play();

        stage.setOnCloseRequest(event -> {
            updateLoop.stop();
        });

        //try to add objects


        for (int i = 0; i < 3000; i++) {

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


//        System.out.println("Frame: " + lastUpdate[1]);
        long a = lastUpdate[0];




        isLoading = true;

        isFirstScene = !isFirstScene;
        Canvas canvas = isFirstScene ? canvas1 : canvas2;


        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);


        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, 1000, 1000);

        long s1 = System.nanoTime();
        for (Renderable obj : objects) {
            obj.render(gc);
        }


//        System.out.println(stage);

        stage.setScene(isFirstScene ? SCENE1 : SCENE2);
//        WritableImage img = new WritableImage(INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y);
//        canvas.snapshot(null, img);

        System.out.println(1000000000d / (System.nanoTime() - lastUpdate[0]) + " frames per second.");
        lastUpdate[0] = System.nanoTime();
        lastUpdate[1]++;

        long dur = System.nanoTime() - s1;
//        System.out.println("Updating took: " + dur / 1000000000d + " seconds");
        isLoading = false;


//        System.out.printf("update took: %.3f\n", (System.nanoTime() - lastUpdate[0]) / 1000000000d);
    }




    public static void main(String[] args) {
        launch();
    }
}