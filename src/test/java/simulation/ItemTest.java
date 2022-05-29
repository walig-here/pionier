package simulation;        // Plik z danymi ustalamy na podstawie nazwy maszyny


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void constructor(){

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