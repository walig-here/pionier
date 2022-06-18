package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy ComponentItem
 */
public class ComponentItemTest {

    @Test
    void testConstructor() {
        ComponentItem testItem = new ComponentItem(1,2,3);
        Assertions.assertEquals(1, testItem.getID());
        Assertions.assertEquals(2, testItem.getAmount());
        Assertions.assertEquals(3, testItem.getIncome());

        //Dane pobierane z bazy danych
        Assertions.assertEquals("Wegiel", testItem.getName());
        Assertions.assertEquals(1,testItem.getProductionTime());
        Assertions.assertEquals(2, testItem.getRecipe().getMachine());

        //dla drugiego przedmiotu
        ComponentItem testItem2 = new ComponentItem(3,4,5);
        Assertions.assertEquals(3, testItem2.getID());
        Assertions.assertEquals(4, testItem2.getAmount());
        Assertions.assertEquals(5, testItem2.getIncome());

        //Dane pobierane z bazy danych
        Assertions.assertEquals("Miedz", testItem2.getName());
        Assertions.assertEquals(1,testItem2.getProductionTime());
        Assertions.assertEquals(2, testItem2.getRecipe().getMachine());
    }

    @Test
    void printAllComponentItems() {

        for(int i = 1; i < 24; i++){
            ComponentItem item = new ComponentItem(i,100,0);

            System.out.println("STWORZONY PRZEDMIOT");
            System.out.println("Ilosc: " + item.getAmount());
            System.out.println("Bilans: " + item.getIncome());
            System.out.println("ID: " +item.getID());
            System.out.println("Nazwa: " + item.getName());
            System.out.println("Czas produkcji: " + item.getProductionTime());

            System.out.println("Maszyna produkujaca: " );
            System.out.println("Maszyna: " + item.getRecipe().getMachine());
            System.out.print("Skladniki: ");
            for(Item component : item.getRecipe().getInput()){
                System.out.print(component.getAmount() + "x" + component.getName() + "\t");
            }
            System.out.println("\n");
        }
    }
}