package org.example.minesweeper;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.Observer;
import java.util.Vector;

public class Particle implements PlayableAnim {

    private static final Group ROOT = HelloApplication.ROOT;

    private Vector2 size = Vector2.identity;
    private Vector2 position = Vector2.zero;
    private Vector2 velocity = Vector2.zero;
    private Vector2 acceleration = Vector2.zero;

    private double duration;

    private double rotation = 0;
    private double torque = 0;

    private UpdateAction updateAction;

    private Rotate rotate;
    private Rectangle rect;

    public Particle(double duration, Vector2 position, Vector2 velocity, Vector2 size) {
        this.duration = duration;
        this.position = position;
        this.velocity = velocity;
        this.size = size;

        this.rotate = new Rotate();
        rotate.setAngle(10);
        rotate.setPivotX(position.getX());
        rotate.setPivotY(position.getY());

        this.rect = new Rectangle();
        rect.setTranslateX(position.getX() - size.getX() / 2);
        rect.setTranslateY(position.getY() - size.getY() / 2);

        rect.setHeight(size.getY());
        rect.setWidth(size.getX());

        rect.getTransforms().add(rotate);

        ROOT.getChildren().add(rect);
    }

    public void play() {
        if (updateAction == null) {
            Thread thread = new Thread(() -> {
                updateAction = new UpdateAction() {
                    @Override
                    public void onUpdate(double lastUpdate) {
                        velocity.add(acceleration.multiply(lastUpdate));
                        position.add(velocity.multiply(lastUpdate));
                        rotation += torque;

                        rotate.setAngle(rotation);

                        rect.setTranslateX(position.getX() - size.getX() / 2);
                        rect.setTranslateY(position.getY() - size.getY() / 2);

                    }
                };
                updateAction.bind();

                try {
                    Thread.sleep((long) (duration * 1000));
                } catch (InterruptedException e) {

                }



            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {

            }
            updateAction.disconnect();
            cleanUp();
        }
    }

    public void stop() {
        cleanUp();
    }

    private void cleanUp() {
        ROOT.getChildren().remove(rect);
        if (updateAction != null) {
            updateAction.disconnect();
            updateAction = null;
        }

    }
}
