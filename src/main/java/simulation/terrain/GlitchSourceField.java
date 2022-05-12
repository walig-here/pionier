package simulation.terrain;

import simulation.Glitch;

/**
 * Pole emitujące zakłócenie. Wypływa na inne pola w określonej odległości, zwiększając prawodpodobieństwo wystąpienia wnich zakłócenia.
 * Pionier porusza się po nim bardzo wolno.
 */
public class GlitchSourceField extends Field {

    Glitch glitch; // zakłócenie generowane przez pole
}
