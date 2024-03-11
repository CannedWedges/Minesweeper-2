package org.example.demo7;

public class Affine3 {
    private static final boolean NORMALISE_DEFAULT = true;

    public double x0;
    public double x1;
    public double x2;

    public double y0;
    public double y1;
    public double y2;


    public Affine3() {
        this.x0 = 0;
        this.x1 = 1;
        this.x2 = 0;

        this.y0 = 0;
        this.y1 = 0;
        this.y2 = 1;
    }


    public Affine3(double x0, double y0, double x1, double y1, double x2, double y2) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;

        this.y0 = y0;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Affine3 add(Vector2 translation) {
        this.x0 += translation.x;
        this.y0 += translation.y;
        return this;
    }



    public Affine3 transform(Affine3 other) {
        return new Affine3(
                other.x0 + this.x0 * other.x1 + this.y0 * other.x2,
                other.y0 + this.x0 * other.y1 + this.y0 * other.y2,

                this.x1 * other.x1 + this.y1 * other.x2,
                this.x1 * other.y1 + this.y1 * other.y2,

                this.x2 * other.x1 + this.y2 * other.x2,
                this.x2 * other.y1 + this.y2 * other.y2
        ).normalise();
    }

    public Affine3 inverse() {
        return new Affine3(

        )
    }


    private Affine3 normalise() {
        if (NORMALISE_DEFAULT) {
            double d1 = Math.pow(x1 * x1 + y1 * y1, .5);
            x1 = x1 / d1;
            y1 = y1 / d1;

            x2 = y1;
            y2 = -x1;
        }
        return this;
    }




    private double[] vectorDecomposition(double x0, double y0, double x1, double y1, double x2, double y2) {
        double det = determinant();
        return new double[] {1, 2};
    }

    private double determinant(double c11, double c12, double c21, double c22) {
        return c11 * c22 - c12 * c21;
    }

}
