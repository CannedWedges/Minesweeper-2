package org.example.demo7;

public class GameGrid {

    private final ImageSprite sprite;
    /*
    -1  : undiscovered
    -2  : flagged
    -3  : unknown

    -4  : mine

    0   : no adjacent mines
    1   : 1 adjacent mine
    2...

     */
    private int state = -1;
    private boolean isHovered = false;
    private boolean isHeld = false;

    public GameGrid(double x, double y) {
        sprite = new ImageSprite(25, 25, ImageManager.images[getIndex(state)], x + 12.5, y + 12.5);


    }

    public void setState(int state) {
        this.state = state;
        updateImg();
    }

    public void destroy() {
        sprite.destroy();
    }

    public void hover() {
        isHovered = true;
        updateImg();
    }

    public void hoverEnded() {
        isHovered = false;
        updateImg();
    }

    public void hold() {
        isHeld = true;
        updateImg();
    }

    public void holdEnded() {
        isHeld = false;
        updateImg();
    }

    private void updateImg() {
        if (state >= 0 || state == -4) {
            sprite.img = ImageManager.images[getIndex(state)];
        } else {

            if (isHeld && state == -1) {
                sprite.img = ImageManager.images[getIndex(0)];
            } else {
                sprite.img = ImageManager.images[getIndex(state) + (isHovered ? 12 : 0)];
            }

        }

    }

    private int getIndex(int state) {
        return switch (state) {
            case -1 -> 0;
            case -2 -> 1;
            case -3 -> 2;
            case -4 -> 15;

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
