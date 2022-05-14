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
    private boolean active; // wskazuje czy maszyna działa
}
