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

    private int produced_item; // ID produkowanego przedmitou

    private int output; // ilość przedmiotów produkowanych na turę

    private Recipe cost; // lista obiektow potrzebnych do wybudowania/ulepszenia

    private Glitch glitch; // zakłócenie obecne w maszynie

    private int ID; // ID maszyny


    public Machine(int ID, int produced_item) {
        //id podany do szukania w plikach
        this.ID = ID;

        // Resztę danych pobieramy z niezawodnych baz danych na dysku systemowym.
        // Plik z danymi ustalamy na podstawie nazwy maszyny

        String path = "database\\machines\\";
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
                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek gdy pliku nie udało się otworzyć
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla maszyny o ID " + ID + "! Nie udalo sie uzyskac dostepu do pliku z danymi!");
        }
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

        // zmiana ilości przedmitów wynikła z produkcji
    public void production(ArrayList<Item> inventory) {

    }

    public int getID() {
        return ID;
    }

    // włączenie gitcha w maszynie
    public void activateGlitch(int gitchID) {

    }

    public Recipe getCost() {
        return cost;
    }
}
