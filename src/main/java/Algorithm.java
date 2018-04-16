import interfaces.Deb;

import java.util.Map.Entry;

import java.util.*;

public class Algorithm {

    public ArrayList<Entry<Integer, Individ>> getSelectionGroup(HashMap<Integer, Individ> population) {
        Random rg = new Random();
        int realCs = (int) (cs * N);
        ArrayList<Entry<Integer, Individ>> retList = new ArrayList<Entry<Integer, Individ>>();
        List<Integer> keys = new ArrayList<Integer>(population.keySet());

        for (int i = 0; i < realCs; i++) {
            final int key = keys.get(rg.nextInt(keys.size()));
            final Individ ind = population.get(key);
            Entry<Integer, Individ> entryInd = new Entry<Integer, Individ>() {
                public Integer getKey() {
                    return key;
                }

                public Individ getValue() {
                    return ind;
                }

                public Individ setValue(Individ value) {
                    return value;
                }
            };
            retList.add(entryInd);
        }

        return retList;
    }

    public ArrayList<HashMap<Integer, Individ>> getFactorGroups(HashMap<Integer, Individ> population) {
        Random rg = new Random();
        int realS = (int) (s * N);
        ArrayList<HashMap<Integer, Individ>> retList = new ArrayList<HashMap<Integer, Individ>>();
        List<Integer> keys = new ArrayList<Integer>(population.keySet());

        for (int i = 0; i < cf; i++) {
            HashMap<Integer, Individ> tempMap = new HashMap<Integer, Individ>();
            for (int j = 0; j < realS; j++) {
                int key = keys.get(rg.nextInt(keys.size()));
                Individ ind = population.get(key);
                tempMap.put(key, ind);
            }
            retList.add(tempMap);
        }

        return retList;
    }

    public HashMap<Integer, Individ> getSelectionPool(Individ individual, ArrayList<HashMap<Integer, Individ>> factorGroups) {
        HashMap<Integer, Individ> retMap = new HashMap<Integer, Individ>();
        for (HashMap<Integer, Individ> group : factorGroups) {

            Entry<Integer, Individ> mostSimilarEntry = getMostSimilarIndividual(individual,
                    new ArrayList<Entry<Integer, Individ>>(group.entrySet()));
            Individ mostSimilarInd = mostSimilarEntry.getValue();
            int key = mostSimilarEntry.getKey();

            retMap.put(key, mostSimilarInd);
        }

        return retMap;
    }

    public HashMap<Integer, Individ> getSelectionPool2(ArrayList<HashMap<Integer, Individ>> factorGroups) {
        HashMap<Integer, Individ> retMap = new HashMap<Integer, Individ>();
        for (HashMap<Integer, Individ> group : factorGroups) {

//            Entry<Integer, Individ> mostSimilarEntry = getMostSimilarIndividual(individual,
//                    new ArrayList<Entry<Integer, Individ>>(group.entrySet()));
            Entry<Integer, Individ> worstFitnessInd = getIndWithWorstFitness(group);
            //Individ mostSimilarInd = mostSimilarEntry.getValue();
            //int key = mostSimilarEntry.getKey();

            retMap.put(worstFitnessInd.getKey(), worstFitnessInd.getValue());
        }

        return retMap;
    }

    public Entry<Integer, Individ> getIndWithWorstFitness(HashMap<Integer, Individ> individualHashMap) {
        Set<Entry<Integer, Individ>> entries = individualHashMap.entrySet();
        Entry<Integer, Individ> retEntry = (Entry<Integer, Individ>) entries.toArray()[0];

        for (Entry<Integer, Individ> entry : entries) {
            if (entry.getValue().getFitness() < retEntry.getValue().getFitness()) {
                retEntry = entry;
            }
        }

        return retEntry;
    }

    public void generateRandomPopulation() {
        for (int i = 0; i < N; i++) {
            population.put(i, new Individ(dimension, function));
        }
    }

    public Entry<Integer, Individ> getRandomFirstParent(HashMap<Integer, Individ> population) {
        Random rg = new Random();
        ArrayList<Integer> keys = new ArrayList<Integer>(population.keySet());
        final int randomKey = keys.get(rg.nextInt(keys.size()));
        final Individ retInd = population.get(randomKey);

        return new Entry<Integer, Individ>() {
            public Integer getKey() {
                return randomKey;
            }

            public Individ getValue() {
                return retInd;
            }

            public Individ setValue(Individ value) {
                return null;
            }
        };
    }

    public Entry<Integer, Individ> getMostSimilarIndividual(Individ individual, ArrayList<Entry<Integer, Individ>> individuals) {
        Entry<Integer, Individ> retEntry = individuals.get(0);
        double euclidean = euclideanDistance(individual, individuals.get(0).getValue());
        for (int i = 1; i < individuals.size(); i++) {
            double temp = euclideanDistance(individual, individuals.get(i).getValue());
            if (temp > euclidean) {
                euclidean = temp;
                retEntry = individuals.get(i);
            }
        }

        return retEntry;
    }

