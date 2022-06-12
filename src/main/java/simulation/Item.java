package simulation;


import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Prymitywny rodzaj przedmiotu, który może zostac pozyskany bez użycia receptury.
 * */
public class Item {
    private final int ID; // ID przedmiotu
    private String name;
    private float amount; // ilość przedmiotu
    private double income; // przyrost na turę
    private int productionTime; //bazowy czas produkcji przedmiotu (w turach)

    public Item(int id, int amount, double income) {

        // Ustalamy ilość tego przedmiotu
        this.amount = amount;

        // Ustalamy ID
        ID = id;

        // Ustalamy bilans
        this.income = income;

        // Resztę danych pobieramy z baz danych na dysku systemowych.
        // Plik z danymi ustalamy na podstawie ID przedmiotu.
        String path = "database/items/";
        switch (id){
            // 0 - energia
            case 0: path += "energy.txt"; break;

            // 1 - węgiel
            case 1: path += "coal.txt"; break;

            // 2 - drewno
            case 2: path += "wood.txt"; break;

            // 3 - miedź
            case 3: path += "copper.txt"; break;

            // 4 - ropa
            case 4: path += "oil.txt"; break;

            // 5 - złoto
            case 5: path += "gold.txt"; break;

            // 6 - żelazo
            case 6: path += "iron.txt"; break;

            // 7 - diament
            case 7: path += "diamond.txt"; break;

            // 8 - benzyna
            case 8: path += "petrol.txt"; break;

            // 9 - sztabka miedzi
            case 9: path += "copper_ingot.txt"; break;

            // 10 - sztabka złota
            case 10: path += "gold_ingot.txt"; break;

            // 11 - stal
            case 11: path += "steel.txt"; break;

            // 12 - plastik
            case 12: path += "plastic.txt"; break;

            // 13 - kwas
            case 13: path += "acid.txt"; break;

            // 14 - kabel
            case 14: path += "cable.txt"; break;

            // 15 - trybik
            case 15: path += "cog.txt"; break;

            // 16 - taśma produkcyjna
            case 16: path += "production_belt.txt"; break;

            // 17 - element obudowy
            case 17: path += "casing.txt"; break;

            // 18 - bateria
            case 18: path += "battery.txt"; break;

            // 19 - silnik
            case 19: path += "engine.txt"; break;

            // 20 - piła diamentowa
            case 20: path += "diamond_saw.txt"; break;

            // 21 - układ elektroniczny
            case 21: path += "electronic_circuit.txt"; break;

            // 22 - procesor
            case 22: path += "cpu.txt"; break;

            // 23 - superkomputer
            case 23: path += "supercomputer.txt"; break;

            // 24 - sztabka żelaza
            case 24: path += "iron_ingot.txt"; break;
        }

        // Pobieramy dane
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
                    // linia zawierająca informację na temat czasu potrzebnego do wyprodukowania przedmiotu
                else if(line.contains("\"production time\":") && line_scanner.hasNextInt()) productionTime = line_scanner.nextInt();

                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek gdy pliku nie udało się otworzyć
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla przedmiotu o ID " + ID + "! Nie udalo sie uzyskac dostepu do pliku z danymi!");
        }
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }
}