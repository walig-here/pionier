package simulation.terrain;


import simulation.Pioneer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

/**
 * Pole zawierające centrum kompleksu przemysłowego, magazyn. Pionier porusza się po nim wolniej. Pionier musi przyjść do centrum, aby
 *  "zebrać" wszystko co będzie mu potrzebne do przyszłej budowy.
 * */
public class CentralField extends Field {

    private static float move_bonus = 0.0f; // bonus do punktów ruchu, nadawany przez pole centralne przy wjeściu na jego teren


    public CentralField(int x, int y){

        super(x,y,4);

        // Wczytujemy z pliku dane o bonusie do punktów ruchu nadawanym przez pole.
        // Jeżeli te informacje zostały już cześniej wczytane do klasy(są różne od 0) to nie musimy ich wczytywać drugi raz.
        if(move_bonus == 0.0f)
        {
            try{
                InputStream file_stream = new FileInputStream("database\\terrain\\central.txt");
                Scanner file = new Scanner(file_stream);

                while (file.hasNextLine())
                {
                    String line = file.nextLine();
                    Scanner line_scanner = new Scanner(line);
                    line_scanner.useDelimiter(":");
                    line_scanner.useLocale(Locale.US);
                    line_scanner.next();

                    // linia zawierająca informację o czasie potrzebnym do przepłynięcia pola
                    if(line.contains("\"move bonus\":") && line_scanner.hasNextFloat()) move_bonus = line_scanner.nextFloat();

                    line_scanner.close();
                }
                file.close();
            }
            // zwracamy wyjątek gdy pliku nie udało się otworzyć
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania danych dla pola centralnego planszy! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            }
        }
    }
    public CentralField(Field transform_into_central){
        this(transform_into_central.coordinates[0], transform_into_central.coordinates[1]);
    }

    /**
     * Zmienia ilość punktów pioniera zgodnie z modyfikatorem move_bonus pola.
     *
     * @param pioneer wchodzący na pole pionier
     * */
    @Override
    public boolean goInto(Pioneer pioneer) {
        // jeżeli pionier nie ma punktów ruchu to nie może wejść na pole
        if(pioneer.getMove_points() == 0) return false;

        // jeżeli pionier ma jeszcze punkty ruchu to wchodzi na pole i odpoczywa przez co regeneruje punkty ruchu
        pioneer.setMove_points((int)Math.floor(pioneer.getMove_points() * move_bonus));
        return true;
    }
}