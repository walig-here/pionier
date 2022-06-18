package simulation.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.Pioneer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy GlitchSourceField
 */
public class GlitchSourceFieldTest {

    @Test
    void testConstructor() {
        GlitchSourceField testField = new GlitchSourceField(1, 2, 2, (byte) 0);

        Assertions.assertEquals(1, testField.coordinates[0]);
        Assertions.assertEquals(2, testField.coordinates[1]);
        Assertions.assertEquals((byte)0, testField.getGlitch_id());
    }

    @Test
    void testPioneerCannotEntryWithoutMovementPoints() {
        GlitchSourceField glitchSourceField = new GlitchSourceField(1, 2, 2, (byte) 0);
        Pioneer pioneer= new Pioneer(glitchSourceField);
        pioneer.setMove_points(0);
        pioneer.setCoordinates(1, 1);
        Assertions.assertFalse(glitchSourceField.goInto(pioneer));
    }
    @Test
    void testItDecreasesMovementPointsAmountToZero() {
        GlitchSourceField glitchSourceField = new GlitchSourceField(1, 2, 2, (byte) 0);
        Pioneer pioneer= new Pioneer(glitchSourceField);
        pioneer.setMove_points(100);
        Assertions.assertTrue(glitchSourceField.goInto(pioneer));
        Assertions.assertEquals(0, pioneer.getMove_points());

    }

    @Test
    void testItGivesMovementPoints() {
        GlitchSourceField glitchSourceField = new GlitchSourceField(1, 2, 2, (byte) 0);
        Pioneer pioneer= new Pioneer(glitchSourceField);
        pioneer.setMove_points(0);
        glitchSourceField.goOut(pioneer, true);
        //wartosc pobrana z bazy danych
        Assertions.assertEquals(1, pioneer.getMove_points());
    }

    //pokazuje w konsoli mapę zakłóceń
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
        map[8][6] = new GlitchSourceField(8,6,3,(byte)1);
        if(map[8][6] instanceof GlitchSourceField){
            ((GlitchSourceField)map[8][6]).setProbabilities(map);
        }

        System.out.println("MAPA PRAWDOPODOBIENSTW WYSTAPENIA ZAKLOCENIA");
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