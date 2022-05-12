package main;

import rendering.*;
import simulation.terrain.Field;

public class Main {

    static private NFrame window; // główne okno symulacji
    static private int grid_size; // rozmiar planszy(w polach) z przedziału [???]
    static private Field[][] map; // plansza na której odbywa się symulacja

    static private int tura;
    public static int getTura() {
        return tura;
    }

    public static void setTura(int tura) {
        Main.tura = tura;
    }

    public static void main(String[] args) {
        grid_size = 30;
        window = new NFrame(grid_size);
    }

}
