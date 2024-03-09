package org.example.minesweeper;

import java.util.Vector;

/*
immutable object
 */
public class Vector2 {
    private final double x;
    private final double y;

    public static final Vector2 zero = new Vector2(0, 0);
    public static final Vector2 identity = new Vector2(1, 1);
    
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 unit() {
        double dist = Math.pow(x * x + y * y, .5);
        return new Vector2(this.x / dist, this.y / dist);
    }



    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 add(double x, double y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 multiply(double d) {
        return new Vector2(this.x * d, this.y * d);
    }

    public Vector2 multiply(Vector2 other) {
        return new Vector2(this.x * other.x, this.y * other.y);
    }

    public double dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