    private double euclideanDistance(Individ ind1, Individ ind2) {

        double sum = 0;

        for (int i = 0; i < dimension; i++) {
            double temp = ind1.getPhenotype().get(i) - ind2.getPhenotype().get(i);
            sum += temp * temp;
        }

        return Math.pow(sum, 0.5);
    }

    public ArrayList<Entry<Integer, Individ>> cross(final ArrayList<Entry<Integer, Individ>> pair) {

        int m = Algorithm.M;
        Random rg = new Random();
        int n = rg.nextInt(m * dimension);

        ArrayList<Entry<Integer, Individ>> retPair = new ArrayList<Entry<Integer, Individ>>();

        String longString1 = "";
        String longString2 = "";

        for (int i = 0; i < dimension; i++) {
            //System.out.println(pair.get(0).getValue().getChromosome().get(i));
            longString1 += pair.get(0).getValue().getChromosome().get(i);
            longString2 += pair.get(1).getValue().getChromosome().get(i);
        }

        String subA1 = longString1.substring(0, n);
        String subB2 = longString2.substring(n, longString2.length());

        String subB1 = longString2.substring(0, n);
        ;
        String subA2 = longString1.substring(n, longString1.length());

        String finalLongString1 = subA1.concat(subB2);
        String finalLongString2 = subB1.concat(subA2);

        ArrayList<String> chromosomes1 = new ArrayList<String>();
        ArrayList<String> chromosomes2 = new ArrayList<String>();

        for (int i = 0; i < finalLongString1.length(); i = i + m) {
            chromosomes1.add(finalLongString1.substring(i, Math.min(i + m, finalLongString1.length())));
        }

        for (int i = 0; i < finalLongString2.length(); i = i + m) {
            chromosomes2.add(finalLongString2.substring(i, Math.min(i + m, finalLongString2.length())));
        }


        /*********************************/

        final Individ ind1 = new Individ(dimension, chromosomes1, function);
        final Individ ind2 = new Individ(dimension, chromosomes2, function);

        Entry<Integer, Individ> entry1 = new Entry<Integer, Individ>() {
            public Integer getKey() {
                return pair.get(0).getKey();
            }

            public Individ getValue() {
                return ind1;
            }

            public Individ setValue(Individ value) {
                return value;
            }
        };

        Entry<Integer, Individ> entry2 = new Entry<Integer, Individ>() {
            public Integer getKey() {
                return pair.get(1).getKey();
            }

            public Individ getValue() {
                return ind2;
            }

            public Individ setValue(Individ value) {
                return value;
            }
        };

        retPair.add(entry1);
        retPair.add(entry2);

        return retPair;
    }

//    public void mutation(){

//    }

    public double averageFitness(HashMap<Integer, Individ> population) {
        double sum = 0;
        for (int i = 0; i < population.size(); i++) {
            sum += population.get(i).getFitness();
        }
        return sum / population.size();
    }

    public ArrayList<ArrayList<Double>> getSeeds(HashMap<Integer, Individ> population) {
        ArrayList<ArrayList<Double>> retArray = new ArrayList<ArrayList<Double>>();
        ArrayList<Individ> tempArray = new ArrayList<Individ>(population.values());

        Arrays.sort(tempArray.toArray());

        for (int i = 0; i < tempArray.get(0).getPhenotype().size(); i++) {
            ArrayList<Double> tempDouble = new ArrayList<Double>();
            for (int j = 0; j < tempArray.size(); j++) {
                if (!tempDouble.contains(tempArray.get(j).getPhenotype().get(i))) {
                    tempDouble.add(tempArray.get(j).getPhenotype().get(i));
                }
            }

            retArray.add(tempDouble);
        }

        return retArray;
    }

    public static final double A = 0;
    public static final double B = 1;
    public static final int Q = 3;
    public static final int M = 10;
    public static final int BOUND = 1024;

    //private ArrayList<Individ> population;
    private HashMap<Integer, Individ> population;

    private double pc;
    private double pm;
    private double cf;
    private double cs;
    private double s;
    private int N;
    private Deb function;
    private int dimension;

    public Deb getFunction() {
        return function;
    }

    public void setFunction(Deb function) {
        this.function = function;
    }

    public Algorithm() {
        population = new HashMap<Integer, Individ>();
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getPc() {
        return pc;
    }

    public void setPc(double pc) {
        this.pc = pc;
    }

    public double getPm() {
        return pm;
    }

    public void setPm(double pm) {
        this.pm = pm;
    }

    public double getCf() {
        return cf;
    }

    public void setCf(double cf) {
        this.cf = cf;
    }

    public double getCs() {
        return cs;
    }

    public void setCs(double cs) {
        this.cs = cs;
    }

    public double getS() {
        return s;
    }

    public void setS(double s) {
        this.s = s;
    }

    public HashMap<Integer, Individ> getPopulation() {
        return population;
    }

    public void setPopulation(HashMap<Integer, Individ> population) {
        this.population = population;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

}
