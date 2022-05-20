package simulation.terrain;

import simulation.Pioneer;

/**
 * Standardowe i najpowszechniejsze pole na planszy.
 * */
public class SoilField extends Field {

    final private int move_cost = 1; // koszt przemarszu przez pole

    // konstruktor
    public SoilField(int x, int y){
        super(x,y,0);
    }
    @Override
    /**
     * Odbiera pionierowi określoną ilość punktów ruchu przy przejściu przez to pole.
     *
     * @param pioneer wchodzący na pole pionier
     * */
    public boolean goInto(Pioneer pioneer) {

        // Sprawdzamy czy pionier ma odpowiednią ilość punktów ruchu.
        // Jeżeli nie ma odpowiedniej ilości to pole odbiera mu wszystkie punkty, ale nie pozwala mu wkroczyć na swój teren.
        if(pioneer.getMove_points() - move_cost < 0)
        {
            pioneer.setMove_points(0);
            return false;
        }

        // Jeżeli wystarczy mu punktów ruchu to pole odbiera mu ich odpowiednią ilosć i pozwala wkroczyć na swój teren.
        pioneer.setMove_points(pioneer.getMove_points() - move_cost);
        return true;
    }
}
