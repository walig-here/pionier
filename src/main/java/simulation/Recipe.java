package simulation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Obiekty tej klasy są używane przez ComponentItem; Znajdują się tu informacje o koszcie wyprodukowania produktu i rodzaju (ID) maszyny, w której może być on produkowany.
 */
public class Recipe{

    /**
     * Lista składników potrzebnych do wyprodukowania przedmiotu
     */
    private final ArrayList<Item> input;
    /**
     * Maszyna, w której przedmiot może zostać wyprodukowany
     */
    private int machine;

    /**
     * Konstruktor klasy Recipe.  Pobiera dane (listę składników i ID maszyny) z bazy danych receptur (database/recipes)
     * @param filename nazwa pliku z recepturą (znajdującego się w bazie danych recipe).
     */
    Recipe(String filename){

        // Inicjujemy listę składników
        input = new ArrayList<>();

        // Z bazy danych wczytujemy dane o recepturze
        try{
            InputStream file_stream = new FileInputStream("database/recipes/" + filename);
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

                    // Sprawdzamy, czy można wczytać z pliku ilość przedmiotu;
                    // Jeżeli można to dodajemy składnik do receptury;
                    // Jeżeli nie można, to pomijamy składnik;
                    // Jeśli itemem jest energia, jest tworzony item. Jak cos innego to componentitem;
                    if(line_scanner.hasNextInt() && ID != 0) input.add(new ComponentItem(ID,line_scanner.nextInt(),0));
                    else if(line_scanner.hasNextInt()) input.add(new Item(ID,line_scanner.nextInt(),0));
                }
                // linia zawierająca informację na temat ID maszyny wykonującej tę recepturę
                else if(line.contains("\"machine\":") && line_scanner.hasNextInt()) machine = line_scanner.nextInt();

                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek, gdy pliku nie udało się otworzyć
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla receptury '" + filename + "'! Nie udalo sie uzyskac dostepu do pliku z danymi!");
        }
    }

    public ArrayList<Item> getInput() {
        return input;
    }

    public int getMachine() {
        return machine;
    }
}