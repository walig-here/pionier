package main;

import map.MapGenerator;
import rendering.*;
import simulation.*;
import simulation.terrain.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void setMap_size(int map_size) {
        Main.map_size = map_size;
    }

    public static int getMap_size() {
        return map_size;
    }

    static private int map_size; // rozmiar planszy(w polach)
    static public Field[][] map; // plansza na której odbywa

    public static void setMapTab(int[][] mapTab) {
        Main.mapTab = mapTab;
    }

    static private int[][] mapTab;// się symulacja
    static private MenuGUI menu;
    static public Pioneer pioneer; // pionier
    static final private Item targetItem = new ComponentItem(16, 0, 0); // przedmiot, ktorego zdobycie, konczy symulacje (przedmiot docelowy)
    static private ArrayList<Item> children = new ArrayList<>();
    static private ArrayList<Integer> buildingQueue = new ArrayList<>();
    static final private ArrayList<String> log = new ArrayList<>();

    // funkcja zapisująca dziennik w pliku tekstowym
    public static void saveLog(){

        // usuwamy poprzedni dziennik
        File old_log= new File("simulation_log.txt");
        old_log.delete();

        // otwieramy nowy dziennik
        FileWriter log_file;
        try {
            log_file = new FileWriter("simulation_log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // zapisujemy do niego zawartość
        for(String log_line : log){
            try {
                log_file.write(log_line + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // zamykamy dziennik
        try {
            log_file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // funkcja tworząca wpis do dziennika symulacji
    public static void addToLog(String new_log_line){
        log.add(new_log_line);
    }

    public static void main(String[] args) {
        MapGenerator generator=new MapGenerator();
        menu=new MenuGUI();
    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    public static int simulationLoop(int turn, int max_turns) {

        // pętla główna

            addToLog("\n\nTURA " + turn + ":");

            // w pierwszej turze pionier wybiera miejsce pod pole centralne
            if(turn == 0) if(pioneer.chooseCentral(map, buildingQueue) == -1) return -3;

            // Pętla działań wywoływanych co turę na kafelkach planszy
            for (Field[] fields : map) {
                for (Field field : fields) {

                    // Ustalamy czy w tej turze na polu wystąpiło zakłócenie
                    field.activateGlitch();

                    // Na każdym polu posiadającym maszynę odbywa się produkcja lub wydobycie
                    if (field.getMachine() != null) {

                        // Dla każdego pola z maszyną, w której wystąpiło zakłócenie, symulujemy wpływ zakłócenia na tę maszynę
                        if (field.getMachine().getGlitch() != null) {
                            field.getMachine().getGlitch().glitchImpact(field.getMachine(), pioneer.getInventory());
                            if (field.getMachine().getGlitch().isGlitch_ended())
                                field.getMachine().deactivateGlitch();
                        }

                        // Dla każdego pola z surowcem preprowadzamy proces wydobycia
                        if (field instanceof DepositField)
                            ((DepositField) field).extract(pioneer.getInventory());

                        // Każde inne pole z maszyną produkuje przedmioty
                        if (field.getMachine() instanceof ProductionMachine)
                            ((ProductionMachine) field.getMachine()).production(buildingQueue, pioneer, map);
                        else field.getMachine().production(pioneer.getInventory());
                    }
                }
            }
            addToLog("");

            // Pionier podejmuje decyzję co do kolejnej budowy
            pioneer.setCould_build(true);
            {
                int next_building_output;
                do {
                    next_building_output = pioneer.setNextBuilding(buildingQueue,map);
                }while(next_building_output == 2);
                if (next_building_output == -1 && buildingQueue.size() != 0) return -1;
            }
            addToLog("");

            // Pętla ruchu - wykonuje się dopóki pionierowi starcza punktów ruchu lub aż dotrze do celu
            {
                boolean starting = true; // true - jest to pierwszy ruch pioniera w tej turze
                addToLog("\tPionier rozpoczyna marsz z pola (" + pioneer.getCoordinates()[0] + "," + pioneer.getCoordinates()[1] + ").");
                do{

                    // Przemieszczenie na pole
                    pioneer.walk(map, starting);
                    if(starting) starting = false;

                    // Pionier próbuje na aktualnie zajmowanym polu coś zbudować
                    if(pioneer.buildMachine(map, buildingQueue) == -1) return -1;
                }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
                addToLog("\tPionier kończy marsz na polu (" + pioneer.getCoordinates()[0] + "," + pioneer.getCoordinates()[1] + ").");
            }

            debug_simulation_preview(turn);

            // sprawdzamy czy wyprodukowano odpowiedni przedmiot
            for(Item item : pioneer.getInventory()){
                if(item.getID() == targetItem.getID() && item.getAmount() >= 1){
                    for(Field[] row : map){
                        for(Field field : row)
                            if(field.getMachine() != null && field.getMachine().getProduced_item() == item.getID())
                                return 1;
                    }
                }
            }

        if(turn >= max_turns) return -2;
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
        if(Machine.count!=0)score += 1000 * Machine.active_machines/Machine.count;

        return score;
    }

    public static int simulation_setup(){

        // Ustalamy kolejkę budowania
        log.add("Symulacja rozpoczyna się...");
        setBuildingOrder();

        // PIONIER
        if(spawn_pioneer() == -1) return -1;

        // Dodajemy energię na początek kolejki budowania
        buildingQueue.add(0, 0);

        // EKWIPUNEK
        for(int i = 0; i <= 24; i++) {
            Item new_item;
            if(i == 0) new_item = new Item(i,100,0);
            else new_item = new ComponentItem(i,20,0);
            pioneer.getInventory().add(new_item);
        }

        final int base_prodbelt_amount = 50;
        pioneer.getInventory().get(16).setAmount(base_prodbelt_amount);

        final int base_engines_amount = 17;
        pioneer.getInventory().get(19).setAmount(base_engines_amount);

        // Ustalenie prawdopodobieństw wystąpienia zakłóceń
        for (Field[] fields : map) {
            for (Field field : fields) {
                if (field instanceof GlitchSourceField)
                    ((GlitchSourceField) field).setProbabilities(map);
            }
        }
        addToLog("Ustalono prawdopodobieństwa wystąpienia poszczególnych zakłóceń na polach.");
        return 0;
    }

    // funkcja wybierająca miejsce spawnu pioniera
    private static int spawn_pioneer(){

        // Tworzymy maszynę losującą
        Random rng = new Random(map_size * targetItem.getID() / 2 - targetItem.getProductionTime());

        // Losujemy kolejne miejsce spawnu, aż do momentu, kiedy pole będzie odopowiednie pod pojawienie się na nim pioniera
        Field spawn_field;
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
                String tile_fx;
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
                for (char c : tile) tile_fx += c;
                tile_fx += "  ";
                System.out.print(tile_fx);
            }
            System.out.println();
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
