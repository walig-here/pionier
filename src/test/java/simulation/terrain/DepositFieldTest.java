package simulation.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.Pioneer;
import simulation.Machine;
import simulation.Item;

import java.util.ArrayList;

/**
 * Testy jednostkowe klasy DepositFieldTest
 */
public class DepositFieldTest {
    
    @Test
    void testConstructor() {
        DepositField depositField = new DepositField(1, 2, 1000, 2);
        Assertions.assertEquals(1, depositField.coordinates[0]);
        Assertions.assertEquals(2, depositField.coordinates[1]);
        Assertions.assertEquals(1000, depositField.getCapacityOfDeposit());
        Assertions.assertEquals(2,depositField.getItem_id());
    }
    
    @Test
    void testItDecreasesMovementPointsAmount() {
        DepositField depositField = new DepositField(1, 2, 1000, 2);
        Pioneer pioneer= new Pioneer(depositField);
        pioneer.setMove_points(100);
        depositField.goInto(pioneer);
        Assertions.assertEquals(99, pioneer.getMove_points());
    }
    @Test
    void  testPioneerCannotEntryWithoutMovementPoints() {
        DepositField depositField = new DepositField(1, 2, 1000, 2);
        Pioneer pioneer= new Pioneer(depositField);
        pioneer.setMove_points(0);
        pioneer.setCoordinates(1, 1);
        Assertions.assertFalse(depositField.goInto(pioneer));
    }

    @Test
    void testItGivesMovementPoints() {
        DepositField depositField = new DepositField(1, 2, 1000, 2);
        Pioneer pioneer= new Pioneer(depositField);
        pioneer.setMove_points(0);
        depositField.goOut(pioneer, true);
        //wartosc pobrana z bazy danych
        Assertions.assertEquals(2, pioneer.getMove_points());
    }

    @Test
    void testMachineExtractsMaterials() {
        int capacity = 1000;
        DepositField depositField = new DepositField(1, 2, capacity, 6);
        Machine machine = new Machine(2, 6);
        machine.setActive(1);
        ArrayList<Item> inventory = new ArrayList<Item>();
        depositField.setMachine(machine, inventory);
        depositField.extract(inventory);
        Assertions.assertEquals(capacity - machine.getOutput(), depositField.getCapacityOfDeposit());
    }
}
