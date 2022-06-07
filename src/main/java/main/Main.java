package main;

import rendering.*;
import simulation.*;
import simulation.terrain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {

    static private NFrame current_window; // akutalne okno symulacji
    static public int map_size; // rozmiar planszy(w polach) z przedziału [???]
    static public Field[][] map; // plansza na której odbywa się symulacja
    static private MenuGUI menu;
    static private Pioneer pioneer; // pionier
    static private Item targetItem = new ComponentItem(16, 0, 0); // przedmiot, ktorego zdobycie, konczy symulacje (przedmiot docelowy)
    static private ArrayList<Item> children = new ArrayList<>();
    static private ArrayList<Integer> buildingQueue = new ArrayList<>();

    public static void main(String[] args) {
        
        setBuildingOrder();
        
        //menu=new MenuGUI();
        // główna pętla symulacji
        simulationLoop(50);
    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    private static void simulationLoop(int max_turns) {

        // tymaczasowa mapa i eq
        map = new Field[10][10];
        Random rng = new Random();
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){
                int ID = rng.nextInt(3);
                switch (ID){
                    case 0: map[x][y] = new SoilField(x,y); break;
                    case 1: map[x][y] = new WaterField(x,y); break;
                    case 2: map[x][y] = new DepositField(x,y, rng.nextInt(100)+1, rng.nextInt(7)); break;
                    case 3: map[x][y] = new GlitchSourceField(x,y,rng.nextInt(10)+1, (byte)rng.nextInt(1)); break;
                }
            }
        }
        int central_x = rng.nextInt(9);
        int central_y = rng.nextInt(9);
        map[central_x][central_y] = new CentralField(central_x,central_y);
        pioneer = new Pioneer(map[0][0]);
        for(int i = 0; i <= 24; i++) {
            Item new_item;
            if(i == 0) new_item = new Item(i,100,0);
            else new_item = new ComponentItem(i,100,0);
            pioneer.getInventory().add(new_item);
        }


        // wyświetlanie
        debug_simulation_preview();

        // pętla główna
        for (int turn = 0; turn < max_turns; turn++) {

            System.out.println("TURA " + (turn+1));

            // Pętla działań wywoływanych co turę na kafelkach planszy
            for (Field[] row : map) {
                for(Field field : row){

                    // Ustalamy czy w tej turze na polu wystąpiło zakłócenie
                    field.activateGlitch();

                    // Dla każdego pola z surowcem preprowadzamy proces wydobycia
                    if(field instanceof DepositField) ((DepositField)field).extract();
                }
            }

            // Pionier podejmuje decyzję co do kolejnej budowy
            pioneer.setCould_build(true);
            pioneer.setNextBuilding(buildingQueue,map);

            // Pętla ruchu - wykonuje się dopóki pionierowi starcza punktów ruchu lub aż dotrze do celu
            {
                boolean starting = true; // true - jest to pierwszy ruch pioniera w tej turze
                do{
                    // Przemieszczenie na pole
                    pioneer.walk(map, starting);
                    if(starting) starting = false;

                    // Pionier próbuje na aktualnie zajmowanym polu coś zbudować
                    pioneer.buildMachine(map, buildingQueue);
                }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }

            debug_simulation_preview();

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(buildingQueue.size() == 0) break;
        }
    }

    private static void debug_simulation_preview(){
        System.out.println("MAPA:");
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){
                System.out.print("{");

                // CZY ŚCIEŻKA
                for(Integer[] filed : pioneer.getPath()){
                    if(x == filed[0] && y == filed[1]) {
                        System.out.print("#");
                        break;
                    }
                }

                // PIONIER
                if(pioneer.getCoordinates()[0] == x && pioneer.getCoordinates()[1] == y) System.out.print(" @");
                else System.out.print(" ");
                System.out.print("|");

                // MASZYNA
                if(map[x][y].getMachine() != null) System.out.print(" " + map[x][y].getMachine().getID() + "|");
                else System.out.print("  |");

                // GLITCH
                if(map[x][y].getMachine() != null && map[x][y].getMachine().getGlitch() != null) System.out.print(" " + map[x][y].getMachine().getGlitch().getGlitchID() + "|");
                else System.out.print("  |");

                //TEREN
                if(map[x][y] instanceof WaterField) System.out.print("~~");
                else if(map[x][y] instanceof CentralField) System.out.print("CF");
                else if(map[x][y] instanceof GlitchSourceField) System.out.print("*" + ((GlitchSourceField)map[x][y]).getGlitch_id());
                else if(map[x][y] instanceof DepositField) System.out.print("$" + ((DepositField)map[x][y]).getItem_id());
                else System.out.print("__");

                System.out.print("}  ");
            }
            System.out.println("");
        }

        System.out.println("KOLEJKA BUDOWY:");
        for(Integer building : buildingQueue) System.out.print(building + "\t");
        System.out.println();
        System.out.println("ŚCIEŻKA(PR " + pioneer.getMove_points() + "):");
        for(Integer[] filed : pioneer.getPath()) System.out.print("(" + filed[0] + "," + filed[1] + ")\t");
        System.out.println();
    }

    private static void setBuildingOrder() {
        Recipe targetItemRecipe = ((ComponentItem) targetItem).getRecipe();

        ArrayList<Item> relatedItems = getItemChildren(targetItemRecipe);
        relatedItems.add(targetItem);

        for (Item relatedItem : relatedItems) {
            buildingQueue.add(relatedItem.getID());
        }
    }

    //Rekurencyjnie zaczyna od docelowego przedmiotu, "rozwija" jego recepty i tak dochodzi do podstawowych przedmiotów
    private static ArrayList<Item> getItemChildren(Recipe recipe) {
        for (int i=0; i < recipe.getInput().size(); i++) {
            Item childItem = recipe.getInput().get(i);
            if (childItem instanceof ComponentItem) {
                Recipe childItemRecipe = ((ComponentItem) childItem).getRecipe();
                getItemChildren(childItemRecipe);
                children.add(childItem);
            }
        }

        return children;
    }
}
