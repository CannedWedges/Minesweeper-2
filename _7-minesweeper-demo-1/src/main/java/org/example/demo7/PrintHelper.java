package org.example.demo7;

public class PrintHelper {

    public static void print(double[] a) {
        if (a.length == 0) {
            System.out.println("Double[0] {}");
        } else {
            StringBuilder stringBuilder = new StringBuilder("Double[").append(a.length).append("] {").append(a[0]);

            for (int i = 1; i < a.length; i++) {
                stringBuilder.append(", ").append(a[i]);
            }
            stringBuilder.append("}");

            System.out.println(stringBuilder);

        }






    }
}
