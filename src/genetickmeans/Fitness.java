/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetickmeans;

/**
 *
 * @author ASUS
 */
public class Fitness {
static int[][] centroids;
static int[] allDistance;
static int[][] coordinates;
    
    public Fitness(int[] gen, int[][] coordinates) {
        this.coordinates = coordinates;
        centroids = new int[4][2];
        for (int i = 0; i < 4; i++) {
            centroids[i][0] = gen[i*2];
            centroids[i][1] = gen[i*2+1];
        }
    }
    static int getDistance (int[] centroids,int[] ruspini){
        int distance;
        distance = (int) ((Math.pow((centroids[0]-ruspini[0]), 2)) +
                (Math.pow((centroids[1]-ruspini[1]), 2)));
        distance = (int) Math.sqrt(distance); 
        
        return distance;   
    }
    static int[] getAllDistance(int[] data){     
        allDistance = new int [4];
        for(int i=0; i<allDistance.length; i++){
            allDistance[i] = getDistance(data, centroids[i]);
        }
        return allDistance;
    }
    
    static void print(int[] data){
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i]+",");
            
        }
        System.out.println("");
    }
    
    static int shortest (int i, int j){
        int distance =0;
        
        distance = (int) ((Math.pow((centroids[j][0]-coordinates[i][0]), 2)) +
                (Math.pow((centroids[j][1]-coordinates[i][1]), 2)));
        distance = (int) Math.sqrt(distance); 
        
        return distance;   
    }
    
    static int distance(){
        int totalDistance = 0;
        for(int i=0; i<coordinates.length; i++){
            int tmp[]=getAllDistance(coordinates[i]);
            
            //print(tmp);
            coordinates[i][2] = nearestcentroids(tmp);
            //System.out.println(coordinates[i][2]);
            totalDistance += shortest(i, coordinates[i][2]);
        }
        return totalDistance;
    }
    
    static int nearestcentroids(int[] tmp){
        int pilihan=0;
        int nearest = 999;
        for(int i=0; i<tmp.length; i++){
            if(tmp[i] < nearest ){
                nearest = tmp[i];
                pilihan = i;                
            }
        }
        return pilihan;
    }
}
