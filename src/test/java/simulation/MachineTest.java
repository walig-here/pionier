package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Testy jednostkowe klasy Machine
 */
public class MachineTest {

    @Test
    void testConstructor() {
        Machine machine = new Machine(0, 2);
        Assertions.assertEquals(0, machine.getID());
        Assertions.assertEquals(2, machine.getProduced_item());
        Assertions.assertEquals(15, machine.getOutput());
        Assertions.assertEquals("Elektrownia", machine.getName());
    }

    @Test
    void testConstructorWhenMachineNotExist() {
        Machine machine = new Machine(-1, 2);
        Assertions.assertNull(machine.getName());
        Assertions.assertEquals(-1,machine.getProduced_item());
        Exception exception = Assertions.assertThrows(FileNotFoundException.class, () -> {
            String path = "database/machines/";
            InputStream file_stream = new FileInputStream(path);
            Scanner file = new Scanner(file_stream);
        });
    }

    @Test
    void testItStartsProduction() {
        ArrayList<Item> inventory = new ArrayList<>();
        inventory.add(new Item(0, 25, 27));
        //output dla tej maszyny wynosi 10
        Machine machine = new Machine(0, 0);
        machine.startProduction(inventory);
        Assertions.assertEquals(42, inventory.get(0).getIncome());

    }
    @Test
    void testItProducesItems() {
        ArrayList<Item> inventory = new ArrayList<>();
        inventory.add(new Item(0, 25, 25));
        //output dla tej maszyny wynosi 10
        Machine machine = new Machine(0, 0);
        machine.production(inventory);
        Assertions.assertEquals(25, inventory.get(0).getAmount());
    }

}
