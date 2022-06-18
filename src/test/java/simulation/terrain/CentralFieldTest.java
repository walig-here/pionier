package simulation.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.Pioneer;

/**
 * Testy jednostkowe klasy CentralFieldTest
 */
public class CentralFieldTest {
    @Test
    void testConstructor() {
        CentralField centralField = new CentralField(1, 2);
        Assertions.assertEquals(1, centralField.coordinates[0]);
        Assertions.assertEquals(2, centralField.coordinates[1]);
    }

    @Test
    void testItGivesBonusMovementPointsWhenPioneerEnters() {
        CentralField centralField = new CentralField(1, 2);
        Pioneer pioneer= new Pioneer(centralField);
        pioneer.setMove_points(100);
        centralField.goInto(pioneer);
        //mnoznik pobierany z bazy danych - domyslnie jest to 1.5
        Assertions.assertEquals(150, pioneer.getMove_points());
    }
    @Test
    void  testPioneerCannotEntryWithoutMovementPoints() {
        CentralField centralField = new CentralField(1, 2);
        Pioneer pioneer= new Pioneer(centralField);
        pioneer.setMove_points(0);
        pioneer.setCoordinates(1, 1);
        Assertions.assertFalse(centralField.goInto(pioneer));
    }

}
