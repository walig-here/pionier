package map;


import rendering.NFrame;
import rendering.TargetItemChooser;
import simulation.terrain.*;
import main.*;

import javax.swing.*;
import java.util.Random;
import java.lang.Math;
import java.util.Scanner;

import static main.Main.simulation_setup;

public class MapGenerator {


    public static void generateMap(){
        //generuje losową mapę


        final int maxMapSize=32;
        final int minMapSize=20;

        Random random=new Random();

        int size=random.nextInt(maxMapSize-minMapSize)+minMapSize+1;

        int[][] mapTab=new int[size][size];

        //generator planszy
        int x,y,a,b;

        //generacja ropy
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(2)+1;
        b=random.nextInt(2)+1;
        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=8;
            }
        }

        //generacja złota
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(2)+1;
        b=random.nextInt(2)+1;
        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=7;
            }
        }
        //generacja miedzi
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(3)+1+1;
        b=random.nextInt(3)+1+1;
        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=5;
            }
        }
        //generacja ropy
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(2)+1;
        b=random.nextInt(2)+1;
        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=8;
            }
        }
        //generacja diamentu
        x=random.nextInt(size);
        y=random.nextInt(size);
        mapTab[x][y]=9;

        //generacja żelaza
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(4)+1+2;
        b=random.nextInt(4)+1+2;
        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=4;
            }
        }

        //generacja węgla
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(3)+1+1;
        b=random.nextInt(3)+1+1;
        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=6;
            }
        }

        //generacja lasu
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(6)+1+2;
        b=random.nextInt(6)+1+2;

        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=3;
            }
        }

        //generacja wody
        x=random.nextInt(size);
        y=random.nextInt(size);
        a=random.nextInt(6)+1+2;
        b=random.nextInt(6)+1+2;

        for (int i =x-a;i<=x+a;i++){
            int delta = (int)Math.sqrt(Math.abs((float)b * (float)b * (1.0f - (float)((i-x) * (i-x)) / (float)(a * a))));
            for (int j = y-delta; j<=y+delta;j++){
                if(i>=0&&i<=size-1&&j>=0&&j<=size-1)mapTab[i][j]=1;
            }
        }

        generateMap(size, mapTab);

    }

    public static void generateMap(int size, int[][] mapTab){
        //generuje mapę z edytora

        final int maxDepositSize=500;
        final int minDepositSize=100;
        int maxGlitchRange=size/5;
        if(maxGlitchRange == 0) maxGlitchRange = 10;

        Random random=new Random();

        Main.setMap_size(size);
        Main.setMapTab(mapTab);
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
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 2);
                        break;
                    case 4:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 6);
                        break;
                    case 5:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 3);
                        break;
                    case 6:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 1);
                        break;
                    case 7:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 5);
                        break;
                    case 8:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 4);
                        break;
                    case 9:
                        Main.map[i][j]=new DepositField(i,j, random.nextInt(maxDepositSize)+minDepositSize, 7);
                        break;
                }

            }
        }



        int target_item_id = 0;
        int max_turns=0;
        TargetItemChooser win = new TargetItemChooser(size, target_item_id);
    }
}
