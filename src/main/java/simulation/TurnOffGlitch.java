package simulation;


import java.util.ArrayList;

public class TurnOffGlitch extends Glitch {

    private int turns_left; // na ile tur jeszcze zostało do ponownego włączenia maszyny

    public TurnOffGlitch(int ID, int duration) {
        super(ID);
        this.turns_left = duration;

    }

    // wpływ glitcha na maszyne
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
    public int getTurns_left() {
        return turns_left;
    }
}