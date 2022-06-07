package map;

import main.Main;
import rendering.NFrame;
import simulation.terrain.*;

import java.util.Random;

public class MapGenerator {


    public static void generateMap(){
        //generuje losową mapę

        final int maxMapSize=40;
        final int minMapSize=5;

        Random random=new Random();

        int size=random.nextInt(maxMapSize-minMapSize)+minMapSize+1;

        int[][] mapTab=new int[size][size];

        //generator planszy


        generateMap(size, mapTab);

    }
    public static void generateMap(int size, int[][] mapTab){
        //generuje mapę z edytora

        final int maxDepositSize=200;
        final int minDepositSize=100;
        final int maxGlitchRange=10;

        Random random=new Random();

        Main.map_size=size;
        Main.map=new Field[size][size];

        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){

                switch (mapTab[i][j]){
                    case 0:
                        Main.map[i][j]=new SoilField(i,j);
                        break;
                    case 1:
                        Main.map[i][j]=new WaterField(i,j);
                        break;
                    case 2:
                        Main.map[i][j]=new GlitchSourceField(i,j,random.nextInt(maxGlitchRange),(byte)random.nextInt(2));
                        break;
                    case 3:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 1);
                        Main.map[i][j].setOre_type(1);
                        break;
                    case 4:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 2);
                        Main.map[i][j].setOre_type(2);
                        break;
                    case 5:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 3);
                        Main.map[i][j].setOre_type(3);
                        break;
                    case 6:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 4);
                        Main.map[i][j].setOre_type(4);
                        break;
                    case 7:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 5);
                        Main.map[i][j].setOre_type(5);
                        break;
                    case 8:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 6);
                        Main.map[i][j].setOre_type(6);
                        break;
                    case 9:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize-minDepositSize)+1+minDepositSize, 7);
                        Main.map[i][j].setOre_type(7);
                        break;
                }

            }
        }

        System.out.println("");
        for (int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                System.out.print(Main.map[i][j].getTerrainId());
            }
        }
        //NFrame klatka =new NFrame();
        Main.simulationLoop(100);

    }
}
