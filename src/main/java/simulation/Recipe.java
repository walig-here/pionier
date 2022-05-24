package simulation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Recipe {

    private ArrayList<Item> input; // lista składników wejściowych

    private int machine; // ID maszyny produkującej

    // konstruktor
    Recipe(String filename){

        // Inicjujemy listę składników
        input = new ArrayList<>();

        // Z bazy danych wczytujemy dane o recepturze
        try{
            InputStream file_stream = new FileInputStream("database\\recipes\\" + filename);
            Scanner file = new Scanner(file_stream);

            while (file.hasNextLine())
            {
                String line = file.nextLine();
                Scanner line_scanner = new Scanner(line);
                line_scanner.useDelimiter(":");
                line_scanner.next();

                // linia zawierająca informację o składniku należącym do receptury
                if(line.contains("\"component\":") && line_scanner.hasNextInt()) {

                    // Wczytujemy ID składnika
                    int ID = line_scanner.nextInt();

                    // Sprawdzamy czy można wczytać z pliku ilość przedmiotu.
                    // Jeżeli można to dodajemy składnik do receptury.
                    // Jeżeli nie można to pomijamy składnik
                    if(line_scanner.hasNextInt()) input.add(new Item(ID,line_scanner.nextInt(),0));
                }
                // linia zawierająca informację na temat ID maszyny wykonującej tę recepturę
                else if(line.contains("\"machine\":") && line_scanner.hasNextInt()) machine = line_scanner.nextInt();

                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek gdy pliku nie udało się otworzyć
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla receptury '" + filename + "'! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            return;
        }
    }


    public ArrayList<Item> getInput() {
        return input;
    }


    public int getMachine() {
        return machine;
    }
}
