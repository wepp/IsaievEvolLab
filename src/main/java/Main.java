import debs.Deb1;
import interfaces.Deb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Main {

    private static int counter = 0;

    private static Deb function = new Deb1();

    private static void updateCounter(double fitness1, double fitness2) {
        if (Math.abs(fitness1 - fitness2) <= 0.0001) {
            counter++;
        } else {
            counter = 0;
        }
    }

    public static void main(String args[]) {
        Algorithm alg = new Algorithm();
        alg.setN(500);


//first params set
        alg.setDimension(1);
        alg.setCf(4);
        alg.setCs(0.15);
        alg.setS(0.01);
        alg.setPc(0.8);

//second params set
//        alg.setDimension(1);
//        alg.setCf(2);
//        alg.setCs(0.15);
//        alg.setS(0.15);
//        alg.setPc(1);

        alg.setFunction(function);
        alg.generateRandomPopulation();
        System.out.println(alg.averageFitness(alg.getPopulation()));
        double tempFitness = 0;
        double currentFitness;
        int j = 0;
        while (counter < 10 && Individ.fitnessCounter < 200000000) {
            HashMap<Integer, Individ> population = (HashMap<Integer, Individ>) alg.getPopulation().clone();
            HashMap<Integer, Individ> newPopulation = new HashMap<Integer, Individ>(); // population after cross and mut
            // Cross and mutation
            for (int k = 0; k < alg.getN() / 2; k++) {
                Entry<Integer, Individ> parent1 = alg.getRandomFirstParent(population);
                population.remove(parent1.getKey());
                ArrayList<Entry<Integer, Individ>> sGroup = alg.getSelectionGroup(population);
                Entry<Integer, Individ> parent2 = alg.getMostSimilarIndividual(parent1.getValue(), sGroup);
                population.remove(parent2.getKey());
                ArrayList<Entry<Integer, Individ>> parentPair = new ArrayList<Entry<Integer, Individ>>();
                parentPair.add(parent1);
                parentPair.add(parent2);
                ArrayList<Entry<Integer, Individ>> afterCross = alg.cross(parentPair);
                newPopulation.put(afterCross.get(0).getKey(), afterCross.get(0).getValue());
                newPopulation.put(afterCross.get(1).getKey(), afterCross.get(1).getValue());

            }
            population = (HashMap<Integer, Individ>) alg.getPopulation().clone();
            for (int i = 0; i < newPopulation.size(); i++) {
                Individ changer = newPopulation.get(i);
                ArrayList<HashMap<Integer, Individ>> factorGroups = alg.getFactorGroups(population);

                // TНагірший з найбільш схожих
                HashMap<Integer, Individ> selectionPool = alg.getSelectionPool(changer, factorGroups);
                Entry<Integer, Individ> worstFromMostSimilar = alg.getIndWithWorstFitness(selectionPool);
                population.put(worstFromMostSimilar.getKey(), changer);

                // Найбільш схожий с гірших
//                HashMap<Integer, Individ> selectionPool2 = alg.getSelectionPool2(factorGroups);
//                Entry<Integer, Individ> mostSimilarFromWorst = alg.getMostSimilarIndividual(changer,
//                        new ArrayList<Entry<Integer, Individ>>(selectionPool2.entrySet()));
//                population.put(mostSimilarFromWorst.getKey(), changer);

            }
            alg.setPopulation(population);
            System.out.println(alg.averageFitness(alg.getPopulation()));
            currentFitness = alg.averageFitness(alg.getPopulation());
            updateCounter(tempFitness, currentFitness);
            tempFitness = currentFitness;
            j++;
        }
        ArrayList<ArrayList<Double>> seeds = alg.getSeeds(alg.getPopulation());
        System.out.println("Individ.fitnessCounter " + Individ.fitnessCounter);
        Plot graphic = new Plot(function, seeds);
        graphic.drawGraph();
    }
}
