package simulation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe klasy Item
 */
public class ItemTest {

    @Test
    void testConstructor() {
        Item testItem = new Item(1,2,3);
        Assertions.assertEquals(1, testItem.getID());
        Assertions.assertEquals(2, testItem.getAmount());
        Assertions.assertEquals(3, testItem.getIncome());

        //Dane pobierane z bazy danych
        Assertions.assertEquals("Wegiel", testItem.getName());
        Assertions.assertEquals(1,testItem.getProductionTime());

        //dla drugiego przedmiotu
        Item testItem2 = new Item(3,4,5);
        Assertions.assertEquals(3, testItem2.getID());
        Assertions.assertEquals(4, testItem2.getAmount());
        Assertions.assertEquals(5, testItem2.getIncome());

        //Dane pobierane z bazy danych
        Assertions.assertEquals("Miedz", testItem2.getName());
        Assertions.assertEquals(1,testItem2.getProductionTime());
    }

    @Test
    void printAllItems(){

        for(int i = 0; i < 24; i++){
            Item energy = new Item(i, 100, -1d);

            System.out.println("STWORZONY PRZEDMIOT");
            System.out.println("Ilosc: " + energy.getAmount());
            System.out.println("Bilans: " + energy.getIncome());
            System.out.println("ID: " +energy.getID());
            System.out.println("Nazwa: " + energy.getName());
            System.out.println("Czas produkcji: " + energy.getProductionTime());
            System.out.println("\n");
        }
    }
}