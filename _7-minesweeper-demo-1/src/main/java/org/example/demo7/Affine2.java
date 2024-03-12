package org.example.demo7;

public class Affine2 {
    private static final boolean NORMALISE_DEFAULT = true;

    public double x0;
    public double x1;
    public double x2;

    public double y0;
    public double y1;
    public double y2;


    public Affine2() {
        this.x0 = 0;
        this.x1 = 1;
        this.x2 = 0;

        this.y0 = 0;
        this.y1 = 0;
        this.y2 = 1;
    }


    public Affine2(double x0, double y0, double x1, double y1, double x2, double y2) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;

        this.y0 = y0;
        this.y1 = y1;
        this.y2 = y2;
        normalise();
    }

    public Affine2 add(Vector2 translation) {
        this.x0 += translation.x;
        this.y0 += translation.y;
        return this;
    }



    public Affine2 transform(Affine2 other) {
        return new Affine2(
                other.x0 + this.x0 * other.x1 + this.y0 * other.x2,
                other.y0 + this.x0 * other.y1 + this.y0 * other.y2,

                this.x1 * other.x1 + this.y1 * other.x2,
                this.x1 * other.y1 + this.y1 * other.y2,

                this.x2 * other.x1 + this.y2 * other.x2,
                this.x2 * other.y1 + this.y2 * other.y2
        ).normalise();
    }

    public Vector2 transform(Vector2 vec) {
        return new Vector2(
                this.x0 + this.x1 * vec.x + this.x2 * vec.y,
                this.y0 + this.y1 * vec.x + this.y2 * vec.y
        );
    }

    public Affine2 rotation() {
        return new Affine2(
                0, 0,
                x1, y1,
                x2, y2
        );
    }

    public Affine2 inverse() {
        normalise();
        double[] xy = vectorDecomposition(-x0, -y0, x1, y1, x2, y2);
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


    public static void main(String[] args) {
//        PrintHelper.print(vectorDecomposition(2, 3, 5, 0, 1, -.5));
        Affine2 a1 = new Affine2(1,0,3,4,-4,3);
        Affine2 a2 = new Affine2(1,0,3,4,-4,3);
        System.out.println(a2.inverse());
        System.out.println(a1);
        System.out.println(a2.inverse().transform(a1));
    }

    private static double[] vectorDecomposition(double x0, double y0, double x1, double y1, double x2, double y2) {
        double det = determinant(x1, y1, x2, y2);
        return new double[] {determinant(x0, y0, x2, y2) / det, determinant(x1, y1, x0, y0) / det};
    }

    private static double determinant(double c11, double c12, double c21, double c22) {
        return c11 * c22 - c12 * c21;
    }

    @Override
    public String toString() {
        return String.format("Affine2 (        1,        0,        0\n          %-6f, %-6f, %-6f\n          %-6f, %-6f, %-6f)", x0, x1, x2, y0, y1, y2);
    }
}
