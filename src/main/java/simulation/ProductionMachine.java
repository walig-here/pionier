package simulation;

import java.util.ArrayList;

public class ProductionMachine extends Machine {

    private ArrayList<Item> input; // lista obiektow potrzebnych do wytworzenia produktu wyjściowego

    public ProductionMachine(int ID, int produced_item) {
        super(ID, produced_item);

        //tworzy item, bierze jego nazwy i szuka takiej recepty
         input = new ComponentItem(produced_item, 0, 0.0).getRecipe().getInput();

    }

    // zmiana ilości przedmitów wynikła z produkcji
    public void production(ArrayList<Item> inventory) {


    }

    public ArrayList<Item> getInput() {
        return input;
    }
}
