package org.example.demo7;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class Main {

    public static final int NUM_MINES = 60;
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 15;
    public static final double SHUFFLE_RATIO = 5;
    public static final long EXPLORE_THRESHOLD = 100;
    public static final boolean DIGS_QUESTION_GRID = true;
    public static final int GRID_SIZE = 25;
    public static boolean[][] mines = new boolean[0][0];
    public static int[][] adjacentMineCount = new int[0][0];
    public static int[][] gameState = new int[0][0];
    public static GameGrid[][] sprites = new GameGrid[0][0];
    public static boolean hasLost = false;

    public static int numMarkedMines = 0;
    public static Text numMineDisplay;
    private static Image[] images = ImageManager.images;

    public static void start(Stage stage, Group root, Canvas canvas) {
        Platform.runLater(() -> {

            canvas.getGraphicsContext2D().setImageSmoothing(false);
        });

        preloadImage();

        boolean[] mouseConditions = {false, false};
//        int[] initialClick = {-2, -2};


        sprites = new GameGrid[BOARD_WIDTH][BOARD_HEIGHT];
        gameState = new int[BOARD_WIDTH][BOARD_HEIGHT];

        AtomicBoolean hasGameStarted = new AtomicBoolean(false);
        AtomicLong prevReleaseTime = new AtomicLong();


        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                sprites[i][j] = new GameGrid(i * GRID_SIZE, j * GRID_SIZE + 100);
            }
        }

        //set up hover effects
        int[] prevHover = new int[]{-2, -2};
        root.setOnMouseMoved(mouseEvent -> {
            //hover effects here

            int[] hitPos = getPosOnGrid(mouseEvent.getSceneX() - root.getLayoutX(), mouseEvent.getSceneY() - root.getLayoutY() - 100);
            updateMouseEvent(prevHover, hitPos, mouseConditions);
            prevHover[0] = hitPos[0];
            prevHover[1] = hitPos[1];
        });

        root.setOnMouseDragged(mouseEvent -> {
            int[] hitPos = getPosOnGrid(mouseEvent.getSceneX() - root.getLayoutX(), mouseEvent.getSceneY() - root.getLayoutY() - 100);
            updateMouseEvent(prevHover, hitPos, mouseConditions);
            prevHover[0] = hitPos[0];
            prevHover[1] = hitPos[1];
        });

        root.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                mouseConditions[0] = true;


                int[] hitPos = getPosOnGrid(mouseEvent.getSceneX() - root.getLayoutX(), mouseEvent.getSceneY() - root.getLayoutY() - 100);
                updateMouseEvent(prevHover, hitPos, mouseConditions);
                prevHover[0] = hitPos[0];
                prevHover[1] = hitPos[1];
                prevReleaseTime.set(0);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                mouseConditions[1] = true;


                int[] hitPos = getPosOnGrid(mouseEvent.getSceneX() - root.getLayoutX(), mouseEvent.getSceneY() - root.getLayoutY() - 100);
                updateMouseEvent(prevHover, hitPos, mouseConditions);
                prevHover[0] = hitPos[0];
                prevHover[1] = hitPos[1];
                prevReleaseTime.set(0);
            }
        });

        root.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                mouseConditions[0] = false;


                int[] hitPos = getPosOnGrid(mouseEvent.getSceneX() - root.getLayoutX(), mouseEvent.getSceneY() - root.getLayoutY() - 100);
                updateMouseEvent(prevHover, hitPos, mouseConditions);
                prevHover[0] = hitPos[0];
                prevHover[1] = hitPos[1];

                if (hasGameStarted.get()) {
                    if (System.currentTimeMillis() - prevReleaseTime.get() < EXPLORE_THRESHOLD) {
                        //explore 3x3 grid
                        exploreGrid(hitPos[0], hitPos[1]);
                    } else {
                        digGrid(hitPos[0], hitPos[1]);
                    }

                } else if (System.currentTimeMillis() - prevReleaseTime.get() > EXPLORE_THRESHOLD) {
                    //initialise game
                    hasGameStarted.set(true);

                    mines = generateMines(BOARD_WIDTH, BOARD_HEIGHT, hitPos[0], hitPos[1]);
                    adjacentMineCount = calculateAdjacentMines(BOARD_WIDTH, BOARD_HEIGHT, mines);
                    gameState = createArrayWithInitialValue2d(BOARD_WIDTH, BOARD_HEIGHT, -1);

                    digGrid(hitPos[0], hitPos[1]);

                }
                prevReleaseTime.set(System.currentTimeMillis());
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                mouseConditions[1] = false;


                int[] hitPos = getPosOnGrid(mouseEvent.getSceneX() - root.getLayoutX(), mouseEvent.getSceneY() - root.getLayoutY() - 100);
                updateMouseEvent(prevHover, hitPos, mouseConditions);
                prevHover[0] = hitPos[0];
                prevHover[1] = hitPos[1];

                if (hasGameStarted.get()) {
                    if (System.currentTimeMillis() - prevReleaseTime.get() < EXPLORE_THRESHOLD) {
                        //explore 3x3 grid
                        exploreGrid(hitPos[0], hitPos[1]);
                    } else {
                        //dig current grid
                        markGrid(hitPos[0], hitPos[1]);
                    }

                } else if (System.currentTimeMillis() - prevReleaseTime.get() > EXPLORE_THRESHOLD) {
                    //initialise game
                    hasGameStarted.set(true);

                    mines = generateMines(BOARD_WIDTH, BOARD_HEIGHT, hitPos[0], hitPos[1]);
                    adjacentMineCount = calculateAdjacentMines(BOARD_WIDTH, BOARD_HEIGHT, mines);
                    gameState = createArrayWithInitialValue2d(BOARD_WIDTH, BOARD_HEIGHT, -1);

                    digGrid(hitPos[0], hitPos[1]);

                }
                prevReleaseTime.set(System.currentTimeMillis());

            }
        });

        //prepare top bar

        Rectangle topBackground = new Rectangle(HelloApplication.INIT_SCREEN_X, 100, Color.color(.7, .7, .75));
        topBackground.setViewOrder(1);

        numMineDisplay = new Text(100, 20, Integer.toString(NUM_MINES));
        numMineDisplay.setViewOrder(2);


        root.getChildren().add(topBackground);
    }


    private static void updateMouseEvent(int[] prevHover, int[] hitPos, boolean[] mouseConditions) {
        //clean up

        if (prevHover[0] != -2) {
//            System.out.println(prevHover[0]);
//            System.out.println(prevHover[1]);
            sprites[prevHover[0]][prevHover[1]].hoverEnded();

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    releaseGrid(sprites, prevHover[0] + i, prevHover[1] + j);
                }
            }
        }


        //perform hover event
        if (mouseConditions[0] && mouseConditions[1]) { /* scanning block */
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    holdGrid(sprites, hitPos[0] + i, hitPos[1] + j);
                }
            }
        } else if (mouseConditions[0] || mouseConditions[1]) { /* holding either mouse */
            holdGrid(sprites, hitPos[0], hitPos[1]);
        } else {


            if (hitPos[0] != -2) {
                sprites[hitPos[0]][hitPos[1]].hover();
            }
        }
    }

    private static void digGrid(int x, int y) {
        if (!hasLost) {
            if (x < 0 || y < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT) {
                return;
            }
            int gridCondition = gameState[x][y];

            if (gridCondition == -1 || (gridCondition == -3 && DIGS_QUESTION_GRID)) { // is empty or a question grid
                int revealedGridCondition = adjacentMineCount[x][y];
                if (revealedGridCondition == -2) { // is a mine

                    //reveal that it is a mine
                    gameState[x][y] = -4;
                    sprites[x][y].setState(-4);

                    loseGame();
                } else { // is not a mine
                    gameState[x][y] = revealedGridCondition;

                    //update current grid
                    sprites[x][y].setState(revealedGridCondition);

                    if (revealedGridCondition == 0) {
                        //reveal adjacent
                        digGrid(x - 1, y - 1);
                        digGrid(x - 1, y);
                        digGrid(x - 1, y + 1);

                        digGrid(x, y - 1);
                        digGrid(x, y + 1);

                        digGrid(x + 1, y - 1);
                        digGrid(x + 1, y);
                        digGrid(x + 1, y + 1);
                    }

                }
            } else {

            }
        } else {

        }
    }

    private static void loseGame() {
        hasLost = true;
        System.out.println("User has lost");

//        int[] count = new int[]{0};
        long loseDelay = 750;
        long millisPerDiagonalRow = 50;


        int[] lastCount = new int[]{0};
        long startMillis = System.currentTimeMillis() + loseDelay;

        UpdateAction action = new UpdateAction() {
            @Override
            void run(double elapsed) {
                //scan through all grids
                int count = (int) ((double) (System.currentTimeMillis() - startMillis) / millisPerDiagonalRow);
//                System.out.println(count);
                if (count >= BOARD_WIDTH + BOARD_HEIGHT) {
                    disconnect();
                } else {
                    for (int newCount = lastCount[0]; newCount < count; newCount++) {
                        //reveal grids
                        for (int i = 0; i <= newCount; i++) {
                            if (inBounds(newCount - i, i, BOARD_WIDTH, BOARD_HEIGHT)) {
                                //reveal this grid with (count[0] - i, i)

                                int curState = gameState[newCount - i][i];
                                if (curState != -2) {
                                    int newState = adjacentMineCount[newCount - i][i];
                                    sprites[newCount - i][i].setState(newState == -2 ? -4 : newState);
                                }


                            }
                        }
                    }
                    lastCount[0] = count;
                }
            }
        };
        action.connect();
    }

    private static void markGrid(int x, int y) {

        if (!hasLost) {

            int gridCondition = gameState[x][y];

            if (gridCondition == -1 || gridCondition == -2 || gridCondition == -3) { // is empty or any marked grid
                if (gridCondition == -1) {
                    numMarkedMines++;
                    changeNumMinesDisplay();
                } else if (gridCondition == -2) {
                    numMarkedMines--;
                    changeNumMinesDisplay();
                }
                int nextCondition = switch (gridCondition) {
                    case -1 -> -2;
                    case -2 -> -3;
                    case -3 -> -1;
                    default -> -1;
                };
                gameState[x][y] = nextCondition;
                //update current grid
                sprites[x][y].setState(nextCondition);
            }
        }
    }

    private static void changeNumMinesDisplay() {
        numMineDisplay.setText(Integer.toString(NUM_MINES - numMarkedMines));
    }

    private static boolean exploreGrid(int x, int y) {
        if (!hasLost) {

            if (!inBounds(x, y, BOARD_WIDTH, BOARD_HEIGHT)) {
                return false;
            }
            int gridCondition = gameState[x][y];
            if (gridCondition <= 0) {
                return false;
            }

            if (gridCondition <= countNumFlag(x, y)) {
                //dig all grids that is not marked
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int state = getGridCondition(x + i, y + j);
                        if (state == -1) {
                            digGrid(x + i, y + j);
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static int countNumFlag(int x, int y) {
        int numFlag = 0;

        numFlag += getGridCondition(x - 1, y - 1) == -2 ? 1 : 0;
        numFlag += getGridCondition(x - 1, y) == -2 ? 1 : 0;
        numFlag += getGridCondition(x - 1, y + 1) == -2 ? 1 : 0;

        numFlag += getGridCondition(x, y - 1) == -2 ? 1 : 0;
        numFlag += getGridCondition(x, y + 1) == -2 ? 1 : 0;

        numFlag += getGridCondition(x + 1, y - 1) == -2 ? 1 : 0;
        numFlag += getGridCondition(x + 1, y) == -2 ? 1 : 0;
        numFlag += getGridCondition(x + 1, y + 1) == -2 ? 1 : 0;

        return numFlag;
    }

    private static int getGridCondition(int x, int y) {
        if (x < 0 || y < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT) {
            return -5;
        } else {
            return gameState[x][y];
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

    private static int[] getPosOnGrid(double x, double y) {
        int x2 = (int) (x / GRID_SIZE);
        int y2 = (int) (y / GRID_SIZE);
//        System.out.printf("%d, %d\n", x2, y2);
        if (x2 < 0 || y2 < 0 || x2 >= BOARD_WIDTH || y2 >= BOARD_HEIGHT) {
            return new int[]{-2, -2};
        } else {
            return new int[]{x2, y2};
        }
    }

    private static void releaseGrid(GameGrid[][] gridRef, int x, int y) {
        GameGrid grid = safeReadData2d(gridRef, BOARD_WIDTH, BOARD_HEIGHT, x, y);
        if (grid != null) {
            grid.holdEnded();
        }
    }

    private static void holdGrid(GameGrid[][] gridRef, int x, int y) {
        GameGrid grid = safeReadData2d(gridRef, BOARD_WIDTH, BOARD_HEIGHT, x, y);
        if (grid != null) {
            grid.hold();
        }
    }


    private static boolean[][] generateMines(int width, int height, int exclX, int exclY) {
        /* create initial mines */
        boolean[][] mines = new boolean[width][height];

        List<int[]> possibleSpots = new ArrayList<>(width * height);

        //left
        for (int i = 0; i < exclX - 1; i++) {
            for (int j = 0; j < height; j++) {
                possibleSpots.add(new int[]{i, j});
            }
        }

        //right
        for (int i = exclX + 2; i < width; i++) {
            for (int j = 0; j < height; j++) {
                possibleSpots.add(new int[]{i, j});
            }
        }

        //top
        for (int i = exclX - 1; i < exclX + 2; i++) {
            for (int j = 0; j < exclY - 1; j++) {
                possibleSpots.add(new int[]{i, j});
            }
        }

        //bottom
        for (int i = exclX - 1; i < exclX + 2; i++) {
            for (int j = exclY + 2; j < height; j++) {
                possibleSpots.add(new int[]{i, j});
            }
        }

        //randomly pick spots for mines
        for (int i = 0; i < NUM_MINES; i++) {
            int[] spot = possibleSpots.remove(random(possibleSpots.size()));
            mines[spot[0]][spot[1]] = true;
        }

        return mines;
    }


    private static int[][] calculateAdjacentMines(int width, int height, boolean[][] mines) {
        int[][] temp = new int[width][height];


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (mines[i][j]) { /* the current grid is a mine */
                    temp[i][j] = -2;

                } else {
                    int mineCount = 0;

                    mineCount += safeReadData2d(mines, width, height, i - 1, j - 1) ? 1 : 0;
                    mineCount += safeReadData2d(mines, width, height, i, j - 1) ? 1 : 0;
                    mineCount += safeReadData2d(mines, width, height, i + 1, j - 1) ? 1 : 0;

                    mineCount += safeReadData2d(mines, width, height, i - 1, j) ? 1 : 0;
                    mineCount += safeReadData2d(mines, width, height, i + 1, j) ? 1 : 0;

                    mineCount += safeReadData2d(mines, width, height, i - 1, j + 1) ? 1 : 0;
                    mineCount += safeReadData2d(mines, width, height, i, j + 1) ? 1 : 0;
                    mineCount += safeReadData2d(mines, width, height, i + 1, j + 1) ? 1 : 0;

                    temp[i][j] = mineCount;
                }
            }
        }

        return temp;
    }


    private static Boolean safeReadData2d(boolean[][] data, int width, int height, int x, int y) {
        if (!inBounds(x, y, width, height)) {
            return false;
        } else {
            return data[x][y];
        }
    }

    private static GameGrid safeReadData2d(GameGrid[][] data, int width, int height, int x, int y) {
        if (!inBounds(x, y, width, height)) {
            return null;
        } else {
            return data[x][y];
        }
    }

    private static int[][] createArrayWithInitialValue2d(int x, int y, int init) {
        int[][] temp = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                temp[i][j] = init;
            }
        }
        return temp;
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

    private static int clamp(int val, int min, int max) {
        return Math.min(Math.max(val, min), max);
    }

    private static boolean inBounds(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

}
