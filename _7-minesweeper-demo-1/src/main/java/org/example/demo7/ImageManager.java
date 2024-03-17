package org.example.demo7;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageManager {

    public static final Image[] images = initialiseImage();

    public static Image[] initialiseImage() {
        return new Image[]{
                new Image("file:img/img1.png"),
                new Image("file:img/img2.png"),
                new Image("file:img/img3.png"),
                new Image("file:img/img4.png"),
                new Image("file:img/img5.png"),
                new Image("file:img/img6.png"),
                new Image("file:img/img7.png"),
                new Image("file:img/img8.png"),
                new Image("file:img/img9.png"),
                new Image("file:img/img10.png"),
                new Image("file:img/img11.png"),
                new Image("file:img/img12.png"),
        };
    }




    private static Image createFlagImage() {
        Canvas base = new Canvas(25, 25);
        GraphicsContext gc = base.getGraphicsContext2D();
//        gc.setLineWidth(0);
        gc.setImageSmoothing(false);

        gc.setFill(Color.color(.7, .7, .7));
        gc.fillRect(0, 0, 25, 25);
        /* Rectangle 1 */
        gc.setFill(Color.color(.2, .2, .2));
        gc.fillRect(2, 17, 17, 3);




        return createSnapshot(base);
    }


    private static Image createImageExample() {

        Canvas base = new Canvas(25, 25);
        GraphicsContext gc = base.getGraphicsContext2D();

        gc.setLineWidth(0);
        gc.fillPolygon(
                new double[]{
                        0, 25, 0
                }, new double[]{
                        0, 12.5, 25
                }, 3);
        return createSnapshot(base);
    }

    private static Image createSnapshot(Node node) {
        WritableImage img = new WritableImage(
                (int) node.getBoundsInParent().getWidth(),
                (int) node.getBoundsInParent().getHeight()
        );
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        node.snapshot(snapshotParameters, img);

        return img;
    }
}
