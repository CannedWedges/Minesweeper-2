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

    public static Image[] initialiseImage() throws NullPointerException {
        return new Image[]{
                new Image(ImageManager.class.getResource("images/img1.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img2.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img3.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img4.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img5.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img6.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img7.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img8.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img9.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img10.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img11.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img12.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img1a.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img2a.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img3a.png").toExternalForm()),
                new Image(ImageManager.class.getResource("images/img0.png").toExternalForm()),
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
