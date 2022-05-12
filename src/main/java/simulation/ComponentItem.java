package simulation;

import java.util.ArrayList;

/**
 * Skomplikowany przedmiot, który może zostac pozyskany tylko z użyciem receptury.
 * */
public class ComponentItem extends Item {

    private ArrayList<Item> recipe; // lista obiektow potrzebnych do wytworzenia
    private int machineId; // id maszyny produkujacej komponent

    public ComponentItem(String name, int amount, double income, double productionTime, ArrayList<Item> recipe, int machineId) {
        super(name, amount, income, productionTime);
        this.recipe = recipe;
        this.machineId = machineId;
    }

    public ArrayList<Item> getRecipe() {
        return recipe;
    }

    public void setRecipe(ArrayList<Item> recipe) {
        this.recipe = recipe;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
