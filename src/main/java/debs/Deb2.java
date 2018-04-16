package debs;

import interfaces.Deb;

import java.util.ArrayList;

public class Deb2 implements Deb {
    @Override
    public double calc(ArrayList<Double> list) {
        double sum = 0;
        for (Double aXList : list) {
            sum += Math.exp(-2 * Math.log(2) * Math.pow((aXList - 0.1) / 0.8, 2)) * Math.pow(Math.sin(5 * Math.PI * aXList), 6);
        }
        return sum;
    }
}
