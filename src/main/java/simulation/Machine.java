package simulation;

import main.Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Prymitywna maszyna, która posiada tylko wyjście.
 */
public class Machine {

    /**
     * ID maszyny
     */
    private final int ID;

    /**
     * Nazwa maszyny, pobierana z bazy danych
     */
    private String name;
    /**
     * ID produkowanego przedmiotu
     */
    protected int produced_item;
    /**
     * Ilość przedmiotów produkowanych na turę, pobierana z bazy danych
     */
    private float output;
    /**
     * Lista przedmiotów potrzebnych do wybudowania maszyny
     */
    private final ArrayList<Item> cost;
    /**
     * Zakłócenie obecne w maszynie
     */
    protected Glitch glitch;
    /**
     * Informacja o ilości tur liczonych od startu produkcji
     */
    protected int production_turn;

    /**
     * Ilość istniejących maszyn
     */
    static public int count = 0;
    /**
     * Ilość aktywnych maszyn
     */
    static public int active_machines = 0;
    /**
     * Ilość zglitchowanych maszyn
     */
    static public int glitched_machines = 0;
    /**
     * Informacja o stanie maszyny (1 - działa; 0 - tymczasowo nie działa; -1 - permanentnie nie działa)
     */
    private int active;



    public void setOutput(float output) {
        this.output = output;
    }
    public void setActive(int active) {
        this.active = active;
    }

    public int getActive() {
        return active;
    }

