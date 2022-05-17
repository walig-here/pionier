package simulation;

import java.util.ArrayList;

/**
 * Prymitywna maszyna, która posiada tylko wyjście.
 */
public class Machine {

    private int produced_item; // ID produkowanego przedmitou
    private int output; // ilość przedmiotów produkowanych na turę
    private Recipe cost; // lista obiektow potrzebnych do wybudowania/ulepszenia
    private String name; // nazwa
    private Glitch glitch; // zakłócenie obecne w maszynie
    private int ID; // ID maszyny

    // zmiana ilości przedmitów wynikła z produkcji
    public void production(ArrayList<Item> inventory) {

    }

    // włączenie gitcha w maszynie
    public void activateGlitch(int gitchID) {

    }
}
