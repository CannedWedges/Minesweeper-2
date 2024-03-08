package org.example.demo2;

@Immutable
public class Transform2 {

    private final double c11;
    private final double c12;
    private final double c21;
    private final double c22;

    public static final Transform2 zero = new Transform2(0, 0, 0, 0);
    public static final Transform2 identity = new Transform2(1, 0, 0, 1);


    public Transform2(Vector2 v1, Vector2 v2) {
        this.c11 = v1.getX();
        this.c12 = v1.getY();

        this.c21 = v2.getX();
        this.c22 = v2.getY();
    }

    public Transform2(double c11, double c12, double c21, double c22) {
        this.c11 = c11;
        this.c12 = c12;

        this.c21 = c21;
        this.c22 = c22;
    }


    public Transform2 add(Transform2 transform2) {
        return new Transform2(
                this.c11 + transform2.c11, this.c12 + transform2.c12,
                this.c21 + transform2.c21, this.c22 + transform2.c22
        );
    }


    public Vector2 transform(Vector2 vector2) {
        return new Vector2(
                vector2.getX() * this.c11 + vector2.getY() * this.c12,
                vector2.getX() * this.c12 + vector2.getY() * this.c22
        );
    }

    public Transform2 transform(Transform2 transform2) {
        return new Transform2(
                this.c11 * transform2.c11 + this.c21 * transform2.c12, this.c12 * transform2.c11 + this.c22 * transform2.c12,
                this.c11 * transform2.c21 + this.c21 * transform2.c22, this.c12 * transform2.c21 + this.c22 * transform2.c22
        );
    }

    public Transform2 rotate(double rad) {
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);
        return new Transform2(
                -sin * c11 + cos * c12,
                cos * c11 + sin * c12,
                -sin * c21 + cos * c22,
                cos * c21 + sin * c22
        );
    }



    public double getC11() {
        return c11;
    }

    public double getC12() {
        return c12;
    }

    public double getC21() {
        return c21;
    }

    public double getC22() {
        return c22;
    }

    @Override
    public String toString() {
        return String.format("Transform: [\n%-5f %-5f\n%-5f %-5f\n]", c11, c12, c21, c22);
    }
}
