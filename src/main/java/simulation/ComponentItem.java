package simulation;

import java.util.ArrayList;

/**
 * Skomplikowany przedmiot, który może zostac pozyskany tylko z użyciem receptury.
 * */
public class ComponentItem extends Item {

    Recipe recipe; // receptura, którą trzeba wykonać, aby otrzymać ten obiekt

    public ComponentItem(String name, int amount, double income, int productionTime, ArrayList<Item> recipe, int machineId, int id) {
        super(id, amount,income);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void Recipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
