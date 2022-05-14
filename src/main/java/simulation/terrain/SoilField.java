package simulation.terrain;

import simulation.Pioneer;

/**
 * Standardowe i najpowszechniejsze pole na planszy.
 * */
public class SoilField extends Field {

    @Override
    public int goThrough(Pioneer pioneer) {
        return 0;
    }
}
