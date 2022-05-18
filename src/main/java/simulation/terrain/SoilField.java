package simulation.terrain;

import simulation.Pioneer;

/**
 * Standardowe i najpowszechniejsze pole na planszy.
 * */
public class SoilField extends Field {

    // konstruktor
    public SoilField(int x, int y){
        super(x,y);
    }
    @Override
    public int goThrough(Pioneer pioneer) {
        return 0;
    }
}
