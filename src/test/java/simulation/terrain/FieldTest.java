package simulation.terrain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void activateGlitch() {

        Field[][] map = new Field[5][5];
        for(int x = 0; x < map.length; x++)
            for(int y = 0; y < map[x].length; y++)
                map[x][y] = new SoilField(x,y);

        map[4][4] = new GlitchSourceField(4,4,4, (byte)1);
        ((GlitchSourceField)map[4][4]).setProbabilities(map);

        for(int i = 0; i < 100; i++){
            System.out.println("TURA " + i);
            for(Field[] row : map) {
                for(Field field : row) {
                    field.activateGlitch();
                    System.out.println("Pole (" + field.getCoordinates()[0] + "," + field.getCoordinates()[1] + "): " + field.getMachine().getGlitch());
                }
            }
            System.out.println("");
        }
    }
}