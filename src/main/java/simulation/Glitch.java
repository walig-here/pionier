package simulation;

public abstract class Glitch {

    private int range; // zasięg zakłócenia
    private float intensity; // natężenie zakłócenia(w %)
    private int glitchID; // ID glitcha

    public Glitch(int range, float intensity) {
        this.range = range;
        this.intensity = intensity;
    }
}
