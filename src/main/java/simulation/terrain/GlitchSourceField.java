package simulation.terrain;

import simulation.Glitch;
import simulation.Pioneer;

/**
 * Pole emitujące zakłócenie. Wypływa na inne pola w określonej odległości, zwiększając prawodpodobieństwo wystąpienia wnich zakłócenia.
 * Pionier porusza się po nim bardzo wolno.
 */
public class GlitchSourceField extends Field {

    private Glitch glitch; // zakłócenie generowane przez pole

    // konstruktor
    public GlitchSourceField(int x, int y){
        super(x,y);
    }

    // zmienia prawodpodobieństwa wystąpienia zakłócenia z tego pola w innych polach
    public void setProbabilities(Field[][] map)
    {

    }

    @Override
    public int goThrough(Pioneer pioneer)
    {
        return 0;
    }
}
