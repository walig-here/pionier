package simulation;

import java.util.ArrayList;

/**
 * Rozszerza abstrakcyjną klasę Glitch. Może spowolnić tymczasowo maszynę.
 */
public class SlowGlitch extends Glitch {

    /**
     * Ile procent wydajności maszyna z tym glitchem traci
     */
    private final float output_loss;

    /**
     * Konstruktor klasy SlowGlitch, rozszerza konstruktor klasy Glitch
     * @param ID id glitcha
     * @param output_loss - ile procent wydajnosci ma stracic maszyna do której glitch zostanie przypisany
     */
    public SlowGlitch(int ID, float output_loss) {
        super(ID);
        this.output_loss = output_loss;
    }

    /**
     * Spowalnia tymczasowo maszynę - odejmuje z ekwipunku pioniera % przedmiotów wynikający z parametru output_loss. Następnie, zmienia parametr glitch_ended na true.
     * @param impacting maszyna, na którą glitch wpływa
     * @param inventory ekwipunek pioniera
     */
    @Override
    public void glitchImpact(Machine impacting, ArrayList<Item> inventory) {
        if(impacting instanceof ProductionMachine) impacting.stopProduction(inventory);
        else impacting.stopProduction(inventory);
        impacting.setOutput((int)Math.floor((float)impacting.getOutput() * (1-output_loss)));
        if(impacting instanceof ProductionMachine) impacting.startProduction(inventory);
        else impacting.startProduction(inventory);

        setGlitch_ended(true);
    }
}