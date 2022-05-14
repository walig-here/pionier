package simulation;
import rendering.Sprite;

import java.io.File;
import java.util.ArrayList;

public class Pioneer {

    private int[] coordinates = new int[2]; // koordynaty pioniera

    private ArrayList<Item> inventory; // ekwipunek pioniera
    private int move_points; // dostępne punkty ruchu
    private Sprite sprite; // render pioniera na ekranie
    private boolean could_build; // wkazuje czy pionier może coś zbudować
}
