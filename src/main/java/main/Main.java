package main;

import rendering.*;
import simulation.*;
import simulation.terrain.*;
import java.util.ArrayList;

public class Main {

    static private NFrame current_window; // akutalne okno symulacji
    static private int grid_size; // rozmiar planszy(w polach) z przedziału [???]
    static private Field[][] map; // plansza na której odbywa się symulacja
    static private int current_turn; // aktualna tura
    static private ArrayList<Integer> buildingOrder; // kolejka ID maszyn, które musi zbudować pionier aby wygrać
    static private Kanwa3 menu;

    public static void main(String[] args) {
        grid_size = 30;
        //current_window = new NFrame(grid_size);
        menu=new Kanwa3();
    }

    // ustala kolejkę budynków, które powinien zbudować pionier
    private static void setBuildingOrder() {

    }
}