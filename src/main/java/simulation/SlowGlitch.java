package simulation;

public class SlowGlitch extends Glitch {

    static private int level_loss; // ile poziomów zakłócenie odbiera maszynie

    public SlowGlitch(int range, int intensity, int level_loss) {
       super(range, intensity);
       this.level_loss = level_loss;
   }

    // wpływ glitcha na maszyne
    @Override
    void glitchImpact(Machine impacting) {

    }
}
