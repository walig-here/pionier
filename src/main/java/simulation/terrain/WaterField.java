package simulation.terrain;

import simulation.Pioneer;

/**
 * Pole zalane wodą. Pionier potrzebuje zmarnować n-tur, aby przez nie przejść.
 * */
public class WaterField extends Field {

    private static int sailing_time; // ilość tur potrzebna do przebycia pola wodnego
    private int waiting_round_counter; // ilość tur już przeczekanych

    // konstruktor
    public WaterField(int x, int y){
        super(x,y,1);
    }

    @Override
    public boolean goInto(Pioneer pioneer)
    {
        return true;
    }
}
