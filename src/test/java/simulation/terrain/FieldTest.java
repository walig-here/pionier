package simulation.terrain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void activateGlitch() {

        Field[][] map = new Field[10][10];
        for(int x = 0; x < map.length; x++)
            for(int y = 0; y < map[x].length; y++)
                map[x][y] = new SoilField(x,y);

        map[5][5] = new GlitchSourceField(4,4,8, (byte)1);
        ((GlitchSourceField)map[5][5]).setProbabilities(map);

        for(int i = 0; i < 100; i++){
            System.out.println("TURA " + i);
            for(Field[] row : map) {
                for(Field field : row) {
                    field.activateGlitch();
                }
            }
            System.out.println("");
        }
    }
}