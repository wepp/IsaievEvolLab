import interfaces.Deb;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plot {

    private int bound = 1000;
    private double step = 0.001;
    private Deb function;
    private ArrayList<ArrayList<Double>> seeds;
    private double[] x;
    private double[] y;
    private double[][] z;

    private double[] xx;
    private double[] yy;
    private double[] zz;

    public Plot(Deb function, ArrayList<ArrayList<Double>> seeds){
        this.function = function;
        this.seeds = seeds;

        if(seeds.size() == 1){
            this.x = new double[bound];
            this.y = new double[bound];

            int j=0;

            for (double i = 0; i < 1; i+=step, j++){
                x[j] = i;
                ArrayList<Double> tempList = new ArrayList<Double>();
                tempList.add(x[j]);
                y[j] = function.calc(tempList);
            }

            this.xx = new double[seeds.get(0).size()];
            this.yy = new double[seeds.get(0).size()];

            for (int i = 0; i<seeds.get(0).size(); i++){
                xx[i] = seeds.get(0).get(i);
                ArrayList<Double> tempList = new ArrayList<Double>();
                tempList.add(xx[i]);
                yy[i] = function.calc(tempList);
            }
        }
        else if(seeds.size() == 2){
            this.x = new double[bound / 10];
            this.y = new double[bound / 10];
            this.z = new double[bound / 10][bound / 10];

            int iter = 0;
            for (double i = 0; i <= 1 && iter < bound / 10; i += step * 10) {
                x[iter] = i;
                y[iter] = i;
                ++iter;
            }

            for (int i = 0; i < x.length; ++i) {
                for (int j = 0; j < y.length; ++j) {
                    ArrayList<Double> tempList = new ArrayList<Double>();
                    tempList.add(x[i]);
                    tempList.add(y[j]);
                    z[i][j] = (double)Math.round(function.calc(tempList) * 1000d) / 1000d;
                }
            }

            this.xx = new double[seeds.get(1).size()];
            this.yy = new double[seeds.get(1).size()];
            this.zz = new double[seeds.get(1).size()];

            for (int i = 0; i<seeds.get(0).size(); i++){
                //for(int d=0; d<seeds.get(1).size(); d++){
                    xx[i] = seeds.get(0).get(i);
                    yy[i] = seeds.get(1).get(i);
                    ArrayList<Double> tempList = new ArrayList<Double>();
                    tempList.add(xx[i]);
                    tempList.add(yy[i]);
                    zz[i] = function.calc(tempList);
                //}
            }

        }
    }

    private static double[] increment(double start, double step, double end) {
        double range = end - start;
        int steps = (int)(range / step);
        double[] rv = new double[steps];
        for (int i = 0; i<steps; i++) {
            rv[i] = start + ((step / range) * i);
        }
        return rv;
    }

    public void drawGraph(){

        if(seeds.size() == 1){
            // create your PlotPanel (you can use it as a JPanel)
            Plot2DPanel plot2d = new Plot2DPanel();

            // define the legend position
            plot2d.addLegend("SOUTH");

            // add a line plot to the PlotPanel
            plot2d.addLinePlot("function", Color.LIGHT_GRAY, x, y);
            plot2d.addScatterPlot("seeds", xx, yy);

            // put the PlotPanel in a JFrame like a JPanel
            JFrame frame2d = new JFrame("a plot panel");
            frame2d.setSize(600, 600);
            frame2d.setContentPane(plot2d);
            frame2d.setVisible(true);
            frame2d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        else if(seeds.size() == 2){
            // create your PlotPanel (you can use it as a JPanel)
            Plot3DPanel plot3d = new Plot3DPanel();

            // define the legend position
            plot3d.addLegend("SOUTH");

            // add a line plot to the PlotPanel
            plot3d.addGridPlot("function", Color.LIGHT_GRAY, x, y, z);
            plot3d.addScatterPlot("seeds", xx, yy, zz);

            // put the PlotPanel in a JFrame like a JPanel
            JFrame frame3d = new JFrame("a plot panel");
            frame3d.setSize(600, 600);
            frame3d.setContentPane(plot3d);
            frame3d.setVisible(true);
            frame3d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

    }
}
