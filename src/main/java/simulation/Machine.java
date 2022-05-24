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

        String path = "database\\items\\";
        switch (ID) {
            // 0 - elektrownia
            case 0: path += "powerplant.txt";

            case 1: path += "sawmill.txt";

            case 2: path += "mining_machine.txt";

            case 3: path += "furnace.txt";

            case 4: path += "oil_pump.txt";

            case 5: path += "refinery.txt";
        }
        try {
            Item item = new Item(produced_item, 0, 0);
            //ustalamy jaki przedmiot wylatuje po weryfikacji (oddelegowanie do klasy Item) czy ten przedmiot istnieje
            this.produced_item = produced_item;
        } catch (Exception e) {} ;

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
            System.out.println("Blad wczytywania danych dla przedmiotu o ID " + ID + "! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            return;
        }
    }

        // zmiana ilości przedmitów wynikła z produkcji
    public void production(ArrayList<Item> inventory) {

    }

    // włączenie gitcha w maszynie
    public void activateGlitch(int gitchID) {

    }
}
