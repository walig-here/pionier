package simulation.terrain;

import simulation.Pioneer;

/**
 * Pole emitujące zakłócenie. Wypływa na inne pola w określonej odległości, zwiększając prawodpodobieństwo wystąpienia wnich zakłócenia.
 * Pionier porusza się po nim bardzo wolno.
 */
public class GlitchSourceField extends Field {

    private int glitch_id; // ID zakłócenia generowanego przez pole
    private int range; // zasięg zakłócenia
    private static int move_cost=-1; // koszt wejścia na pole

    // konstruktor
    public GlitchSourceField(int x, int y, int range, int glitch_id){
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
    }

    // zmienia prawodpodobieństwa wystąpienia zakłócenia z tego pola w innych polach
    public void setProbabilities(Field[][] map)
    {

    }

    /**
     * Odejmuje pionierowi punkty ruchu potrzebne do wjeścia na pole.
     * W wypadku, gdy pionierowi uda się wejść na pole, to w wyniku bardzo silnych zakłóceń traci on wszystkie punkty ruchu.
     *
     * @param pioneer wchodzący na pole pionier
     * */
    @Override
    public boolean goInto(Pioneer pioneer)
    {
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
