package simulation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Skomplikowany przedmiot, który może zostac pozyskany tylko z użyciem receptury.
 * */
public class ComponentItem extends Item {

    Recipe recipe; // receptura, którą trzeba wykonać, aby otrzymać ten obiekt

    public ComponentItem(int id, int amount, double income) {

        // Wywołujemy konstruktor rodzica
        super(id, amount,income);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void Recipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
