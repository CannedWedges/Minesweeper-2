package org.example.demo7;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {


    public static final int INIT_SCREEN_X = 500;
    public static final int INIT_SCREEN_Y = 500;
    public static final int UPDATE_RATE = 60;


    public static final Canvas canvas = new Canvas(INIT_SCREEN_X, INIT_SCREEN_Y);

    public static final Group root = new Group(canvas);

    public static final Scene scene = new Scene(root, INIT_SCREEN_X, INIT_SCREEN_Y);

    public static final List<DrawAction> drawActions = new ArrayList<>(); /* runs when it is rendered, use for canvas */
    public static final List<DrawAction> drawActionRemove = new ArrayList<>();
    public static final List<UpdateAction> updateActions = new ArrayList<>(); /* runs before the next frame is drawn */
    public static final List<UpdateAction> updateActionRemove = new ArrayList<>(); /* removes update actions after concurrently executing the actions */

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(scene);
        stage.setResizable(false);

        startUpdateLoop(stage);


        stage.show();


        new Thread(new Task<Void>() {
            @Override
            protected Void call() {
                Main.start(stage, root, canvas);
                return null;
            }
        }).start();

    }

    private void startUpdateLoop(Stage stage) {
        //update loop
        long[] updateData = {0, System.nanoTime()};
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(0), event -> {/* draw loop */
            updateData[0]++;
//            System.out.println("Frame: " + updateData[0]);
            long start = System.nanoTime();
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (DrawAction action : drawActions) {
                action.run(gc);
            }
            for (DrawAction action : drawActionRemove) {
                if (action != null) {

                    action.onDisconnect();
                    drawActions.remove(action);
                }
            }
            drawActionRemove.clear();
            canvas.snapshot(null, null);
//            System.out.printf("\tDraw: %-6fms", (System.nanoTime() - start) / 100000d);
        }), new KeyFrame(Duration.seconds(0), event -> { /* prepare for next update */
            long elapsedNano = System.nanoTime() - updateData[1];
            double elapsed = elapsedNano / 1000000000d;
            updateData[1] += elapsedNano;
            long start = System.nanoTime();
//            System.out.println(updateActions.size());
            updateActions.parallelStream().forEach(obj -> obj.run(elapsed));
            for (UpdateAction action : updateActionRemove) {
                if (action != null) {
                    action.onDisconnect();
                    updateActions.remove(action);
                }

            }
            updateActionRemove.clear();
//            System.out.printf("\tUpdate: %-6fms\n", (System.nanoTime() - start) / 100000d);
        }), new KeyFrame(Duration.seconds(1d / UPDATE_RATE))
        );

        loop.setCycleCount(-1);
        loop.play();

        stage.setOnCloseRequest(event -> loop.stop());
    }
}