/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetickmeans;
import java.util.Random;

public class Individu implements Comparable{
    int fitness = 0;
    int[] gen;
    int[][] coordinates;
    int genlength;
    
    Individu(int XMAX, int XMIN, int YMAX, int YMIN, int[][] dataset) {
        gen = new int[8];
        Random rand = new Random();
        this.coordinates = dataset;
        for (int i = 0; i < 8; i++) {
            if(i%2 == 0){
                gen[i] = rand.nextInt(XMAX-XMIN);
            } else
                gen[i] = rand.nextInt(YMAX-YMIN);
        }
        setFitness();  
    }

    public int getGenlength() {
        return genlength;
    }

    public void setGenlength(int genlength) {
        this.genlength = genlength;
    }
    
    public int getFitness() {
        return fitness;
    }

    public void setFitness() {
        int M = 10000;
        Fitness fitnessObj = new Fitness(gen, coordinates);
        fitness = M-fitnessObj.distance();
        
    }

    public int getGen(int i) {
        return gen[i];
    }

    public void setGen(int i, int gen) {
        this.gen[i] = gen;
    }
    
//    public String print(){
//        String hasil = "";
//        
//        for (int i = 0; i < gen.length; i++) {
//            hasil += letter[gen[i]];
//        }
//        return (hasil + "---"+fitness);
//    }

    @Override
    public int compareTo(Object o) {
        int fitnessCompare = ((Individu)o).getFitness();//typecast
        return fitnessCompare-this.fitness;
    }
}