    /**
     * Konstruktor klasy Machine. Na podstawie ID maszyny, przeszukuje bazę danych i ustawia parametry name, output i cost
     * @param ID ID maszyny, potrzebne do przeszukania bazy danych w poszukiwaniu reszty informacji
     * @param produced_item ID produkowanego przedmiotu
     */
    public Machine(int ID, int produced_item) {
        cost = new ArrayList<>();
        active = 0;
        //id podany do szukania w plikach
        this.ID = ID;

        // Resztę danych pobieramy z niezawodnych baz danych na dysku systemowym.
        // Plik z danymi ustalamy na podstawie nazwy maszyny

        String path = "database/machines/";
        switch (ID) {
            // 0 - elektrownia
            case 0: path += "powerplant.txt"; break;

            case 1: path += "sawmill.txt"; break;

            case 2: path += "mining_machine.txt"; break;

            case 3: path += "furnace.txt"; break;

            case 4: path += "oil_pump.txt"; break;

            case 5: path += "refinery.txt"; break;

            case 6: path += "battery_factory.txt"; break;

            case 7: path += "cable_factory.txt"; break;

            case 8: path += "cpu_factory.txt"; break;

            case 9: path += "diamond_saw_factory.txt"; break;

            case 10: path += "supercomputer_factory.txt"; break;

            case 11: path += "casing_factory.txt"; break;

            case 12: path += "cog_factory.txt"; break;

            case 13: path += "electronic_circuit_factory.txt"; break;

            case 14: path += "engine_factory.txt"; break;

            case 15: path += "production_belt_factory.txt"; break;

        }

        this.produced_item = produced_item;

        try{
            InputStream file_stream = new FileInputStream(path);
            Scanner file = new Scanner(file_stream);
            while (file.hasNextLine())
            {
                String line = file.nextLine();
                Scanner line_scanner = new Scanner(line);
                line_scanner.useDelimiter(":");
                line_scanner.next();

                // linia zawierająca informację o nazwie przedmiotu
                if(line.contains("\"name\":") && line_scanner.hasNext()) name = line_scanner.next();
                    // linia zawierająca informację na temat ilości produkowanych przedmiotów na ture
                else if(line.contains("\"output\":") && line_scanner.hasNextInt()) output = line_scanner.nextInt();

                else if(line.contains("\"cost\":") && line_scanner.hasNextInt()) {
                    // Wczytujemy ID składnika
                    int productID = line_scanner.nextInt();

                    // Sprawdzamy, czy można wczytać z pliku ilość przedmiotu;
                    // Jeżeli można to dodajemy składnik do receptury;
                    // Jeżeli nie można, to pomijamy składnik;
                    // Jeśli itemem jest energia, jest tworzony item. Jak cos innego to componentitem;
                    if(line_scanner.hasNextInt() && productID != 0) cost.add(new ComponentItem(productID,line_scanner.nextInt(),0));
                    else if(line_scanner.hasNextInt()) cost.add(new Item(productID,line_scanner.nextInt(),0));
                }
                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek, gdy pliku nie udało się otworzyć
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla maszyny o ID " + ID + "! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            this.produced_item=-1;
        }
    }
    public Machine(Machine copy){
        this(copy.ID,copy.produced_item);
    }

    public float getOutput() {
        return output;
    }

    public int getProduced_item() {
        return produced_item;
    }

    public Glitch getGlitch() {
        return glitch;
    }

    /**
     * Rozpoczyna produkcję, zwiększa przyrost (income) produktów i ilość aktywnych maszyn.
     * @param inventory ekwipunek pioniera
     */
    public void startProduction(ArrayList<Item> inventory) {
        if(active == 1 || active == -1) return;

        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setIncome((inventoryItem.getIncome() + (double)output/inventoryItem.getProductionTime()));
            break;
        }

        // resetujemy licznik tur produkcyjnych
        production_turn = 0;
        active = 1;
        active_machines++;
    }
    /**
     * Zatrzymuje produkcję, zmniejsza income zatrzymanej maszyny do 0. Na tej podstawie, aktualizuje ekwipunek i ilość aktywnych maszyn.
     * @param inventory ekwipunek pioniera
     */
    public void stopProduction(ArrayList<Item> inventory){
        if(active == 0 || active == -1) return;

        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setIncome((inventoryItem.getIncome() - (double)output/inventoryItem.getProductionTime()));
            break;
        }
        active = 0;
        active_machines--;
    }

    /**
     * Metoda odpowiedzialna za produkcję.
     * Jeśli maszyna nie ma aktywnego glitcha i minęła odpowiednia ilość tur od poprzedniego wyprodukowanego przedmiotu (productionTime) - dodaje wyprodukowany przedmiot do ekwipunku
     * @param inventory ekwipunek pioniera
     */
    public void production(ArrayList<Item> inventory) {
        if(active == 0 || active == -1) return;

        // produkcja trwa kolejną turę
        production_turn++;

        // sprawdzamy, czy minęła już odpowiednia ilość tur, niezbędnych do wyprodukowania przedmiotu
        {
            Item temp = new Item(produced_item, 0,0);

            // jeżeli taka ilość czasu jeszcze nie minęła, to produkcja trwa
            if(temp.getProductionTime() > production_turn) return;

            // jeżeli taka ilość czasu już minęła to resetujemy timer produkcji
            production_turn = 0;
        }

        //przeszukuje ekwipunek w poszukiwaniu itemu produkowanego przez maszyne i zwieksza jego ilsoc
        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setAmount(inventoryItem.getAmount() + output);
            break;
        }
    }

    public int getID() {
        return ID;
    }

    /**
     * Aktywuje podanego glitcha w maszynie. Aktualizuje informację o ilości zglitchowanych maszyn
     * @param glitchID id glitcha
     */
    public void activateGlitch(int glitchID) {
        if(glitchID == 0)
            glitch = new TurnOffGlitch(glitchID, 10);
        else glitch = new SlowGlitch(glitchID, 0.1f);
        glitched_machines++;
    }
    /**
     * Deaktywuje glitcha, dodaje tę informację do logów, aktualizuje informację o ilości zglitchowanych maszyn
     */
    public void deactivateGlitch() {
        Main.addToLog("\tW maszynie "+ name + " problemy spowodowane zak\u0142\u00F3ceniem " + glitch.getID() + " sko\u0144czy\u0142y się.");
        glitch = null;
        glitched_machines--;
    }

    public ArrayList<Item> getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}