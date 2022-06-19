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

/**
 * Klasa główna
 */
public class Main {

    /**
     * Rozmiar planszy (w polach)
     */
    static private int map_size;

    /**
     * Plansza, na której symulacja się odbywa (dwuwymiarowa tablica obiektów klasy Field)
     */
    static public Field[][] map;
    static private int[][] mapTab;

    static private MenuGUI menu;

    /**
     * Obiekt pioniera
     */
    static public Pioneer pioneer; // pionier
    /**
     * Przedmiot, którego zdobycie kończy symulację (przedmiot docelowy)
     */
    static public Item targetItem;
    /**
     * Lista (kolejka) ID przedmiotów do zdobycia (na jej podstawie, pionier buduje kolejne budynki)
     */
    static public ArrayList<Integer> buildingQueue = new ArrayList<>();

    /**
     * Tablica zawierająca logi symulacji
     */
    static final private ArrayList<String> log = new ArrayList<>();

    /**
     * Funkcja main — tutaj otwierane jest menu i tworzony jest generator map.
     */
    public static void main(String[] args) {
        MapGenerator generator=new MapGenerator();
        menu=new MenuGUI();
    }

    /**
     * Zapisuje dziennik symulacji w pliku tekstowym — usuwa plik ze starej symulacji, tworzy nowy i zapisuje tam logi.
     */
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

    /**
     * Metoda pomocnicza — dodaje do logów kolejną linijkę
     * @param new_log_line - linia tekstu do zapisania
     */
    public static void addToLog(String new_log_line){
        log.add(new_log_line);
    }

