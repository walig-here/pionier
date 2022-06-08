package main;

import rendering.*;
import simulation.*;
import simulation.terrain.*;
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
        menu=new MenuGUI();
    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    public static int simulationLoop(int max_turns) {


        if(simulation_setup() == -1) return -4;
        debug_simulation_preview(0);

        // pętla główna
        for (int turn = 0; turn < max_turns; turn++) {

            // w pierwszej turze pionier wybiera miejsce pod pole centralne
            if(turn == 0) if(pioneer.chooseCentral(map, buildingQueue) == -1) return -3;

            // Pętla działań wywoływanych co turę na kafelkach planszy
            for (int x = 0; x < map.length; x++) {
                for(int y = 0; y < map[x].length; y++){

                    // Ustalamy czy w tej turze na polu wystąpiło zakłócenie
                    map[x][y].activateGlitch();

                    // Na każdym polu posiadającym maszynę odbywa sie produkcja lub wydobycie
                    if(map[x][y].getMachine() != null){

                        // Dla każdego pola z maszyną, w której wystąpiło zakłócenie, symulujemy wpływ zakłócenia na tę maszynę
                        if(map[x][y].getMachine().getGlitch() != null) {
                            map[x][y].getMachine().getGlitch().glitchImpact(map[x][y].getMachine(),pioneer.getInventory());
                            if(map[x][y].getMachine().getGlitch().isGlitch_ended()) map[x][y].getMachine().deactivateGlitch();
                        }

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
                        TimeUnit.MILLISECONDS.sleep(600);
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

    // funkcja zliczająca punkty zebrane podczas całej symulacji
    public static int getScore(){
        int score = 0;

        // punkty przyznaje się za ilośc przedmiotu w ekwipunku
        for(Item eq_item : pioneer.getInventory())
            score += eq_item.getAmount();

        // punty przyznaje się za każdą maszynę
        score += Machine.count * 10;

        // za działające maszyny otrzymuje się dodatkową premię
        score += 1000 * Machine.active_machines/Machine.count;

        return score;
    }

    private static int simulation_setup(){

        // Ustalamy kolejkę budowania
        setBuildingOrder();

        // PIONIER
        if(spawn_pioneer() == -1) return -1;

        // Dodajemy energię na początek kolejki budowania
        buildingQueue.add(0, 0);

        // EKWIPUNEK
        for(int i = 0; i <= 24; i++) {
            Item new_item;
            if(i == 0) new_item = new Item(i,20,0);
            else new_item = new ComponentItem(i,20,0);
            pioneer.getInventory().add(new_item);
        }

        // Ustalenie prawdopodobieństw wystąpienia zakłóceń
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){
                if(map[x][y] instanceof GlitchSourceField)
                    ((GlitchSourceField)map[x][y]).setProbabilities(map);
            }
        }
        return 0;
    }

    // funkcja wybierająca miejsce spawnu pioniera
    private static int spawn_pioneer(){

        // Tworzymy maszynę losującą
        Random rng = new Random(map_size * targetItem.getID() / 2 - targetItem.getProductionTime());

        // Losujemy kolejne miejsce spawnu, aż do momentu, kiedy pole będzie odopowiednie pod pojawienie się na nim pioniera
        Field spawn_field = new SoilField(0,0);
        int p = 0;
        do {
            p++;
            if(p > map_size * map_size) return -1;
            spawn_field = map[rng.nextInt(map_size)][rng.nextInt(map_size)];
        }while (spawn_field.getTerrainId() == 1 || spawn_field.getTerrainId() == 3);

        // Spawnujemy pioniera
        pioneer = new Pioneer(spawn_field);
        return 0;
    }

    private static void debug_simulation_preview(int turns){
        System.out.println("MAPA:");
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++){

                //TEREN
                String tile_fx = "";
                if(map[x][y] instanceof WaterField)  tile_fx = "~~~~~~";
                else if(map[x][y] instanceof CentralField) tile_fx = "[   ] ";
                else if(map[x][y] instanceof GlitchSourceField) tile_fx = "*****" + ((GlitchSourceField)map[x][y]).getGlitch_id();
                else if(map[x][y] instanceof DepositField) tile_fx = "=====" + ((DepositField)map[x][y]).getItem_id();
                else tile_fx= "------";
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

                    if(map[x][y].getMachine().getGlitch() != null) {
                        Integer i = map[x][y].getMachine().getGlitch().getID();
                        tile[tile.length-2] = i.toString().charAt(0);
                    }
                }

                // KONIEC
                tile_fx = "";
                for(int i = 0; i < tile.length; i++) tile_fx += tile[i];
                tile_fx += "  ";
                System.out.print(tile_fx);
            }
            System.out.println("");
        }
        System.out.println("Numer tury: " + turns);
        System.out.println("Ilosc maszyn: " + Machine.count + "(" + Machine.active_machines + " aktywnych)");
        if(buildingQueue.size() != 0) {
            Item i = new Item(buildingQueue.get(0),0,0);
            System.out.println("Pozadany przedmiot: " + i.getName());
        }


        /*System.out.println("Ekwipunek:");
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
        }*/
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
