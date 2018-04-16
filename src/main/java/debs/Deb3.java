package debs;

import interfaces.Deb;

import java.util.ArrayList;

public class Deb3 implements Deb {
    @Override
    public double calc(ArrayList<Double> list) {
        double sum = 0;
        for (Double aXList : list) {
            sum += Math.pow(Math.sin(5 * Math.PI * (Math.pow(aXList, 0.75) - 0.05)), 6);
        }
        return sum / list.size();
    }
}
