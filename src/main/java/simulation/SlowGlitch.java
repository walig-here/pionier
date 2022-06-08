package simulation;

import java.util.ArrayList;

public class SlowGlitch extends Glitch {

    private float output_loss; // ile % wydajności zakłócenie odbiera maszynie

    public SlowGlitch(int ID, float output_loss) {
        super(ID);
       this.output_loss = output_loss;
   }

    // wpływ glitcha na maszyne
    @Override
    public void glitchImpact(Machine impacting, ArrayList<Item> inventory) {
        if(impacting instanceof ProductionMachine) ((ProductionMachine)impacting).stopProduction(inventory);
        else impacting.stopProduction(inventory);
        impacting.setOutput((int)Math.floor((float)impacting.getOutput() * (1-output_loss)));
        if(impacting instanceof ProductionMachine) ((ProductionMachine)impacting).startProduction(inventory);
        else impacting.startProduction(inventory);

        setGlitch_ended(true);
    }
}
