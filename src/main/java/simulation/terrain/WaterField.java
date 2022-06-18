package simulation.terrain;

import simulation.Pioneer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Dziedziczy po klasie Field. Pole planszy zalane wodą. Pionier musi zmarnować n-tur, aby przez nie przepłynąć.
 */
public class WaterField extends Field {
    /**
     * Ilość tur potrzebna do przebycia pola wodnego, wartość pobierana z bazy danych (database/terrain/water.txt)
     */
    private static int sailing_time = -1;
    /**
     * Ilość tur już przeczekanych na przepłynięcie
     */
    private int waited_rounds;

    /**
     * Konstruktor klasy WaterField. Rozszerza konstruktor klasy Field, dodając informacje o czasie przepływania przez pole wodne (pobrane z bazy danych).
     * @param x współrzędne x pola
     * @param y współrzędne y pola
     */
    public WaterField(int x, int y){

        // Konstruktor rodzica
        super(x,y,1);

        // wyzerowujemy ilość odczekanych tur
        waited_rounds = 0;

        // Wczytujemy z pliku dane o czasie potrzebnym do przepłynięcia pola.
        // Jeżeli te informacje zostały już wcześniej wczytane do klasy (są różne od -1) to nie musimy ich wczytywać drugi raz.
        if(sailing_time == -1)
        {
            try{
                InputStream file_stream = new FileInputStream("database/terrain/water.txt");
                Scanner file = new Scanner(file_stream);

                while (file.hasNextLine())
                {
                    String line = file.nextLine();
                    Scanner line_scanner = new Scanner(line);
                    line_scanner.useDelimiter(":");
                    line_scanner.next();

                    // linia zawierająca informację o czasie potrzebnym do przepłynięcia pola
                    if(line.contains("\"sailing time\":") && line_scanner.hasNextInt()) sailing_time = line_scanner.nextInt();

                    line_scanner.close();
                }
                file.close();
            }
            // zwracamy wyjątek, gdy pliku nie udało się otworzyć
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania danych dla pola wodnego planszy! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            }
        }
    }

    /**
     * Mechanizm przepływania przez pole. Sprawdza, czy pionier odczekał już odpowiednią ilość tur.
     * @param pioneer pionier
     * @return wartość boolean mówiąca o tym, czy pionier może już przejść. Jeśli może - przechodzi.
     */
    @Override
    public boolean goInto(Pioneer pioneer) {
        // Sprawdzamy, czy pionier już odczekał odpowiednią ilość tur na tym polu, aby przejść dalej.
        // Jeżeli tak jest, zerujemy licznik i pozwalamy mu przejść na drugi brzeg.
        if(waited_rounds == sailing_time) {
            waited_rounds = 0;
            pioneer.setMove_points(0);

            // Usuwamy z kolejki pioniera to pole wodne. Dzięki temu od razu przemieści się on na kolejne pole w kolejce (drugi brzeg).
            pioneer.getPath().remove(0);

            return true;
        }

        // W przeciwnym wypadku pole każe pionierowi przeczekać tę turę.
        waited_rounds++;
        pioneer.setMove_points(0);
        return false;
    }

    /**
     * Obsługuje mechanizm wyjścia pioniera z danego pola wodnego.
     * Wykonuje ona funkcję odpowiedzialną za przemieszczanie się przez pole, aby obsłużyć przypadek
     * Gdy pionier wchodzi na pole wodne po przemierzeniu innego pola wodnego. Jeżeli pionier może wyjść z pola,
     * funkcja zwróci wartość true.
     *
     * @param pioneer wchodzący na pole wodne pionier
     * @param starting_point określa czy pole, z którego wychodzimy jest punktym startowym marszu pioniera w tej turze
     *
     * @return Wartość boolowska określająca czy pionier może wyjść z pola wodnego.
     */
    @Override
    public boolean goOut(Pioneer pioneer, boolean starting_point) {
        int[] coords = {pioneer.getPath().get(0)[0], pioneer.getPath().get(0)[1]};
        boolean wentOut = goInto(pioneer);
        if(wentOut) pioneer.setCoordinates(coords[0], coords[1]);
        return false;
    }

    // pobieranie ilości tur potrzebnych do przepłynięcia pola
    public static int getSailing_time() {
        return sailing_time;
    }

}