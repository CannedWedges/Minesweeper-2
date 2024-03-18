package org.example.demo7;

public class Bezier {
    double[] xControl;
    double[] yControl;

    int numControl = 0;
    int maxAng = 10;
    boolean hasEvaluated = false;

    public Bezier(double[] xControl, double[] yControl, int numControl) {
        this.xControl = xControl;
        this.yControl = yControl;
        this.numControl = numControl;
    }


    public double length() {
        return 0;
    }


}
