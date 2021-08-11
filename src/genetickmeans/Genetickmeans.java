/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetickmeans;



import graph.SimpleGraph;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.print.Collation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
public class Genetickmeans {
    static public ArrayList<Individu> individual = new ArrayList<>();
    static public ArrayList<Individu> parent = new ArrayList<>();
    static public ArrayList<Individu> offSpring = new ArrayList<>();
    static public ArrayList<Individu> ellitis = new ArrayList<>();
    static int MAX_IND = 20;
    static int current_gen = 0;
    static int lenght = 8;
    static int dataset[][] = new int[75][3];
    static int coordinates[][] = new int[75][3];
    static int XMIN =0; static int XMAX =0;
    static int YMIN =0; static int YMAX =0;
    static SimpleGraph graph = new SimpleGraph();
    static DefaultCategoryDataset defaultdata = new DefaultCategoryDataset();
    
    public static void main(String[] args) {       
        
        graph.setGridSpreadX(10);
        graph.setGridSpreadY(10);
        graph.display();
        System.out.println("Masukkan Jumlah Generasi : ");
        Scanner input = new Scanner(System.in);
        int JUM_GEN = input.nextInt();
        String fileName = "F:\\Kuliah\\Semester 5\\Machine Learning\\genetickmeans\\src\\genetickmeans\\ruspini.csv";
        readFile(fileName);
        coordinatesXYRuspini(dataset);
        range();
        randomIndividual();
        
        for (int i = 0; i < JUM_GEN; i++) {
            selection();
            crossOver();
            mutation();
            ellitism();
            current_gen++;
//            System.out.println("Hasil Generasi "+i+" "+ellitis.get(0).print());
        }        
        result();
    }
    
