package org.example.demo7;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Main {

    private static Image[] images = ImageManager.images;

    public static final int NUM_MINES = 15;
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 10;
    public static final double SHUFFLE_RATIO = 5;

    public static boolean[][] mines = new boolean[0][0];
    public static GameGrid[][] sprites = new GameGrid[0][0];

    public static void start(Stage stage, Group root, Canvas canvas) {
        Platform.runLater(() -> {
            canvas.getGraphicsContext2D().setImageSmoothing(false);
        });

        preloadImage();


        mines = generateMines(BOARD_WIDTH, BOARD_HEIGHT, SHUFFLE_RATIO);
        sprites = new GameGrid[BOARD_WIDTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                sprites[i][j] = new GameGrid(i * 25, j * 25);

                if (mines[i][j]) {
                    sprites[i][j].setState(-2);
                }
            }
        }


    }

    private static Rectangle makeRectangle1(int x, int y) {
        Rectangle a = new Rectangle(25, 25, Color.rgb(150, 150, 150));
        a.setX(x);
        a.setY(y);
        Platform.runLater(() -> {
            HelloApplication.root.getChildren().add(a);
        });
        return a;
    }

    private static void preloadImage() {
        Thread thread = new Thread(() -> {
            images = ImageManager.initialiseImage();
        });
        Platform.runLater(thread);
        try {
            thread.join();
        } catch (InterruptedException e) {

        }

    }

    private static boolean[][] generateMines(int width, int height, double shuffleRatio) {
        /* create initial mines */
        boolean[][] mines = new boolean[width][height];
        int numRef = (int) shuffleRatio * width * height;
        int[][] ref = new int[numRef][2];

        int d = (int) ((double) NUM_MINES / height);
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < height; j++) {
                mines[i][j] = true;
            }
        }


        for (int j = 0; j < NUM_MINES % height; j++) {
            mines[d][j] = true;
        }

//        System.out.println(Arrays.deepToString(mines));

        /* shuffle the mines */

        for (int i = 0; i < numRef; i++) {
            int x1 = random(width);
            int y1 = random(height);

            int x2 = random(width);
            int y2 = random(height);
            /* flip the values */

            boolean temp = mines[x1][y1];

            mines[x1][y1] = mines[x2][y2];
            mines[x2][y2] = temp;
        }
//        System.out.println(Arrays.deepToString(mines));
        return mines;
    }

    private static void particleDemo() {
        ArrayList<Rectangle> obj = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                obj.add(makeRectangle1(i * 25, j * 25));
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        for (Rectangle r : obj) {
            ObjectParticle.asParticle(r, 50);
        }
    }

    private static void mouseParticleDemo(Group root) {
        root.setOnMouseClicked(mouseEvent -> {

            for (int i = 0; i < 10; i++) {
                Rectangle rectangle = new Rectangle(5, 5);
                rectangle.setX(mouseEvent.getX());
                rectangle.setY(mouseEvent.getY());

                rectangle.setFill(randColor());

                root.getChildren().add(rectangle);


                ObjectParticle.asParticle(rectangle);
//                System.out.println(rectangle);
            }

        });
    }

    private static Color randColor() {
        return Color.hsb(random(0, 360), random(.9, 1), 1);
    }

    private static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    private static int random(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1)) + min;
    }

    private static int random(int size) {
        return (int) Math.floor(Math.random() * (size));
    }
}
