package simulation;
import rendering.Sprite;
import simulation.terrain.CentralField;
import simulation.terrain.Field;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class Pioneer {
    private int[] coordinates; // koordynaty pioniera
    private ArrayList<Item> inventory; // ekwipunek pioniera

    private int move_points; // dostępne punkty ruchu
    private Sprite sprite; // render pioniera na ekranie
    private boolean could_build; // wkazuje czy pionier może coś zbudować
    private int to_build ; // produkt, który ma być produkowany przez budynek(w produkcie jesy receptura a w recepturze maszyna, którą trzeba zbudować)
    private int building_field[]; // gdzie postawić budynek?
    private ArrayList<Integer[]> path; // ścieżka po której porusza się pionier

    // konstruktor
    public Pioneer(Field spawn_filed){

        // ustalenie pozycji na planszy
        coordinates = new int[2];
        coordinates[0] = spawn_filed.getCoordinates()[0];
        coordinates[1] = spawn_filed.getCoordinates()[1];

        // pobranie początkowych punktów ruchu
        setStartMovePoints(spawn_filed);

        // pionier przy zespawnowaniu może budować
        could_build = true;

        // ustawiamy pole kolejnej budowy ustalamy na wartość [-1;-1] co oznacza, że pionier nie wybrał żadnego pola pod budowę
        building_field = new int[2];
        building_field[0] =  -1;

        // ustawiamy planowaną budowlę na wartość -1 co oznacza, że pionier nie wybrał żadnej budowli do postawienia
        to_build =- 1;

        // stworzenie instancji dla reszty pól
        path = new ArrayList<>();
    }

    // ustala punkty ruchu
    public void setMove_points(int move_points) {
        this.move_points = move_points;
    }

    //pobiera punkty ruchu
    public int getMove_points() {
        return move_points;
    }

    // ustala koordynaty
    public void setCoordinates(int x, int y) {
        this.coordinates[0] = x;
        this.coordinates[1] = y;
    }

    // pobiera koordynaty
    public int[] getCoordinates() {
        return coordinates;
    }

    // pobiera ścieżkę pioniera
    public ArrayList<Integer[]> getPath() {
        return path;
    }

    /**
     * Przemieszcza pioniera o jedno pole znajdujące się na początku ścieżki, którą ten podąża.
     *
     * @param map plansza symulacji
     * @param starting określa czy wywołana funkcja odpowiada pierwszemu ruchowi pioniera w tej turze
     * **/
    public void walk(Field[][] map, boolean starting) {
        // Sprawdzamy czy pionier może wyjść z pola, na którym aktualnie stoi
        if(!map[coordinates[0]][coordinates[1]].goOut(this, starting)) return;

        // Wybieramy z mapy pole, na które chcemy wejść. Następnie próbujemy wkroczyć pionierem na pole.
        // Jeżeli pionierowi nie uda się wejść na teren pola to nie przemieszcza się dalej w tej turze.
        // Jeżeli pionierowi uda się wejść na teren pola to przemieszcza się, a pole na które wkracza zostaje usunięte ze ścieżki,
        if(map[path.get(0)[0]][path.get(0)[1]].goInto(this)) {
            coordinates[0] = path.get(0)[0];
            coordinates[1] = path.get(0)[1];
            path.remove(0);
        }
    }

    // buduje maszynę na danym polu
    public void buildMachine(Field[][] map, ArrayList<Integer> buildingOrder) {

        // Sprawdzamu czy pionier ma w ogóle zamiar cokolwiek zbudować
        if(to_build == -1) return;

        // Sprawdzamy czy pionier może jeszcze coś zbudować
        if(!could_build) return;

        // Ustawiamy na polu maszyne na podstawie receptury itemu, który chcemy zacząć wytwarzać
        Item start_producting;
        Machine new_building;
        if(to_build > 0){
            start_producting = new ComponentItem(to_build, 0, 0);
            new_building = new ProductionMachine(((ComponentItem)start_producting).getRecipe().getMachine(),to_build);
        }
        else new_building= new Machine(0, to_build);

        // Sprawdzamy czy pionier ma wystarczająco materiałów do zbudowania tej maszyny
        for(Item item_cost : new_building.getCost().getInput()){
            boolean owns_item = false;
            for(Item item_eq : inventory){
                if(item_cost.getID() == item_eq.getID())
                {
                    if(item_eq.getAmount() - item_eq.getAmount() < 0) return;
                    owns_item = true;
                }
            }
            // Jeżeli pionier wcale nie posiada potrzebnego itemu to również nie zbuduje maszyny
            if(!owns_item) return;
        }

        // Stawiamy maszynę na polu
        map[building_field[0]][building_field[1]].setMachine(new_building);

        // Resetujemy wybór budowy
        to_build = -1;

        // Usuwamy maszynę z kolejki budowy symulacji
        buildingOrder.remove(0);

        // Pionier nie może już w tej turze budować
        could_build = false;
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
    private void findBuildingPlace(Field[][] map) {
        building_field[0] = building_field[1] = 0;
    }

    // wyznacza jaki budynek postawi pionier jako nastepny
    public void setNextBuilding(ArrayList<Integer> buildingOrder, Field[][] map)
    {
        // Sprawdzamy czy pionier zakończył już ostatnią budowę
        if(to_build != -1) return;

        // Pobieramy ID produkowanego przez maszynę przedmiotu
        to_build = buildingOrder.get(0);

        // Najpierw pionier musi udać się do magazynu po materiały, wyznaczamy mu ścieżkę/
        // Szukamy zatem koordynatów pola centralnego.
        {
            boolean central_found = false;
            for (Field[] row : map)
            {
                for(Field field : row){
                    if(field instanceof CentralField)
                    {
                        calculatePath(field);
                        central_found = true;
                        break;
                    }
                }
                if(central_found) break;
            }
        }

        // Szukamy idealnego miejsca pod budowę wybranego obiektu.
        findBuildingPlace(map);

        // Następnie wyznaczamy do niego ścieżkę
        calculatePath(map[building_field[0]][building_field[1]]);
    }

    // wyznacza bazowe punkty ruchu w zależności od pola na którym stoi pionier
    public void setStartMovePoints(Field start_filed)
    {
        move_points = start_filed.getBase_move_points();
    }
}
