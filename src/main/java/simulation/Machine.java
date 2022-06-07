package simulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Prymitywna maszyna, która posiada tylko wyjście.
 */
public class Machine {

    private String name; // nazwa

    protected int produced_item; // ID produkowanego przedmiotu

    private int output; // ilość przedmiotów produkowanych na turę

    private ArrayList<Item> cost; // lista obiektów potrzebnych do wybudowania/ulepszenia

    protected Glitch glitch; // zakłócenie obecne w maszynie

    private int ID; // ID maszyny

    protected int production_turn; // ile tur minęło od rozpoczęcia produkcji

    static public int count = 0; // ilość maszyn


    public Machine(int ID, int produced_item) {
        cost = new ArrayList<Item>();
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
        try {
            Item item = new Item(produced_item, 0, 0);
            //ustalamy jaki przedmiot wylatuje po weryfikacji (oddelegowanie do klasy Item) czy ten przedmiot istnieje
            this.produced_item = produced_item;
        } catch (Exception e) {}

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
                // linia zawierająca informację na temat ilosci produkowanych przedmiotow na ture
                else if(line.contains("\"output\":") && line_scanner.hasNextInt()) output = line_scanner.nextInt();

                else if(line.contains("\"cost\":") && line_scanner.hasNextInt()) {
                    // Wczytujemy ID składnika
                    int productID = line_scanner.nextInt();

                    // Sprawdzamy czy można wczytać z pliku ilość przedmiotu.
                    // Jeżeli można to dodajemy składnik do receptury.
                    // Jeżeli nie można to pomijamy składnik
                    // Jeśli itemem jest energia, jest tworzony item. Jak cos innego to componentitem
                    if(line_scanner.hasNextInt() && productID != 0) cost.add(new ComponentItem(productID,line_scanner.nextInt(),0));
                    else if(line_scanner.hasNextInt()) cost.add(new Item(productID,line_scanner.nextInt(),0));
                }
                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek gdy pliku nie udało się otworzyć
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla maszyny o ID " + ID + "! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            this.produced_item=-1;
        }
    }
    public Machine(Machine copy){
        this(copy.ID,copy.produced_item);
    }

    public int getOutput() {
        return output;
    }

    public int getProduced_item() {
        return produced_item;
    }

    public Glitch getGlitch() {
        return glitch;
    }

    //rozpoczyna produkcję, zwieksza income produktow
    public void startProduction(ArrayList<Item> inventory) {
        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setIncome((inventoryItem.getIncome() + (double)output/inventoryItem.getProductionTime()));
            break;
        }

        // resetujemy licznik tur produkcyjnych
        production_turn = 0;
    }

        // zmiana ilości przedmitów wynikła z produkcji
    public void production(ArrayList<Item> inventory) {
        if(glitch instanceof TurnOffGlitch) {
            return;
        }

        // produkcja trwa kolejną turę
        production_turn++;

        // sprawdzamy czy minęła już odpowiednia ilość tur, niezbędnych do wyprodukowania przedmiotu
        {
            Item temp = new Item(produced_item, 0,0);

            // jeżeli taka ilość czasu jeszcze nie minęła to produkcja trwa dalej
            if(temp.getProductionTime() > production_turn) return;

            // jeżeli taka ilość czasu już minęła to resetujemy timer produkcji
            production_turn = 0;
        }

        //przeszukuje ekwipunek w poszukiwaniu itemu produkowanego przez maszyne i zwieksza jego ilsoc
        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setAmount(inventoryItem.getAmount() + output/inventoryItem.getProductionTime());
            break;
        }
    }

    public int getID() {
        return ID;
    }

    // włączenie gitcha w maszynie
    public void activateGlitch(int gitchID) {

    }

    public ArrayList<Item> getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }
}
