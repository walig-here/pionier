package simulation;

import java.util.ArrayList;

public abstract class Glitch {

    private final int ID;

    private boolean glitch_ended = false;
    public Glitch(int ID) {
        this.ID = ID;
    }

    // wpływ glitcha na maszyne
    abstract public void glitchImpact(Machine impacting, ArrayList<Item> inventory);

    public int getID() {
        return ID;
    }

    public boolean isGlitch_ended() {
        return glitch_ended;
    }

    public void setGlitch_ended(boolean glitch_ended) {
        this.glitch_ended = glitch_ended;
    }
}