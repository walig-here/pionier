package map;

import main.Main;
import simulation.terrain.Field;

public class MapGenerator {

    MapGenerator(){

    }
    public static void generateMap(){
        //generuje losową mapę
    }
    public static void generateMap(int size, int[][] mapTab){
        //generuje mapę z edytora
        Main.map_size=size;
        Main.map=new Field[size][size];



    }
}
