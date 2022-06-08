package simulation;

import java.util.ArrayList;

public class ProductionMachine extends Machine {

    private ArrayList<Item> input; // lista obiektow potrzebnych do wytworzenia produktu wyjściowego

    public ProductionMachine(int ID, int produced_item) {
        super(ID, produced_item);

        //tworzy item, bierze jego nazwy i szuka takiej recepty
         input = new ComponentItem(produced_item, 0, 0.0).getRecipe().getInput();

    }
    public ProductionMachine(ProductionMachine copy){
        this(copy.getID(),copy.produced_item);
    }

    //rozpoczyna produkcję, zwieksza income produktow, zmniejsza income itemow potrzevbnych do wytworzenia produktu
    @Override
    public void startProduction(ArrayList<Item> inventory) {
        if(super.getActive()) return;

        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setIncome((inventoryItem.getIncome() + (double)getOutput()/inventoryItem.getProductionTime()));
            break;
        }

        for (Item inputItem : input) {
            for (Item inventoryItem : inventory) {
                if (inputItem.getID() != inventoryItem.getID()) continue;
                inventoryItem.setIncome(inventoryItem.getIncome() - (double) inputItem.getAmount()/inventoryItem.getProductionTime());
                break;
            }
        }
        super.setActive(true);
        Machine.active_machines++;
    }

    @Override
    public void stopProduction(ArrayList<Item> inventory){
        if(!super.getActive()) return;

        for (Item inventoryItem : inventory) {
            if (inventoryItem.getID() != getProduced_item()) continue;
            inventoryItem.setIncome((inventoryItem.getIncome() - (double)getOutput()/inventoryItem.getProductionTime()));
            break;
        }

        for (Item inputItem : input) {
            for (Item inventoryItem : inventory) {
                if (inputItem.getID() != inventoryItem.getID()) continue;
                inventoryItem.setIncome(inventoryItem.getIncome() + (double) inputItem.getAmount()/inventoryItem.getProductionTime());
                break;
            }
        }
        super.setActive(false);
        Machine.active_machines--;
    }

    // zmiana ilości przedmitów (amount) wynikła z produkcji
    public void production(ArrayList<Integer> buildingOrder, Pioneer pioneer, simulation.terrain.Field[][] map) {

        // sprawdzamy czy nie ma glitcha wyłączającego w maszynie
        if(super.glitch instanceof TurnOffGlitch) return;

        // sprawdzamy czy mamy wystarczająco przedmiotów do kontynuacji produkcji
        startProduction(pioneer.getInventory());
        for (Item inputItem : input) {
            for (Item inventoryItem : pioneer.getInventory()) {
                if (inputItem.getID() != inventoryItem.getID()) continue;

                // Jeżeli nie mamy wystarczającej ilości wymaganego przedmiotu, to zatrzymujemy produkcję i nakazujemy pionierowi pozyskać przedmiot
                if(inventoryItem.getAmount() - inputItem.getAmount() < 0 && inventoryItem.getIncome() < 0) {
                    stopProduction(pioneer.getInventory());
                    if(!pioneer.getEmergency_construction()){
                        buildingOrder.add(0,inventoryItem.getID());
                        pioneer.getPath().clear();
                        pioneer.setTo_build(-1);
                        pioneer.setNextBuilding(buildingOrder,map);
                        pioneer.setEmergency_construction(true);
                    }
                    return;
                }
                break;
            }
        }

        if(!super.getActive()) return;

        // produkcja trwa kolejną turę
        production_turn++;

        // sprawdzamy czy minęła już odpowiednia ilość tur, niezbędnych do wyprodukowania przedmiotu
        {
            Item temp = new Item(produced_item, 0,0);

            // jeżeli taka ilość czasu jeszcze nie minęła to produkcja trwa dalej
            if(temp.getProductionTime() > production_turn) return;

            // jeżeli taka ilość czasu już minęła to resetujemy timer produkcji
            production_turn = 0;
        }

        //przeszukuje ekwipunek w poszukiwaniu itemu produkowanego przez maszyne i zwieksza jego ilsoc
        for (Item item : pioneer.getInventory()) {
            if (item.getID() != getProduced_item()) continue;
            item.setAmount(item.getAmount() + getOutput());
            break;
        }

        //przeszukuje ekwipunek w poszukiwaniu przedmiotow potrzebnych do wyprodukowania produktu i zmniejsza ich ilosc
        for (Item inputItem : input) {
            for (Item inventoryItem : pioneer.getInventory()) {
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
