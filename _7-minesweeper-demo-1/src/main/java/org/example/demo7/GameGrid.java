package org.example.demo7;

public class GameGrid {

    /*
    -1  : undiscovered
    -2  : flagged
    -3  : unknown

    0   : no adjacent mines
    1   : 1 adjacent mine
    2...

     */
    private int state = -1;
    private final ImageSprite sprite;


    public GameGrid(double x, double y) {
        sprite = new ImageSprite(25, 25, ImageManager.images[getIndex(state)], x + 12.5 ,y + 12.5);



    }

    public void setState(int state) {
        this.state = state;
        sprite.img = ImageManager.images[getIndex(state)];
    }

    public void destroy() {
        sprite.destroy();
    }

    private int getIndex(int state) {
        return switch (state) {
            case -1 -> 0;
            case -2 -> 1;
            case -3 -> 2;

            case 0 -> 3;
            case 1 -> 4;
            case 2 -> 5;
            case 3 -> 6;
            case 4 -> 7;
            case 5 -> 8;
            case 6 -> 9;
            case 7 -> 10;
            case 8 -> 11;

            default -> 3;
        };
    }

}
