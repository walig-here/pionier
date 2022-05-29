package simulation;

import java.util.ArrayList;

public class ProductionMachine extends Machine {

    private ArrayList<Item> input; // lista obiektow potrzebnych do wytworzenia produktu wyjściowego

    public ProductionMachine(int ID, int produced_item) {
        super(ID, produced_item);

        //tworzy item, bierze jego nazwy i szuka takiej recepty
         input = new ComponentItem(produced_item, 0, 0.0).getRecipe().getInput();

    }

    //rozpoczyna produkcję, zwieksza income produktow, zmniejsza income itemow potrzevbnych do wytworzenia produktu
    public void startProduction(ArrayList<Item> inventory) {
        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setIncome((inventoryItem.getIncome() + getOutput()));
            break;
        }

        for (Item inputItem : input) {
            for (Item inventoryItem : inventory) {
                if (inputItem.getID() != inventoryItem.getID()) continue;
                inventoryItem.setIncome(inventoryItem.getIncome() - inputItem.getAmount());
                break;
            }
        }
    }

    // zmiana ilości przedmitów (amount) wynikła z produkcji
    public void production(ArrayList<Item> inventory) {
        //przeszukuje ekwipunek w poszukiwaniu itemu produkowanego przez maszyne i zwieksza jego ilsoc
        for (Item item : inventory) {
            if (item.getID() != getProduced_item()) continue;
            item.setAmount(item.getAmount() + getOutput());
            break;
        }

        //przeszukuje ekwipunek w poszukiwaniu przedmiotow potrzebnych do wyprodukowania produktu i zmniejsza ich ilosc
        for (Item inputItem : input) {
            for (Item inventoryItem : inventory) {
                if (inputItem.getID() != inventoryItem.getID()) continue;
                inventoryItem.setAmount(inventoryItem.getAmount() - inputItem.getAmount());
                break;
            }
        }
    }

    public ArrayList<Item> getInput() {
        return input;
    }
}
