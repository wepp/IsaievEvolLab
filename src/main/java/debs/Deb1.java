package debs;

import interfaces.Deb;

import java.util.ArrayList;

public class Deb1 implements Deb {
    public double calc(ArrayList<Double> list) {
        double sum = 0;
        for (Double aXList : list) {
            sum += Math.pow(Math.sin(5 * Math.PI * aXList), 6);
        }
        return sum / list.size();
    }
}
