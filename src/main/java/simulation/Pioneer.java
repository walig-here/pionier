package simulation;
import rendering.Sprite;
import simulation.terrain.Field;

import java.io.File;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Pioneer {
    private int[] coordinates; // koordynaty pioniera

    private ArrayList<Item> inventory; // ekwipunek pioniera
    private int move_points; // dostępne punkty ruchu
    private Sprite sprite; // render pioniera na ekranie
    private boolean could_build; // wkazuje czy pionier może coś zbudować

    private int to_build; // jaką maszynę postawić(ID)?
    private int building_field[][]; // gdzie postawić budynek?
    private ArrayList<Integer[]> path = new ArrayList<>(); // ścieżka po której porusza się pionier

    // konstruktor
    public Pioneer(Field spawn_filed){

        // ustalenie pozycji na planszy
        coordinates = new int[2];
        coordinates[0] = spawn_filed.getCoordinates()[0];
        coordinates[1] = spawn_filed.getCoordinates()[1];
    }

    // pobiera koordynaty
    public int[] getCoordinates() {
        return coordinates;
    }

    // pobiera ścieżkę pioniera
    public ArrayList<Integer[]> getPath() {
        return path;
    }

    // przemieszcza pioniera na pierwszą pozycję zapisaną w ścieżce path
    public void walk()
    {

    }

    // buduje maszynę na danym polu
    public void buildMachine(Field[][] build_in) {

    }

    // wyznacza ścieżkę po której porusza się pionier
    public void calculatePath(Field destination) {

        // Sprawdzamy różnice między koordynatami x oraz y celu względem pioniera
        int delta_x = destination.getCoordinates()[0] - coordinates[0];
        int delta_y = destination.getCoordinates()[1] - coordinates[1];

        // Jeżeli różnice między oboma koordynatami wynoszą zero, oznacza to, że pionier nie musi się przemieszczać i nie potrzebuje ścieżki
        if(delta_x == 0 && delta_y == 0) return;

        // Jeżeli różnica w koordynatach x wynosi 0 to funkcja ścieżki przyjmie postać x(y) = x_pioniera
        if(delta_x == 0) {

            // Jeżeli różnica w koordynatach y jest ujemna to cofamy się na osi OY o 1 jednostkę do momentu, aż osiągniemy koordynat x równy y_celu
            // Jeżeli różnica w koordynatach y jest dodatnia to idziemy naprzód na osi OY o 1 jednostkę do momentu, aż osiągniemy koordynat y równy y_celu
            // W czasie poruszania się dodajemy do ścieżki przemierzane pola o równaniu (x_pioniera, y)
            int y_motion = (delta_y < 0 ? -1 : 1);
            for (int current_y = coordinates[1]+y_motion; current_y != destination.getCoordinates()[1]; current_y += y_motion){
                Integer[] field = new Integer[2];
                field[0] = coordinates[0];
                field[1] = current_y;
                path.add(field);
            }

        }
        // Jeżeli różnica w koordynatach y wynosi 0 to funkcja ścieżki przyjmie postać y(x) = y_pioniera
        else if(delta_y == 0) {

            // Jeżeli różnica w koordynatach x jest ujemna to cofamy się na osi OX o 1 jednostkę do momentu, aż osiągniemy koordynat x równy x_celu
            // Jeżeli różnica w koordynatach x jest dodatnia to idziemy naprzód na osi OX o 1 jednostkę do momentu, aż osiągniemy koordynat x równy x_celu
            // W czasie poruszania się dodajemy do ścieżki przemierzane pola o równaniu (x, y_pioniera)
            int x_motion = (delta_x < 0 ? -1 : 1);
            for (int current_x = coordinates[0]+x_motion; current_x != destination.getCoordinates()[0]; current_x += x_motion) {
                Integer[] field = new Integer[2];
                field[1] = coordinates[1];
                field[0] = current_x;
                path.add(field);
            }

        }
        // Jeżeli obie różnice w koorydnatach są niezerowe to:
        // > Gdy wartość bezwzględna różnicy y jest większa od różnicy x to funkcja ścieżki przyjmie postać x(y) = podłoga( (y-b)/a )
        // > Gdy wartość bezwzględna różnicy x jest większa/równa od różnicy y to funkcja ścieżki przyjmie postać y(x) = podłoga( ax + b )
        else {
            double a = ((double)coordinates[1] - destination.getCoordinates()[1])/(coordinates[0]-destination.getCoordinates()[0]);
            double b = coordinates[1] - coordinates[0] * a;

            if(Math.abs(delta_x) >= Math.abs(delta_y)) {

                // Jeżeli różnica w koordynatach x jest ujemna to cofamy się na osi OX o 1 jednostkę do momentu, aż osiągniemy koordynat x równy x_celu
                // Jeżeli różnica w koordynatach x jest dodatnia to idziemy naprzód na osi OX o 1 jednostkę do momentu, aż osiągniemy koordynat x równy x_celu
                // W czasie poruszania się dodajemy do ścieżki przemierzane pola o równaniu (x, podłoga( ax + b ))
                int x_motion = (delta_x < 0 ? -1 : 1);
                for (int current_x = coordinates[0]+x_motion; current_x != destination.getCoordinates()[0]; current_x += x_motion){
                    Integer[] field = new Integer[2];
                    field[1] = (int)Math.floor( current_x*a + b );
                    field[0] = current_x;
                    path.add(field);
                }

            }
            else {

                // Jeżeli różnica w koordynatach y jest ujemna to cofamy się na osi OY o 1 jednostkę do momentu, aż osiągniemy koordynat x równy y_celu
                // Jeżeli różnica w koordynatach y jest dodatnia to idziemy naprzód na osi OY o 1 jednostkę do momentu, aż osiągniemy koordynat y równy y_celu
                // W czasie poruszania się dodajemy do ścieżki przemierzane pola o równaniu (x, podłoga( (y-b)/a ))
                int y_motion = (delta_y < 0 ? -1 : 1);
                for (int current_y = coordinates[1]+y_motion; current_y != destination.getCoordinates()[1]; current_y += y_motion){
                    Integer[] field = new Integer[2];
                    field[1] = current_y;
                    field[0] = (int)Math.floor( (current_y - b) / a);
                    path.add(field);
                }

            }
        }

        // Na koniec ścieżki dodajemy ostatnie pole, którym jest pole celu, do którego ścieżka miała prowadzić
        Integer[] field = new Integer[2];
        field[1] = destination.getCoordinates()[1];
        field[0] = destination.getCoordinates()[0];
        path.add(field);
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
