package simulation;

public abstract class Glitch {

    private float intensity; // natężenie zakłócenia(w %)
    private int glitchID; // ID glitcha

    public Glitch(int ID, float intensity) {
        this.intensity = intensity;
        glitchID = ID;
    }

    // wpływ glitcha na maszyne
    abstract void glitchImpact(Machine impacting);

    public float getIntensity() {
        return intensity;
    }
}
