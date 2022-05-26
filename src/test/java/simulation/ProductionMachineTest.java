package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class ProductionMachineTest {
    @Test
    void testConstructor() {
        int itemId = 9;
        ProductionMachine machine = new ProductionMachine(3, itemId);
        Assertions.assertEquals(3, machine.getID());
        Assertions.assertEquals(itemId, machine.getProduced_item());
        Assertions.assertEquals(10, machine.getOutput());
        Assertions.assertEquals("Piec", machine.getName());
        Recipe recipe = new ComponentItem(itemId, 0, 0.0).getRecipe();

        for (int i = 0; i < recipe.getInput().size(); i++) {
            Assertions.assertEquals(recipe.getInput().get(i).getID(), machine.getInput().get(i).getID());
            Assertions.assertEquals(recipe.getInput().get(i).getAmount(), machine.getInput().get(i).getAmount());
            Assertions.assertEquals(recipe.getInput().get(i).getName(), machine.getInput().get(i).getName());
            Assertions.assertEquals(recipe.getInput().get(i).getProductionTime(), machine.getInput().get(i).getProductionTime());
            Assertions.assertEquals(recipe.getInput().get(i).getIncome(), machine.getInput().get(i).getIncome());
        }
    }

    @Test
    void testConstructorWhenMachineNotExist() {
        ProductionMachine machine = new ProductionMachine(-1, 2);
        Assertions.assertNull(machine.getName());
        Assertions.assertEquals(-1,machine.getProduced_item());
        Exception exception = Assertions.assertThrows(FileNotFoundException.class, () -> {
            String path = "database/machines/";
            InputStream file_stream = new FileInputStream(path);
            Scanner file = new Scanner(file_stream);
        });
    }
}
