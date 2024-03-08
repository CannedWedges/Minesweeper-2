package org.example.demo3;

import java.util.ArrayList;
import java.util.Comparator;

public class SortedList<T> extends ArrayList<T> {

    private final Comparator<T> comparator;

    public SortedList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public boolean add(T elem) {
        int startIndex = 0;
        int endIndex = size();

        int i2 = (startIndex + endIndex) / 2;;
        while (startIndex != endIndex) {


            int res = comparator.compare(elem, get(i2));
            if (res < 0) {
                endIndex = i2;
                i2 = (startIndex + endIndex) / 2;
            } else if (res > 0) {
                startIndex = i2;
                i2 = (startIndex + endIndex) / 2;
            } else {
                add(i2, elem);
                return true;
            }
        }
        add(i2, elem);
        return true;
    }


}
