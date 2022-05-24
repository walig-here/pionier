package simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentItemTest {

    @Test
    void constructor() {

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