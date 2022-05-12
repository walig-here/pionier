package simulation;


public class TurnOffGlitch extends Glitch {

    private int duration; // na ile tur zakłócenie ma wyłączyć maszynę

    public TurnOffGlitch(int range, int intensity, int duration) {
        super(range, intensity);
        this.duration = duration;
    }
}
