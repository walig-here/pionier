package simulation;


import java.util.ArrayList;

/**
 * Rozszerza abstrakcyjną klasę Glitch; Może wyłączyć tymczasowo maszynę.
 */
public class TurnOffGlitch extends Glitch {

    /**
     * Informacja o ilości tur, po których maszyna się ponownie włączy (glitch przestanie działać)
     */
    private int turns_left;

    /**
     * Konstruktor klasy TurnOffGlitch.
     * @param ID id glitcha
     * @param duration czas trwania glitcha
     */
    public TurnOffGlitch(int ID, int duration) {
        super(ID);
        this.turns_left = duration;
    }

    /**
     * Wyłącza tymczasowo maszynę. Maszyna jest włączana, gdy parametr turns_left jest równy 0.
     * @param impacting maszyna, na którą glitch wpływa
     * @param inventory ekwipunek pioniera
     */
    @Override
    public void glitchImpact(Machine impacting, ArrayList<Item> inventory) {

        if(turns_left == 0) {
            if(impacting instanceof ProductionMachine) impacting.startProduction(inventory);
            else impacting.startProduction(inventory);
            super.setGlitch_ended(true);
        }

        else {
            if(impacting instanceof ProductionMachine) impacting.stopProduction(inventory);
            else impacting.stopProduction(inventory);
            turns_left--;
        }
    }
}