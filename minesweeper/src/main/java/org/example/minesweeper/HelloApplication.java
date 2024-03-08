package org.example.minesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    private static final int INIT_SCREEN_SIZE_X = 500;
    private static final int INIT_SCREEN_SIZE_Y = 300;
    private static final int TARGET_FRAME_RATE = 60;

    //scene essentials
    public static final Group ROOT = new Group();
    public static final Scene SCENE = new Scene(ROOT, INIT_SCREEN_SIZE_X, INIT_SCREEN_SIZE_Y, Color.color(.9, .9, .9));

    public static final List<UpdateAction> updateActions = new ArrayList<UpdateAction>();
    private static boolean isRunning = false;



    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Hello!");
        stage.setScene(SCENE);
        stage.show();


        SCENE.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("X: " + mouseEvent.getSceneX());
                System.out.println("Y: " + mouseEvent.getSceneY());

                PlayableAnim particle = new Particle(5, new Vector2(5, 5), new Vector2(1, 1), new Vector2(10, 10));
                particle.play();
            }
        });

        Text a = new Text();


        ObservableList


        initialiseFrameUpdate(stage);


    }



    /**
     * Initialises the update rates of this application
     */
    private static void initialiseFrameUpdate(Stage stage) {


        if (isRunning) {
            return;
        }
        isRunning = true;



        final long[] lastMillis = {System.currentTimeMillis()};


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> {
            /* call other frame actions */

            long currentMillis = System.currentTimeMillis() - lastMillis[0];
            lastMillis[0] += currentMillis;

            double lastUpdate = currentMillis / 1000d;

            for (UpdateAction action : updateActions) {
                new Thread(() -> action.onUpdate(lastUpdate)).start();
            }
        }));

        stage.setOnCloseRequest(event -> {
            if (timeline != null) {
                timeline.stop();
            }
        });

        timeline.stop();


    }


    public static void main(String[] args) {
        launch();
    }
}