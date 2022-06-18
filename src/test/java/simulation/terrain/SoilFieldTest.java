package simulation.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.Pioneer;

import java.beans.FeatureDescriptor;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Testy jednostkowe klasy SoilFieldTest
 */
public class SoilFieldTest {

    @Test
    void constructor(){
        final int x = 0;
        final int y = 0;

        Field soil = new SoilField(x,y);
        Assertions.assertFalse(soil.getCoordinates()[0] != x, "Bledne ustalenie danych standardowego pola planszy. Niepoprawny koordynat x!");
        Assertions.assertFalse(soil.getCoordinates()[1] != y, "Bledne ustalenie danych standardowego pola planszy. Niepoprawny koordynat y!");
        Assertions.assertFalse(soil.getBase_move_points() != 5, "Bledne wczytanie danych standardowego pola planszy. Niepoprawna wartosc bazowych punktow ruchu!");
        Assertions.assertFalse(soil.isCanBuild() != true, "Bledne wczytanie danych standardowego pola planszy. Niepoprawna wartosc wskazujaca zdatnosc pola pod zabudowe!");
    }

    @Test
    void testItDecreasesMovementPointsAmount() {
        SoilField soilField = new SoilField(1, 2);
        Pioneer pioneer= new Pioneer(soilField);
        pioneer.setMove_points(100);
        soilField.goInto(pioneer);
        Assertions.assertEquals(99, pioneer.getMove_points());
    }
    @Test
    void  testPioneerCannotEntryWithoutMovementPoints() {
        SoilField soilField = new SoilField(1, 2);
        Pioneer pioneer= new Pioneer(soilField);
        pioneer.setMove_points(0);
        pioneer.setCoordinates(1, 1);
        Assertions.assertFalse(soilField.goInto(pioneer));
    }

    @Test
    void testItGivesMovementPoints() {
        SoilField soilField = new SoilField(1, 2);
        Pioneer pioneer= new Pioneer(soilField);
        pioneer.setMove_points(0);
        soilField.goOut(pioneer, true);
        //wartosc pobrana z bazy danych
        Assertions.assertEquals(5, pioneer.getMove_points());
    }
}