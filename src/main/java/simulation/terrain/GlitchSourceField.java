package simulation.terrain;

import simulation.Pioneer;

/**
 * Pole emitujące zakłócenie. Wypływa na inne pola w określonej odległości, zwiększając prawodpodobieństwo wystąpienia wnich zakłócenia.
 * Pionier porusza się po nim bardzo wolno.
 */
public class GlitchSourceField extends Field {

    private int glitchID; // ID zakłócenia generowanego przez pole
    private int range; // zasięg zakłócenia

    // konstruktor
    public GlitchSourceField(int x, int y){
        super(x,y,3);
    }

    // zmienia prawodpodobieństwa wystąpienia zakłócenia z tego pola w innych polach
    public void setProbabilities(Field[][] map)
    {

    }

    @Override
    public boolean goInto(Pioneer pioneer)
    {
        return true;
    }
}
