package simulation.terrain;

import simulation.Pioneer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Pole emitujące zakłócenie. Wypływa na inne pola w określonej odległości, zwiększając prawodpodobieństwo wystąpienia wnich zakłócenia.
 * Pionier porusza się po nim bardzo wolno.
 */
public class GlitchSourceField extends Field {

    private byte glitch_id; // ID zakłócenia generowanego przez pole
    private int range; // zasięg zakłócenia
    private static int move_cost=-1; // koszt wejścia na pole
    private static byte generated_glitch_probability=-1; // maksymalne, generawane przez źródło prawdopodobieństwo wystąpienia generowanego przez nie zakłócenia
    private static int distance_modifier = -1; // tempo zmniejszania generowanego prawdopodobieństwa zakłóceń wraz z odległością

    // konstruktor
    public GlitchSourceField(int x, int y, int range, byte glitch_id){
        super(x,y,3);

        // ustalamy zasięg pola
        this.range = range;

        // ustalamy id zakłócenia generowanego przez pole
        this.glitch_id = glitch_id;

        // Tworzymy pomocnicze pole ziemi, z którego pobierzemy punkty ruchu potrzebne do wejścia na pole.
        // Jeżeli wartość move_cost jest inna niż -1 to znaczy, że nie musimy już wczytywać tej wartości
        if(move_cost == -1) {
            SoilField temp = new SoilField(0,0);
            move_cost = SoilField.getMove_cost();
        }

        // pobieramy dane z zadanego pliku(jeżeli nie zostały uprzednio załadowane)
        if(distance_modifier == -1 || generated_glitch_probability == -1){
            try{
                InputStream file_stream = new FileInputStream("database/terrain/glitch.txt");
                Scanner file = new Scanner(file_stream);

                while (file.hasNextLine())
                {
                    String line = file.nextLine();
                    Scanner line_scanner = new Scanner(line);
                    line_scanner.useDelimiter(":");
                    line_scanner.next();

                    // linia zawierająca informację o maksymalnym prawdopodobieństwu generowanym przez pole
                    if(line.contains("\"max probability\":") && line_scanner.hasNextInt()) generated_glitch_probability = (byte)line_scanner.nextInt();
                    else if(line.contains("\"distance modifier\":") && line_scanner.hasNextInt()) distance_modifier = line_scanner.nextInt();

                    line_scanner.close();
                }
                file.close();
            }
            // zwracamy wyjątek gdy pliku nie udało się otworzyć
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania danych dla pola źródłowego zakłóceń! Nie udalo sie uzyskac dostepu do pliku z danymi!");
                return;
            }
        }
    }

    // zmienia prawodpodobieństwa wystąpienia zakłócenia z tego pola w innych polach planszy
    public void setProbabilities(Field[][] map) {
        int distance; // dystans między źródłem a analizowanym polem plnaszy

        // Sprawdzamy kolejne pola na planszy
        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[x].length; y++ ){

                // Wyznaczamy odległość danego pola od źródła zakłóceń
                distance = (int)Math.sqrt(Math.pow(coordinates[0]-x,2)+Math.pow(coordinates[1]-y,2));

                // Jeżeli dystans jest większy od zasięgu pola to nie źródło zakłóceń nie ingeruje w pole
                if(distance > range) continue;

                // W przeciwnym wypadku sprawdzamy czy na tym polu już istnieje jakieś prawdopodobieństwo zakłócenia o tym samym ID.
                // Jeżeli takie zakłócenie jeszcze nie ma prawdopodobieństwa na tym polu do musimy tam stworzyć na takie miejsce.
                {
                    boolean exist = false;
                    for(int q = 1; q < map[x][y].getGlitch_probabilities().size(); q++) {
                        if(map[x][y].getGlitch_probabilities().get(q)[0] == glitch_id) {
                            exist = true;
                            break;
                        }
                    }
                    if(!exist) {
                        Byte[] glitch = {glitch_id, 0};
                        map[x][y].getGlitch_probabilities().add(glitch);
                    }
                }

                // W zależności od odległości od źródła nadajemy zakłóceniu w polu odpowiedni poziom prawdopodobieństwa
                byte probability = generated_glitch_probability;

                // Jeżeli dystans jest równy 0 to prawdopodobieństwo wystąpienia zakłócenia jest równe 100%
                if(distance == 0) probability = 100;
                // Jeżeli dystans jest mniejszy od 25% zasięgu to prawdopodobieństwo nie zmieni się
                else if(distance < range/4);
                // Jeżeli dystans jest mniejszy od 50% zasięgu to prawdopodobieństwo zmniejsza się distance_modifier razy
                else if(distance < range/2) probability /= distance_modifier;
                // Jeżeli dystans jest większy lub równy od 75% zasięgu to prawdopodobieństwo zmniejsza się distance_modifier^2 razy
                else if(distance < 3*range/4) probability /= Math.pow(distance_modifier,2);
                // Jeżeli dystans jest od 100% zasięgu to prawdopodobieństwo zmniejsza się distance_modifier^3 razy
                else if(distance < range) probability /= Math.pow(distance_modifier,3);
                // Jeżeli dystans jest równy zasięgowi to prawdopodobieństwo zmniejsza się distance_modifier^4 razy
                else if(distance == range) probability /= Math.pow(distance_modifier,4);

                // Nadajemy polu odpowiednie prawdopodobieństwo
                for(int q = 0; q < map[x][y].getGlitch_probabilities().size(); q++) {
                    if(map[x][y].getGlitch_probabilities().get(q)[0] == glitch_id) {

                        byte prob = (byte) (probability + map[x][y].getGlitch_probabilities().get(q)[1]);
                        map[x][y].getGlitch_probabilities().get(q)[1] = prob;

                        // Jeżeli prawdopodobieństwo przekroczyło w wyniku tych operacji wartość 100 to ustalamy jego wartość na 100
                        if(map[x][y].getGlitch_probabilities().get(q)[1] > 100)
                            map[x][y].getGlitch_probabilities().get(q)[1] = 100;

                        break;
                    }
                }
            }
        }
    }

    /**
     * Odejmuje pionierowi punkty ruchu potrzebne do wjeścia na pole.
     * W wypadku, gdy pionierowi uda się wejść na pole, to w wyniku bardzo silnych zakłóceń traci on wszystkie punkty ruchu.
     *
     * @param pioneer wchodzący na pole pionier
     * */
    @Override
    public boolean goInto(Pioneer pioneer) {
        boolean walked_in;

        // Sprawdzamy czy pionier ma odpowiednią ilość punktów ruchu.
        // Jeżeli nie ma odpowiedniej ilości to pole nie pozwala mu wkroczyć na swój teren.
        if(pioneer.getMove_points() - move_cost < 0) walked_in = false;
        // Jeżeli wystarczy mu punktów ruchu to pole pozwala wkroczyć na swój teren.
        else walked_in = true;

        // Bez względu na to czy pionier wkroczył na pole to traci swoje punkty ruchu
        pioneer.setMove_points(0);
        return walked_in;
    }
}
