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
    static public Pioneer pioneer; // pionier
    static private Item targetItem = new ComponentItem(16, 0, 0); // przedmiot, ktorego zdobycie, konczy symulacje (przedmiot docelowy)
    static private ArrayList<Item> children = new ArrayList<>();
    static private ArrayList<Integer> buildingQueue = new ArrayList<>();

    public static void main(String[] args) {
        
        setBuildingOrder();
        
        menu=new MenuGUI();

    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    public static int simulationLoop(int max_turns) {


        simulation_debug_start(map_size);

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
            if(pioneer.setNextBuilding(buildingQueue,map) == -1) return -1;

            // Pętla ruchu - wykonuje się dopóki pionierowi starcza punktów ruchu lub aż dotrze do celu
            {
                boolean starting = true; // true - jest to pierwszy ruch pioniera w tej turze
                do{
                    try {
                        TimeUnit.MILLISECONDS.sleep(900);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Przemieszczenie na pole
                    pioneer.walk(map, starting);
                    if(starting) starting = false;

                    // Pionier próbuje na aktualnie zajmowanym polu coś zbudować
                    pioneer.buildMachine(map, buildingQueue);

                    debug_simulation_preview();
                }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }

            if(buildingQueue.size() == 0) break;
        }

        if(buildingQueue.size() != 0) return -2;
        return 0;
    }

    private static void simulation_debug_start(int map_size){
        // tymaczasowa mapa i eq
        Random rng = new Random();
        int central_x = rng.nextInt(map_size);
        int central_y = rng.nextInt(map_size);
        map[central_x][central_y] = new CentralField(central_x,central_y);
        pioneer = new Pioneer(map[rng.nextInt(map_size)][rng.nextInt(map_size)]);
        for(int i = 0; i <= 24; i++) {
            Item new_item;
            if(i == 0) new_item = new Item(i,100,0);
            else new_item = new ComponentItem(i,100,0);
            pioneer.getInventory().add(new_item);
        }
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){
                if(map[x][y] instanceof GlitchSourceField)
                    ((GlitchSourceField)map[x][y]).setProbabilities(map);
            }
        }

        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){
                if(map[x][y].getGlitch_probabilities().size() != 0)
                {
                    Byte p = map[x][y].getGlitch_probabilities().get(0)[1];
                    System.out.printf("%-3d ", p.intValue());
                }
                else System.out.printf("0   ");
            }
            System.out.println();
        }
    }

    private static void debug_simulation_preview(){
        System.out.println("MAPA:");
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){

                //TEREN
                String tile_fx = "";
                if(map[x][y] instanceof WaterField)  tile_fx = "~~~~~";
                else if(map[x][y] instanceof CentralField) tile_fx = "[   ]";
                else if(map[x][y] instanceof GlitchSourceField) tile_fx = "*****";
                else if(map[x][y] instanceof DepositField) tile_fx = "=====";
                else tile_fx= "-----";
                char[] tile = tile_fx.toCharArray();

                // PIONIER
                if(x == pioneer.getCoordinates()[0] && y == pioneer.getCoordinates()[1])
                    tile[2] = 'P';

                // ŚCIEŻKA
                for(Integer[] filed : pioneer.getPath()) {
                    if (x == filed[0] && y == filed[1]) {
                        tile[2] = 'x';
                        break;
                    }
                }

                // MASZYNA

                if(map[x][y].getMachine() != null) {
                    Integer machine = map[x][y].getMachine().getID();
                    tile[0] = machine.toString().charAt(0);
                    if(machine > 9) tile[1] = machine.toString().charAt(1);
                }

                // KONIEC
                tile_fx = "";
                for(int i = 0; i < tile.length; i++) tile_fx += tile[i];
                tile_fx += "   ";
                System.out.print(tile_fx);
            }
            System.out.println("");
        }
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
