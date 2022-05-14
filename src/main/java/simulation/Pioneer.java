package simulation;
import rendering.Sprite;
import simulation.terrain.Field;

import java.io.File;
import java.util.ArrayList;

public class Pioneer {

    private int[] coordinates = new int[2]; // koordynaty pioniera

    private ArrayList<Item> inventory; // ekwipunek pioniera
    private int move_points; // dostępne punkty ruchu
    private Sprite sprite; // render pioniera na ekranie
    private boolean could_build; // wkazuje czy pionier może coś zbudować

    private int to_build; // jaką maszynę postawić(ID)?
    private int building_field[][]; // gdzie postawić budynek?
    private ArrayList<Integer[][]> path; // ścieżka po której porusza się pionier

    // przemieszcza pioniera na pierwszą pozycję zapisaną w ścieżce path
    public void walk()
    {

    }

    // buduje maszynę na danym polu
    public void buildMachine(Field[][] build_in) {

    }

    // wyznacza ścieżkę po której porusza się pionier
    public void calculatePath(Field[][] map) {

    }

    // wylicza pole pod następną budowę
    public void findBuildingPlace(Field[][] map) {

    }

    // wyznacza jaki budynek postawi pionier jako nastepny
    public void setNextBuilding(ArrayList<Integer> buildingOrder)
    {

    }

    // wyznacza bazowe punkty ruchu w zależności od pola na którym stoi
    public void setMove_points(Field start_filed)
    {
        
    }
}
