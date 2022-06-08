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
        debug_simulation_preview(0);

        // pętla główna
        for (int turn = 0; turn < max_turns; turn++) {

            // Pętla działań wywoływanych co turę na kafelkach planszy
            for (int x = 0; x < map.length; x++) {
                for(int y = 0; y < map[x].length; y++){

                    // Ustalamy czy w tej turze na polu wystąpiło zakłócenie
                    map[x][y].activateGlitch();

                    // Na każdym polu posiadającym maszynę odbywa sie produkcja lub wydobycie
                    if(map[x][y].getMachine() != null){
                        // Dla każdego pola z surowcem preprowadzamy proces wydobycia
                        if(map[x][y] instanceof DepositField) ((DepositField)map[x][y]).extract(pioneer.getInventory());

                        // Każde inne pole z maszyną produkuje przedmioty
                        if(map[x][y].getMachine() instanceof ProductionMachine) ((ProductionMachine)map[x][y].getMachine()).production(buildingQueue, pioneer, map);
                        else map[x][y].getMachine().production(pioneer.getInventory());
                    }
                }
            }

            // Pionier podejmuje decyzję co do kolejnej budowy
            pioneer.setCould_build(true);
            if(pioneer.setNextBuilding(buildingQueue,map) == -1 && buildingQueue.size() != 0) return -1;

            // Pętla ruchu - wykonuje się dopóki pionierowi starcza punktów ruchu lub aż dotrze do celu
            {
                boolean starting = true; // true - jest to pierwszy ruch pioniera w tej turze
                do{
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Przemieszczenie na pole
                    pioneer.walk(map, starting);
                    if(starting) starting = false;

                    // Pionier próbuje na aktualnie zajmowanym polu coś zbudować
                    pioneer.buildMachine(map, buildingQueue);

                    debug_simulation_preview(turn);
                }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }

            // sprawdzamy czy wyprodukowano odpowiedni przedmiot
            for(Item item : pioneer.getInventory()){
                if(item.getID() == targetItem.getID() && item.getAmount() >= 1){
                    for(Field[] row : map){
                        for(Field field : row)
                            if(field.getMachine() != null && field.getMachine().getProduced_item() == item.getID())
                                return 0;
                    }
                }
            }
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
            if(i == 0) new_item = new Item(i,10,0);
            else new_item = new ComponentItem(i,10,0);
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

    private static void debug_simulation_preview(int turns){
        System.out.println("MAPA:");
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){

                //TEREN
                String tile_fx = "";
                if(map[x][y] instanceof WaterField)  tile_fx = "~~~~~";
                else if(map[x][y] instanceof CentralField) tile_fx = "[   ]";
                else if(map[x][y] instanceof GlitchSourceField) tile_fx = "*****";
                else if(map[x][y] instanceof DepositField) tile_fx = "====" + ((DepositField)map[x][y]).getItem_id();
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
        System.out.println("Numer tury: " + turns);
        System.out.println("Ilosc maszyn: " + Machine.count);
        if(buildingQueue.size() != 0) {
            Item i = new Item(buildingQueue.get(0),0,0);
            System.out.println("Pozadany przedmiot: " + i.getName());
        }

        System.out.println("Ekwipunek:");
        for(Item item : pioneer.getInventory()){
            System.out.printf("\t%-25s\t%-5d\t%-5.2f\n", item.getName(), item.getAmount(), item.getIncome());
        }

        System.out.println("Zasoby na mapie:");
        for(int i = 1; i < 9; i++){
            Item item = new Item(i,0,0);
            for(Field[] row : map){
                for(Field field : row){
                    if(field instanceof DepositField && ((DepositField)field).getItem_id() == i){
                        item.setAmount(item.getAmount() + ((DepositField)field).getCapacityOfDeposit());
                        if(field.getMachine() != null && field.getMachine().getActive())
                        item.setIncome(item.getIncome() - field.getMachine().getOutput());
                    }
                }
            }
            System.out.printf("\t%-25s\t%-5d\t%-5.2f\n", item.getName(), item.getAmount(), item.getIncome());
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
