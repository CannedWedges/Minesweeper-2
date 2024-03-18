package org.example.demo7;

public class Vector2 {
    public double x = 0;
    public double y = 0;

    public Vector2() {

    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 unit() {
        return new Vector2(1, 1);
    }

    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    public static Vector2 fromPolar(double radius, double angle) {
        double radian = Math.toRadians(angle);
        return new Vector2(radius * Math.sin(radian), radius * Math.cos(radian));
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 addFraction(Vector2 other, double f) {
        this.x += other.x * f;
        this.y += other.y * f;
        return this;
    }

    public Vector2 multiply(double f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public Vector2 multiply(Vector2 other) {
        this.x *= other.x;
        this.y *= other.y;
        return this;
    }

    public double dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }

    @Override
    public String toString() {
        return String.format("Vector2 (%-6f, %-6f)", this.x, this.y);
    }
}
