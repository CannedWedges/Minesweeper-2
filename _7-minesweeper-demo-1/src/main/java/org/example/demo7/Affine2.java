package org.example.demo7;

public class Affine2 {
    private static final boolean NORMALISE_DEFAULT = true;

    public double x;
    public double x1;
    public double x2;

    public double y;
    public double y1;
    public double y2;


    public Affine2() {
        this.x = 0;
        this.x1 = 1;
        this.x2 = 0;

        this.y = 0;
        this.y1 = 0;
        this.y2 = 1;
    }


    public Affine2(double x, double y, double x1, double y1, double x2, double y2) {
        this.x = x;
        this.x1 = x1;
        this.x2 = x2;

        this.y = y;
        this.y1 = y1;
        this.y2 = y2;
        normalise();
    }

    public static Affine2 fromAngle(double deg) {
        double rad = Math.toRadians(deg);
        double c1x = Math.cos(rad);
        double c1y = Math.sin(rad);
        return new Affine2(0, 0, c1x, c1y, -c1y, c1x);
    }

    public static Affine2 lookAt(Vector2 v1, Vector2 v2) {
        double dx = (v2.x - v1.x);
        double dy = (v2.y - v1.y);
        double dist = Math.pow(dx * dx + dy * dy, .5);
        double c1x = dx / dist;
        double c1y = dy / dist;
        return new Affine2(v1.x, v1.y, c1x, c1y, -c1y, c1x);
    }

    private static double[] vectorDecomposition(double x0, double y0, double x1, double y1, double x2, double y2) {
        double det = determinant(x1, y1, x2, y2);
        return new double[]{determinant(x0, y0, x2, y2) / det, determinant(x1, y1, x0, y0) / det};
    }

    private static double determinant(double c11, double c12, double c21, double c22) {
        return c11 * c22 - c12 * c21;
    }

    public Affine2 add(Vector2 translation) {
        this.x += translation.x;
        this.y += translation.y;
        return this;
    }

    public Affine2 transform(Affine2 other) {
        return new Affine2(
                other.x + this.x * other.x1 + this.y * other.x2,
                other.y + this.x * other.y1 + this.y * other.y2,

                this.x1 * other.x1 + this.y1 * other.x2,
                this.x1 * other.y1 + this.y1 * other.y2,

                this.x2 * other.x1 + this.y2 * other.x2,
                this.x2 * other.y1 + this.y2 * other.y2
        ).normalise();
    }

    public Vector2 transform(Vector2 vec) {
        return new Vector2(
                this.x + this.x1 * vec.x + this.x2 * vec.y,
                this.y + this.y1 * vec.x + this.y2 * vec.y
        );
    }

    public Affine2 rotation() {
        return new Affine2(
                0, 0,
                x1, y1,
                x2, y2
        );
    }

    public Vector2 translation() {
        return new Vector2(x, y);
    }

    public Affine2 inverse() {
        normalise();
        double[] xy = vectorDecomposition(-x, -y, x1, y1, x2, y2);
        return new Affine2(
                xy[0], xy[1],
                x1, -y1,
                -x2, y2
        ).normalise();
    }

    private Affine2 normalise() {
        if (NORMALISE_DEFAULT) {
            double d1 = Math.pow(x1 * x1 + y1 * y1, .5);
            x1 = x1 / d1;
            y1 = y1 / d1;

            x2 = -y1;
            y2 = x1;
        }
        return this;
    }

    private double toAngle() {
        return Math.toDegrees(Math.atan(y1 / x1));
    }

    @Override
    public String toString() {
        return String.format("Affine2 (        1,        0,        0\n          %-6f, %-6f, %-6f\n          %-6f, %-6f, %-6f)", x, x1, x2, y, y1, y2);
    }
}
