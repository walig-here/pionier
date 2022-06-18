package simulation;

import java.util.ArrayList;

/**
 * Klasa abstrakcyjna, na podstawie której tworzone są rodzaje zakłóceń maszyn (klasy SlowGlitch, TurnOffGlitch).
 * Zakłócenia emanują z pola zakłóceń. Przeszkadzają w pracy maszynie, do której są przypisane. Mogą je spowolnić lub tymczasowo wyłączyć.
 */
public abstract class Glitch {

    /**
     * ID glitcha
     */
    private final int ID;

    /**
     * Zawiera informacje o tym, czy glitch już się skończył
     */
    private boolean glitch_ended = false;

    /**
     * Konstruktor klasy Glitch.
     * @param ID ID glitcha
     */
    public Glitch(int ID) {
        this.ID = ID;
    }

    /**
     * Wpływa na maszynę
     * @param impacting maszyna, na którą glitch wpływa
     * @param inventory ekwipunek pioniera
     */
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