package main;

import rendering.*;
import simulation.*;
import simulation.terrain.*;
import java.util.ArrayList;

public class Main {

    static private NFrame current_window; // akutalne okno symulacji
    static private int grid_size; // rozmiar planszy(w polach) z przedziału [???]
    static private Field[][] map; // plansza na której odbywa się symulacja
    static private ArrayList<Integer> buildingOrder; // kolejka ID maszyn, które musi zbudować pionier aby wygrać
    static private MenuGUI menu;

    public static void main(String[] args) {
        grid_size = 30;
        //current_window = new NFrame(grid_size);
        menu=new MenuGUI();

        // główna pętkla symulacji
        simulationLoop(100);
    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    private static void simulationLoop(int max_turns) {

        for (;max_turns > 0; max_turns--) {

        }
    }

    // ustala kolejkę budynków, które powinien zbudować pionier
    private static void setBuildingOrder() {

    }
}