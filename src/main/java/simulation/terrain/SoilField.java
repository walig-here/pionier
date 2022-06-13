package simulation.terrain;

import simulation.Pioneer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Standardowe i najpowszechniejsze pole na planszy.
 * */
public class SoilField extends Field {

    static private int move_cost = 0; // koszt przemarszu przez pola ziemi(0 do czasu wczytania danych z pliku)

    // konstruktor
    public SoilField(int x, int y){

        super(x,y,0);

        // Wczytujemy z pliku dane o punktach zabieranych przez pole podczas wchodzenia na nie.
        // Jeżeli te informacje zostały już cześniej wczytane do klasy(są różne od 0) to nie musimy ich wczytywać drugi raz.
        if(move_cost == 0) {
            try {
                InputStream file_stream = new FileInputStream("database/terrain/soil.txt");
                Scanner file = new Scanner(file_stream);

                while (file.hasNextLine()) {
                    String line = file.nextLine();
                    Scanner line_scanner = new Scanner(line);
                    line_scanner.useDelimiter(":");
                    line_scanner.next();

                    // linia zawierająca informację o czasie potrzebnym do przepłynięcia pola
                    if (line.contains("\"move cost\":") && line_scanner.hasNextInt())
                        move_cost = line_scanner.nextInt();

                    line_scanner.close();
                }
                file.close();
            }
            // zwracamy wyjątek gdy pliku nie udało się otworzyć
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania danych dla pola ziemnego planszy! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            }
        }
    }

    /**
     * Odbiera pionierowi określoną ilość punktów ruchu przy przejściu przez to pole.
     *
     * @param pioneer wchodzący na pole pionier
     * */
    @Override
    public boolean goInto(Pioneer pioneer) {

        // Sprawdzamy czy pionier ma odpowiednią ilość punktów ruchu.
        // Jeżeli nie ma odpowiedniej ilości to pole odbiera mu wszystkie punkty, ale nie pozwala mu wkroczyć na swój teren.
        if(pioneer.getMove_points() - move_cost < 0)
        {
            pioneer.setMove_points(0);
            return false;
        }

        // Jeżeli wystarczy mu punktów ruchu to pole odbiera mu ich odpowiednią ilosć i pozwala wkroczyć na swój teren.
        pioneer.setMove_points(pioneer.getMove_points() - move_cost);
        return true;
    }

    // pobiera koszt wejścia na pole
    public static int getMove_cost() {
        return move_cost;
    }
}