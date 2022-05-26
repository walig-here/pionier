package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class MachineTest {

    @Test
    void testConstructor() {
        Machine machine = new Machine(0, 2);
        Assertions.assertEquals(0, machine.getID());
        Assertions.assertEquals(2, machine.getProduced_item());
        Assertions.assertEquals(10, machine.getOutput());
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
}
