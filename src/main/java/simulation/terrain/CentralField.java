package simulation.terrain;


import simulation.Pioneer;

/**
 * Pole zawierające centrum kompleksu przemysłowego, magazyn. Pionier porusza się po nim wolniej. Pionier musi przyjść do centrum, aby
 *  "zebrać" wszystko co będzie mu potrzebne do przyszłej budowy.
 * */
public class CentralField extends Field {

    public CentralField(int x, int y){
        super(x,y,4);
    }
    @Override
    public boolean goInto(Pioneer pioneer)
    {
        return true;
    }
}
