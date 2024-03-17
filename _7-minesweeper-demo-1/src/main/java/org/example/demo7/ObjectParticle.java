package org.example.demo7;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

public class ObjectParticle {


    static void asParticle(Rectangle part, double lifetime, Vector2 vel, Vector2 acc, double rotVel, double rotAcc) {


        Vector2 Position = new Vector2(part.getX(), part.getY());
        Vector2 Velocity = vel.copy();
        Vector2 Acceleration = acc.copy();

        double[] dat = {part.getRotate(), rotVel, rotAcc, lifetime};
        UpdateAction action = new UpdateAction() {
            @Override
            void run(double elapsed) {
                dat[3] -= elapsed;
                if (dat[3] > 0) {
                    Velocity.addFraction(Acceleration, elapsed);
                    Position.addFraction(Velocity, elapsed);


//                    System.out.println(Velocity);
//                    System.out.println(Acceleration);
//                    System.out.println(Velocity);

                    dat[1] += dat[2] * elapsed;
                    dat[0] += dat[1] * elapsed;

                    Platform.runLater(() -> {
                        part.setX(Position.x);
                        part.setY(Position.y);
                        part.setRotate(dat[0]);
                    });

                } else {
                    disconnect();
                }
            }

            @Override
            void onDisconnect() {
//                System.out.println("removed");
                HelloApplication.root.getChildren().remove(part);
            }
        };
        action.connect();

    }


    static void asParticle(Rectangle part) {
        asParticle(part, 3, Vector2.fromPolar(random(50, 100), random(0, 360)), new Vector2(0, 200),
                random(-1000, 1000), 0
        );
    }

    static void asParticle(Rectangle part, double power) {
        asParticle(part, 3, Vector2.fromPolar(power, random(0, 360)), new Vector2(0, 200),
                random(-1000, 1000), 0
        );
    }


    private static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }
}