    /**
     * Metoda wywoływana co turę — zapisuje do dziennika symulacji aktualny stan ekwipunku, zasobów na planszy, oraz ilość istniejących, aktywnych i zglitchowanych maszyn
     * @param turn numer aktualnej tury
     */
    private static void turnStatisticsToLog(int turn){
        addToLog("\n\n-------------------------------------------------------------------------------------------");
        addToLog("TURA " + turn + ":");
        addToLog("\tIlo\u015B\u0107 maszyn: " + Machine.count + "\t\tAktywnych: " + Machine.active_machines + "\t\tZ zak\u0142\u00F3ceniami: " + Machine.glitched_machines);
        if(pioneer.getTo_build() != -1) {
            Item i = new Item(pioneer.getTo_build(),0,0);
            addToLog("\tPo\u017C\u0105dany przedmiot: " + i.getName());
        }
        else addToLog("\tPo\u017C\u0105dany przedmiot: brak");
        addToLog("");


        addToLog("\tEkwipunek pioniera:");
;        for(Item eq_item : pioneer.getInventory()) {
            if(eq_item.getName().length() > 16) addToLog("\t\tNazwa: " + eq_item.getName()  +"\tIlo\u015B\u0107: " + eq_item.getAmount() + "\t\tBilans: " + eq_item.getIncome());
            else if(eq_item.getName().length() > 8) addToLog("\t\tNazwa: " + eq_item.getName()  +"\t\tIlo\u015B\u0107: " + eq_item.getAmount() + "\t\tBilans: " + eq_item.getIncome());
            else if(eq_item.getName().length() > 4) addToLog("\t\tNazwa: " + eq_item.getName()  +"\t\t\t\tIlo\u015B\u0107: " + eq_item.getAmount() + "\t\tBilans: " + eq_item.getIncome());
            else addToLog("\t\tNazwa: " + eq_item.getName()  +"\t\t\t\t\tIlo\u015B\u0107: " + eq_item.getAmount() + "\t\tBilans: " + eq_item.getIncome());
        }
        addToLog("");

        addToLog("\tZasoby na planszy:");
        for(int i = 1; i < 8; i++){
            Item item = new Item(i,0,0);
            for(Field[] row : map){
                for(Field field : row){
                    if(field instanceof DepositField && ((DepositField)field).getItem_id() == i){
                        item.setAmount(item.getAmount() + ((DepositField)field).getCapacityOfDeposit());
                        if(field.getMachine() != null && field.getMachine().getActive() == 1 ){
                            if(field.getMachine().getID() < 3 || field.getMachine().getID() == 4 ){
                                if(field.getMachine().getID() == 0) item.setIncome(item.getIncome() - field.getMachine().getOutput()/2);
                                else item.setIncome(item.getIncome() - field.getMachine().getOutput());
                            }
                        }
                    }
                }
            }
            if(item.getName().length() > 16) addToLog("\t\tNazwa: " + item.getName()  +"\tIlo\u015B\u0107: " + item.getAmount() + "\t\tBilans: " + item.getIncome());
            else if(item.getName().length() > 8) addToLog("\t\tNazwa: " + item.getName()  +"\t\tIlo\u015B\u0107: " + item.getAmount() + "\t\tBilans: " + item.getIncome());
            else if(item.getName().length() > 4) addToLog("\t\tNazwa: " + item.getName()  +"\t\t\t\tIlo\u015B\u0107: " + item.getAmount() + "\t\tBilans: " + item.getIncome());
            else addToLog("\t\tNazwa: " + item.getName()  +"\t\t\t\t\tIlo\u015B\u0107: " + item.getAmount() + "\t\tBilans: " + item.getIncome());
        }
        addToLog("");
    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu

    /**
     * Pętla symulacji.
     * W pierwszej turze wybierane jest pole centralne symulacji.
     * Co turę:
     * - na każdym polu, symulacja próbuję wywołać zakłócenie (zostaje one wywołane według procentowej szansy); Jeśli zakłócenie wystąpi, wpływa ono na maszynę;
     * - maszyny produkcyjne wytwarzają przedmioty; Na każdym polu z maszyną wydobywczą (działającą na złożu), wydobywany jest surowiec (ze złoża ubywa surowca, do ekwipunku przybywa);
     * - pętla sprawdza, czy zdobyto już docelowy przedmiot symulacji;
     * - pionier decyduje, co chce zbudować; Następnie, jeśli może, idzie ją zbudować; Informacja o przemieszczaniu się pioniera jest zapisywana w dzienniku symulacji;
     * Jeśli aktualna tura jest równa maksymalnej ilości tur, pionier przegrywa symulację;
     * @param turn - aktualny numer tury
     * @param max_turns - maksymalna liczba tur symulacji, określona w menu, przez użytkownika
     * @return jeśli zwraca -1, to symulacja jest przegrywana ze względu na brak dostępnych miejsc pod budowę kolejnych maszyn;
     * jeśli -2, to symulacja jest przegrywana z powodu osiągnięcia maksymalnej liczby tur symulacji;
     * jeśli -3, to symulacja jest przegrywana z powodu braku możliwości postawienia pola centralnego na mapie
     * jeśli 1 - pionier zdobył przedmiot docelowy i wygrywa symulację
     */
    public static int simulationLoop(int turn, int max_turns) {

        turnStatisticsToLog(turn);

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
                    if (field.getMachine() instanceof ProductionMachine) {
                        if(((ProductionMachine) field.getMachine()).production(buildingQueue, pioneer, map) == -1) return -1;
                    }
                    else field.getMachine().production(pioneer.getInventory());

                    // Sprawdzamy, czy wyprodukowano przedmiot docelowy
                    for(Item item : pioneer.getInventory()){
                        if(item.getID() == targetItem.getID() && item.getAmount() >= 1){
                            if(field.getMachine() != null && field.getMachine().getProduced_item() == item.getID())
                                return 1;
                        }
                    }
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

        // Pętla ruchu — wykonuje się, dopóki pionierowi starcza punktów ruchu lub aż dotrze do celu
        {
            boolean starting = true; // true — jest to pierwszy ruch pioniera w tej turze
            addToLog("\tPionier rozpoczyna marsz z pola (" + pioneer.getCoordinates()[0] + "," + pioneer.getCoordinates()[1] + ").");
            do{

                // Przemieszczenie na pole
                pioneer.walk(map, starting);
                if(starting) starting = false;

                // Pionier próbuje na aktualnie zajmowanym polu coś zbudować
                if(pioneer.buildMachine(map, buildingQueue) == -1) return -1;
            }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            addToLog("\tPionier ko\u0144czy marsz na polu (" + pioneer.getCoordinates()[0] + "," + pioneer.getCoordinates()[1] + ").");
        }

        //debug_simulation_preview(turn);

        if(turn >= max_turns) return -2;
        return 0;
    }

    /**
     * Zlicza punkty uzyskane podczas całej symulacji. Zależą one od ilości posiadanych przedmiotów w ekwipunku i ilości zbudowanych maszyn (za działające maszyny, przyznawana jest premia)
     * @return liczba punktów
     */
    public static int getScore(){
        int score = 0;

        // punkty przyznaje się za ilość przedmiotu w ekwipunku
        for(Item eq_item : pioneer.getInventory())
            score += eq_item.getAmount();

        // punty przyznaje się za każdą maszynę
        score += Machine.count * 10;

        // za działające maszyny otrzymuje się dodatkową premię
        if(Machine.count!=0)score += 1000 * Machine.active_machines/Machine.count;

        return score;
    }

    /**
     * Wywoływana na początku symulacji. Ustalany jest przedmiot docelowy — informacja ta jest zapisywana w dzienniku symulacji.
     * Następnie, zapisujemy do dziennika informację o starcie symulacji i ustalamy kolejkę budowania.
     * Na mapie pojawia się pionier. Do kolejki budowania dodawana jest elektrownia — bez niej, nie da się rozegrać symulacji.
     * Następnie, ustalane są przedmioty startowe (pionier zaczyna symulację z nimi w ekwipunku).
     * Na koniec, na każdym polu zostaje ustalona szansa na wystąpienie zakłócenia. Informacja ta zostaje zapisana w dzienniku symulacji
     * @param target_item_id id przedmiotu docelowego
     * @return jeśli metoda zwraca wartość -1, to symulacja jest przegrywana, ponieważ nie ma dostępnego pola na postawienie pioniera
     */
    public static int simulation_setup(int target_item_id){

        // Ustalamy przedmiot docelowy
        if(target_item_id == 0) targetItem = new Item(target_item_id,0,0);
        else targetItem = new ComponentItem(target_item_id,0,0);
        {
            Item target = new Item(target_item_id, 0,0);
            addToLog("Celem pioniera w tej symulacji jest wyprodukowanie " + target.getName() + ".");
        }

        // Ustalamy kolejkę budowania
        log.add("Symulacja rozpoczyna si\u0119...");
        setBuildingOrder();

        // PIONIER
        if(spawn_pioneer() == -1) return -1;

        // Dodajemy energię na początek kolejki budowania
        buildingQueue.add(0, 0);

        // EKWIPUNEK
        for(int i = 0; i <= 24; i++) {
            Item new_item;
            if(i == 0) new_item = new Item(i,0,0);
            else new_item = new ComponentItem(i,0,0);
            pioneer.getInventory().add(new_item);
        }
        pioneer.getInventory().get(0).setAmount(100);
        pioneer.getInventory().get(19).setAmount(25);
        pioneer.getInventory().get(16).setAmount(50);
        pioneer.getInventory().get(10).setAmount(25);
        pioneer.getInventory().get(9).setAmount(25);
        pioneer.getInventory().get(24).setAmount(25);

        // Ustalenie prawdopodobieństw wystąpienia zakłóceń
        for (Field[] fields : map) {
            for (Field field : fields) {
                if (field instanceof GlitchSourceField)
                    ((GlitchSourceField) field).setProbabilities(map);
            }
        }
        addToLog("Ustalono prawdopodobie\u0144stwa wyst\u0105pienia poszczeg\u00F3lnych zak\u0142\u00F3ce\u0144 na polach.");
        return 0;
    }

    /**
     * Wybiera miejsce, na którym pojawi się pionier. Zostaje losowane miejsce na mapie, do momentu, gdy będzie one odpowiednie (pionier nie może zacząć na wodzie lub polu zakłóceń).
     * Następnie, ustawia pioniera na wybranym polu.
     * @return jeśli zwraca wartość -1, to nie ma odpowiedniego miejsca na postawienie pioniera
     */

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

    /**
     * Wyświetla w konsoli następujące informacje: aktualny numer tury, ilość wszystkich maszyn, ilość aktywnych maszyn, pożądany przedmiot, stan ekwipunku oraz stan zasobów na mapie
     * @param turns aktualny numer tury
     */
    private static void debug_simulation_preview(int turns){
        System.out.println("Numer tury: " + turns);
        System.out.println("Ilosc maszyn: " + Machine.count + "(" + Machine.active_machines + " aktywnych)");
        if(buildingQueue.size() != 0) {
            Item i = new Item(buildingQueue.get(0),0,0);
            System.out.println("Pozadany przedmiot: " + i.getName());
        }


        System.out.println("Ekwipunek:");
        for(Item item : pioneer.getInventory()){
            System.out.printf("\t%-25s\t%-5.2f\t%-5.2f\n", item.getName(), item.getAmount(), item.getIncome());
        }
        System.out.println("Zasoby na mapie:");
        for(int i = 1; i < 9; i++){
            Item item = new Item(i,0,0);
            for(Field[] row : map){
                for(Field field : row){
                    if(field instanceof DepositField && ((DepositField)field).getItem_id() == i){
                        item.setAmount(item.getAmount() + ((DepositField)field).getCapacityOfDeposit());
                        if(field.getMachine() != null && field.getMachine().getActive() == 1 ){
                            if(field.getMachine().getID() < 3 || field.getMachine().getID() == 4 ){
                                if(field.getMachine().getID() == 0) item.setIncome(item.getIncome() - field.getMachine().getOutput()/2);
                                else item.setIncome(item.getIncome() - field.getMachine().getOutput());
                            }
                        }
                    }
                }
            }
            System.out.printf("\t%-25s\t%-5.2f\t%-5.2f\n", item.getName(), item.getAmount(), item.getIncome());
        }
    }

    /**
     * Ustala początkową kolejkę symulacji na podstawie przedmiotu docelowego.
     */
    private static void setBuildingOrder() {
        if(!(targetItem instanceof ComponentItem)) return;

        Recipe targetItemRecipe = ((ComponentItem) targetItem).getRecipe();

        ArrayList<Item> relatedItems = getItemChildren(targetItemRecipe);
        relatedItems.add(targetItem);

        for (Item relatedItem : relatedItems) {
            buildingQueue.add(relatedItem.getID());
        }
    }

    static private ArrayList<Item> children = new ArrayList<>();

    /**
     * Metoda wywoływana w metodzie setBuildingOrder. Rekurencyjnie zaczyna od docelowego przedmiotu, "rozwija" jego recepty i tak dochodzi do podstawowych przedmiotów
     * @param recipe receptura na przedmiot docelowy symulacji
     * @return lista wszystkich przedmiotów biorących udział w produkcji przedmiotu docelowego
     */
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

    public static void setMap_size(int map_size) {
        Main.map_size = map_size;
    }
    public static int getMap_size() {
        return map_size;
    }

    public static void setMapTab(int[][] mapTab) {
        Main.mapTab = mapTab;
    }
}