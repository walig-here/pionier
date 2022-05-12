package main;

import rendering.NFrame;

public class Main {

    static private NFrame window; // główne okno symulacji
    static private int grid_size; // rozmiar planszy(w polach)

    public static void main(String[] args) {
        grid_size = 30;
        window = new NFrame(grid_size);
    }

}