    static int[][] readFile(String fileName){
        BufferedReader br;
        String line;
        int i=0;
        try{
            br = new BufferedReader(new FileReader(fileName));
            while((line=br.readLine())!=null){ //split sebaris
                String[] ruspini=line.split(",");
                for(int j=0; j<ruspini.length ;j++){
                    dataset[i][j]=Integer.parseInt(ruspini[j]); 
                }
                graph.addPoint(dataset[i][0], dataset[i][1], Color.ORANGE);
                i++;
                }
            } catch (IOException ex) {  
            Logger.getLogger(Genetickmeans.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dataset;
    }
    
    static int[][] coordinatesXYRuspini(int dataset[][]){
        for(int i=0; i<dataset.length; i++){
            for(int j=0; j<dataset.length; j++){
                coordinates[i][0]=dataset[i][0];
                coordinates[i][1]=dataset[i][1];
            }        
        }
        return coordinates;
    }
    //mencari min dan max
    static void range(){
        XMIN = coordinates[0][0];
        XMAX = coordinates[0][0];
        
        for(int i=0; i<coordinates.length; i++){
                if(coordinates[i][0] < XMIN )
                    XMIN = coordinates[i][0];
                else if (coordinates[i][0] > XMAX)
                    XMAX = coordinates[i][0];
            }
        System.out.println("XMIN MAX"+XMIN+" "+XMAX);
        YMIN = coordinates[0][1];
        YMAX = coordinates[0][1];
        
        for(int i=0; i<coordinates.length; i++){
                if(coordinates[i][1] < YMIN )
                    YMIN = coordinates[i][1];
                else if (coordinates[i][1] > YMAX)
                    YMAX = coordinates[i][1];
            }
        
        System.out.println("YMIN MAX"+YMIN+" "+YMAX);
        
        }
    public static void randomIndividual(){
        System.out.println("==Initial Individu==");        
        for (int i = 0; i < MAX_IND; i++) {
            Individu initialIndividu = new Individu(XMAX,XMIN,YMAX,YMIN,dataset);
            individual.add(initialIndividu);   
        }
    }
    
    public static void selection(){
        Random rand = new Random();
        int[] range = new int[MAX_IND];
        System.out.println("==Selection==");
        int totalFitness = 0 ;
        int tmp = 0;
        
        for (int i = 0; i < MAX_IND; i++) {
            totalFitness += individual.get(i).getFitness();
            range[i] = totalFitness;
        }
        System.out.println("Total Fitness : "+totalFitness);
        
        for (int i = 0; i < MAX_IND; i++) {
            tmp = rand.nextInt(totalFitness);
            for (int j = 0; j < individual.size(); j++) {
                if(tmp < range[i]){
                    parent.add(new Individu(XMAX,XMIN,YMAX,YMIN,dataset));
                    Individu parentData = new Individu(XMAX,XMIN,YMAX,YMIN,dataset);
                    for (int k = 0; k < parentData.getGenlength(); k++) {
                        parentData.setGen(k, individual.get(j).getGen(k)); //
                    }
                    parentData.setFitness();
                    parent.add(parentData);
                    break;
                }
            }   
        }      
    }
    
    public static void crossOver(){
        Random r = new Random();      
        int left = 0;
        int right = 0;
        double probCO = 0.96;
        int swap;
        
        int[] tmp = new int[8];
        int[] tmp2 = new int[8];
        
        for (int i = 0; i < parent.size(); i+=2) {
            double p = r.nextDouble();
            for (int j = 0; j < lenght; j++) {
                tmp[j] = parent.get(i).getGen(j);
                tmp2[j] = parent.get(i+1).getGen(j);
            }
            if(p < probCO){
//                left = (rand.nextInt(target.length-2));
//                right = (left+1) + rand.nextInt(target.length-1);
                  left = rad(0, lenght - 2);
                  right = rad(left + 1, lenght - 1);
                for (int x = left; x <= right; x++) {
                    swap = tmp[x];
                    tmp[x] = tmp2[x];
                    tmp2[x] = swap;                          
                }
            }
            //Individu offSpIndividu = new Individu(target);
            offSpring.add(new Individu(XMAX,XMIN,YMAX,YMIN,dataset));
            for (int k = 0; k < lenght; k++) {
                offSpring.get(i).setGen(k, tmp[k]);
            }
            offSpring.get(i).setFitness();
            
            offSpring.add(new Individu(XMAX,XMIN,YMAX,YMIN,dataset));
            for (int k = 0; k < lenght; k++) {
                offSpring.get(i+1).setGen(k, tmp2[k]);
            }
            offSpring.get(i+1).setFitness();
            
//            for (int j = 0; j < target.length; j++) {
//                offSpIndividu.setGen(j, tmp[j]);  
//            }
//            offSpIndividu.setFitness();
//            offSpring.add(offSpIndividu);
//            
//            Individu offSpIndividu2 = new Individu(target);
//            for (int j = 0; j < target.length; j++) {
//                offSpIndividu2.setGen(j, tmp2[j]);  
//            }
//            offSpIndividu2.setFitness();
//            offSpring.add(offSpIndividu2);
        }           
    }
    
    public static void mutation(){
        Random r = new Random();
        Random rand = new Random();
        double min = 0.0;
        double max = 1.0;
        double probMutation = 0.96;
        
        for (int i = 0; i < offSpring.size(); i++) {
            double p = min + (max - min) * r.nextDouble();
            
            if(p < probMutation){
                int operasi = rand.nextInt(2);
                int index = rand.nextInt(lenght);
                
                if(operasi == 0){
                    if(offSpring.get(i).getGen(index) + 1 > 25){
                        offSpring.get(i).setGen(index, 0);
                    }else{
                        offSpring.get(i).setGen(index, offSpring.get(i).getGen(index)+1);
                    }
                    offSpring.get(i).setFitness();
                }else{
                    if(offSpring.get(i).getGen(index) - 1 < 0){
                        offSpring.get(i).setGen(index, 25);
                    }else{
                        offSpring.get(i).setGen(index, offSpring.get(i).getGen(index)-1);
                    }
                    offSpring.get(i).setFitness();
                }
            }      
        }
    }
    public static void ellitism(){
        ellitis.addAll(parent);
        ellitis.addAll(offSpring);
        Collections.sort(ellitis);   
        for (int i = ellitis.size()-1; i > individual.size()-1; i--) {
            ellitis.remove(i);
        }
        individual.removeAll(individual);
        parent.removeAll(parent);
        offSpring.removeAll(offSpring);
        individual.addAll(ellitis);
        defaultdata.setValue(individual.get(0).getFitness(),"best ","gen"+current_gen);
//            Individu newIndividu = new Individu(target);
//            for (int j = 0; j < newIndividu.target.length; j++) {
//                newIndividu.setGen(j, ellitis.get(i).getGen(j));
//            }
//            newIndividu.setFitness();
//            individual.add(newIndividu);
//        }
    }
    
    public static void result(){
        for (int i = 0; i < 4; i++) {
            graph.addPoint(individual.get(0).getGen(2*i), individual.get(0).getGen(2*i+1), Color.RED);
        }
        
        JFreeChart jchart = ChartFactory.createLineChart("Nilai Fitness ", "Generasi ", "fitness", defaultdata, PlotOrientation.VERTICAL,true,true,true);
        CategoryPlot p = jchart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.BLACK);
        
        ChartFrame frame = new ChartFrame("Perubahan Nilai Fitness",jchart);
        frame.setVisible(true);
        frame.setSize(700, 700);
    }
    
    public static int rad(int min, int max){
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }
    
}

