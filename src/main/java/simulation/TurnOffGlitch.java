package simulation;


public class TurnOffGlitch extends Glitch {

    static private int duration; // na ile tur zakłócenie ma wyłączyć maszynę
    private int turns_left; // na ile tur jeszcze zostało do ponownego włączenia maszyny

    public TurnOffGlitch(int range, int intensity, int duration) {
        super(range, intensity);
        this.duration = duration;
    }

    // wpływ glitcha na maszyne
    @Override
    void glitchImpact(Machine impacting) {

    }
}
