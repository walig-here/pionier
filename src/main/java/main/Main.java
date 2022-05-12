package main;

import rendering.*;
import simulation.terrain.Field;

public class Main {

    static private NFrame window; // główne okno symulacji
    static private int grid_size; // rozmiar planszy(w polach) z przedziału [???]
    static private Field[][] map; // plansza na której odbywa się symulacja
    static private int current_turn; // aktualna tura

    public static void main(String[] args) {
        grid_size = 30;
        window = new NFrame(grid_size);
    }

}

/* ----------------------------------------------------------------------------------
 TO DO:
 > Obiekt receptury
 > Metoda odpowiedzialna za budowanie
 > Metoda odpowiedzialna za ruch pioniera
 > Metoda odpowiedzialna za wystąpienie zakłócenia
 > Metoda odpowiedzialna za wyznaczenie prawodpodobieństwa zakłócenia w polu
 > Layout okna edytora mapy
 > Layout okna ekranu startowego symulacji
 > Klasa zapisująca dane z symulacji do pliku na dysku
 > Metoda wyznaczająca ścieżkę poruszania się pioniera
 > Metoda symulująca produkcję
 > Proste AI pioniera, pozwalające na wyznaczenie kolejki działań, które ten wykona
---------------------------------------------------------------------------------- */