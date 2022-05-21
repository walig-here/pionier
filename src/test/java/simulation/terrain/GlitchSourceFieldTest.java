package simulation.terrain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlitchSourceFieldTest {

    @Test
    void setProbabilities() {

        Field[][] map = new Field[12][12];
        for(int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++){
                map[x][y] = new SoilField(x,y);
            }
        }
        map[4][4] = new GlitchSourceField(4,4,6,(byte)1);
        if(map[4][4] instanceof GlitchSourceField){
            ((GlitchSourceField)map[4][4]).setProbabilities(map);
        }
        map[8][6] = new GlitchSourceField(8,6,4,(byte)1);
        if(map[8][6] instanceof GlitchSourceField){
            ((GlitchSourceField)map[8][6]).setProbabilities(map);
        }

        System.out.println("MAPA PRAWDOPODOBIENSTW WYSTAPENIA ZAKLOCENIA 1");
        for (Field[] fields : map) {
            for (int y = 0; y < fields.length; y++) {
                if (fields[y].getGlitch_probabilities().size() != 0)
                    System.out.printf("%3d ", fields[y].getGlitch_probabilities().get(0)[1]);
                else System.out.print("  0 ");
            }
            System.out.print("\n");
        }
    }
}