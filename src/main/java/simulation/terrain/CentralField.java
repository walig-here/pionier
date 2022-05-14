package simulation.terrain;


import simulation.Pioneer;

/**
 * Pole zawierające centrum kompleksu przemysłowego, magazyn. Pionier porusza się po nim wolniej. Pionier musi przyjść do centrum, aby
 *  "zebrać" wszystko co będzie mu potrzebne do przyszłej budowy.
 * */
public class CentralField extends Field {

    @Override
    public int goThrough(Pioneer pioneer)
    {
        return 0;
    }
}
