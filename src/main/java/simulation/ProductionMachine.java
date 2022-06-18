package simulation;

import main.Main;
import simulation.terrain.Field;

import java.util.ArrayList;

/**
 * Maszyna posiadająca wejście i wyjście. Rozszerza klasę Machine, dodając parametr input mówiący o koszcie produkcji przedmiotu
 */
public class ProductionMachine extends Machine {

    /**
     * Lista obiektów potrzebnych do wytworzenia produktu wyjściowego — informacja brana z bazy danych recipes.
     */
    private final ArrayList<Item> input; // lista obiektów potrzebnych do wytworzenia produktu wyjściowego

    /**
     * Konstruktor klasy ProductionMachine. Rozszerza konstruktor klasy Machine, przeszukuje bazę danych klasy Recipe i dodaje informacje o koszcie wytworzenia produktu
     * @param ID id maszyny
     * @param produced_item przedmiot, który maszyna ma produkować
     */
    public ProductionMachine(int ID, int produced_item) {
        super(ID, produced_item);

        //tworzy item, bierze jego nazwy i szuka takiej recepty
        input = new ComponentItem(produced_item, 0, 0.0).getRecipe().getInput();
    }

    /**
     * Konstruktor kopiujący
     * @param copy obiekt klasy ProductionMachine, który zostanie skopiowany
     */
    public ProductionMachine(ProductionMachine copy){
        this(copy.getID(),copy.produced_item);
    }

    /**
     * Rozpoczyna produkcję, zwiększa przyrost (income) wytwarzanych produktów i ilość aktywnych maszyn. Zmniejsza przyrost produktów będących składnikami
     * @param inventory ekwipunek pioniera
     */
    @Override
    public void startProduction(ArrayList<Item> inventory) {
        if(super.getActive() == 1 || super.getActive() == -1) return;

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
        super.setActive(1);
        Machine.active_machines++;
    }
    /**
     * Zatrzymuje produkcję, zwiększa przyrost (income) składników. Zmniejsza przyrost wytwarzanego produktu i ilość aktywnych maszyn
     * @param inventory ekwipunek pioniera
     */
    @Override
    public void stopProduction(ArrayList<Item> inventory){
        if(super.getActive() == 0 || super.getActive() == -1) return;

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
        super.setActive(0);
        Machine.active_machines--;
    }

    /**
     * Metoda odpowiedzialna za produkcję. Jeśli maszyna nie ma aktywnego glitcha i w ekwipunku pioniera znajduje się dostateczna ilość składników — zwiększa ilość produkowanego przedmiotu w ekwipunku pioniera i zmniejsza ilość składników.
     * @param buildingOrder kolejka budowania budynków
     * @param pioneer pionier
     * @param map mapa
     */
    public int production(ArrayList<Integer> buildingOrder, Pioneer pioneer, Field[][] map) {

        // sprawdzamy, czy nie ma glitcha wyłączającego w maszynie
        if(super.glitch != null && super.glitch.getID() == 0) return 0;

        // sprawdzamy, czy mamy wystarczająco przedmiotów do kontynuacji produkcji
        Item temp = new Item(produced_item, 0,0);
        startProduction(pioneer.getInventory());
        for (Item inputItem : input) {
            for (Item inventoryItem : pioneer.getInventory()) {
                if (inputItem.getID() != inventoryItem.getID()) continue;

                // Jeżeli nie mamy wystarczającej ilości wymaganego przedmiotu, to zatrzymujemy produkcję i nakazujemy pionierowi pozyskać przedmiot
                if(inventoryItem.getAmount() - inputItem.getAmount()/temp.getProductionTime() < 0) {

                    Main.addToLog("\tMaszyna " + getName() + " zosta\u0142a tymczasowo wy\u0142\u0105czona ze wzgl\u0119du na brak " + inputItem.getName() + ".");
                    stopProduction(pioneer.getInventory());

                    if(inventoryItem.getIncome() < 0 && !pioneer.getEmergency_construction() && (buildingOrder.size() == 0 || buildingOrder.get(0) != inventoryItem.getID())){
                        buildingOrder.add(0,inventoryItem.getID());
                        pioneer.getPath().clear();
                        pioneer.setTo_build(-1);
                        if(pioneer.setNextBuilding(buildingOrder,map) == -1) return -1;
                        pioneer.setEmergency_construction(true);
                    }
                    return 0;
                }
                break;
            }
        }

        if(super.getActive() == 0 || super.getActive() == -1) return 0;

        // produkcja trwa kolejną turę
        production_turn++;

        //przeszukuje ekwipunek w poszukiwaniu przedmiotów potrzebnych do wyprodukowania produktu i zmniejsza ich ilość
        for (Item inputItem : input) {
            for (Item inventoryItem : pioneer.getInventory()) {
                if (inputItem.getID() != inventoryItem.getID()) continue;
                inventoryItem.setAmount(inventoryItem.getAmount() - inputItem.getAmount()/temp.getProductionTime());
                break;
            }
        }

        // sprawdzamy, czy minęła już odpowiednia ilość tur, niezbędnych do wyprodukowania przedmiotu;
        // jeżeli taka ilość czasu jeszcze nie minęła, to produkcja trwa;
        if(temp.getProductionTime() > production_turn) return 0;

        // jeżeli taka ilość czasu już minęła to resetujemy timer produkcji
        production_turn = 0;

        //przeszukuje ekwipunek w poszukiwaniu itemu produkowanego przez maszyne i zwieksza jego ilsoc
        for (Item item : pioneer.getInventory()) {
            if (item.getID() != getProduced_item()) continue;
            item.setAmount(item.getAmount() + getOutput());
            break;
        }

        return 0;
    }

    public ArrayList<Item> getInput() {
        return input;
    }
}