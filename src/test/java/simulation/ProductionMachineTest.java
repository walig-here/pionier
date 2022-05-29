package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
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

    @Test
    void testItStartsProduction() {
        ArrayList<Item> inventory = new ArrayList<>();
        inventory.add(new Item(0, 25, 23)); //energia
        inventory.add(new ComponentItem(3, 25, 22)); //miedz
        inventory.add(new ComponentItem(1, 25, 21)); //wegiel
        ComponentItem copperIngot = new ComponentItem(9, 25, 20);
        inventory.add(copperIngot);

        Recipe copperIngotRecipe = copperIngot.getRecipe();
        ProductionMachine furnace = new ProductionMachine(copperIngotRecipe.getMachine(), copperIngot.getID());
        furnace.startProduction(inventory);

        Assertions.assertEquals(22, inventory.get(0).getIncome());
        Assertions.assertEquals(21, inventory.get(1).getIncome());
        Assertions.assertEquals(20, inventory.get(2).getIncome());
        Assertions.assertEquals(30, inventory.get(3).getIncome());


    }

    @Test
    void testItProducesItems() {
        ArrayList<Item> inventory = new ArrayList<>();
        inventory.add(new Item(0, 25, 25)); //energia
        inventory.add(new ComponentItem(3, 25, 25)); //miedz
        inventory.add(new ComponentItem(1, 25, 25)); //wegiel
        ComponentItem copperIngot = new ComponentItem(9, 25, 20);
        inventory.add(copperIngot);

        Recipe copperIngotRecipe = copperIngot.getRecipe();
        ProductionMachine furnace = new ProductionMachine(copperIngotRecipe.getMachine(), copperIngot.getID());
        furnace.production(inventory);

        //koszt sztabki miedzi to  po 1 sztuce wegla, energii i miedzi
        Assertions.assertEquals(24, inventory.get(0).getAmount());
        Assertions.assertEquals(24, inventory.get(1).getAmount());
        Assertions.assertEquals(24, inventory.get(2).getAmount());
        Assertions.assertEquals(35, inventory.get(3).getAmount());
    }

}

