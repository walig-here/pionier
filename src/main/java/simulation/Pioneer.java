package simulation;
import rendering.Sprite;
import simulation.terrain.CentralField;
import simulation.terrain.DepositField;
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

        // Na tym polu nie można już nic wybudować
        map[building_field[0]][building_field[1]].setCanBuild(false);

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

        ArrayList<Integer[]> potential_fields = new ArrayList<>();  // zbiór potencjalnych miejsc pod budowę
                                                                    // komórka 1 i 2 - koordynaty pola
                                                                    // komórka 3 - waga pola

        // Zbieramy potencjalnie miejsca pod budowę.
        // Sprawdzamy czy pole jest w dopuszczalnej minimalnej odległości od niezbędnych maszyn lub zawiera niezbędny do działania maszyny zasób.
        for (Field[] row : map)
        {
            for(Field field : row){
                Integer[] potential_field = {-1, -1, -1};

                // Sprawdzamy czy pole jest zdatne pod budowę
                if(!field.isCanBuild()) continue;

                // Elektrownie, tartaki, pompy ropy i maszyny wydobywcze potrzebują do działania stać na odpowiednim polu złoża
                if(to_build == 0 || to_build  == 2 || to_build == 4 || to_build == 1)
                {
                    // Jeżeli pole nie jest polem zasobów lub jego zasoby się wyczerpały to na pewno nie nadaje się pod taką budowę
                    if(!(field instanceof DepositField) || ((DepositField)field).getCapacityOfDeposit() <= 0)
                        continue;

                    // Elektrownia - potrzebuje węgiel lub rope lub drewno
                    if(to_build == 0){
                        if(((DepositField)field).getItem_id() != 1 && ((DepositField)field).getItem_id() != 2 && ((DepositField)field).getItem_id() != 4)
                            continue;
                    }

                    // Dowolna inna maszyna wydobywcza - potrzebuje surowca, który ma wydobywać
                    else if(((DepositField)field).getItem_id() != to_build)
                        continue;
                }

                // Reszta maszyn potrzebuje być w otoczeniu maszyn wytwarzających ich materiały wejściowe
                else{

                    // Sprawdzamy jakich maszyn potrzebujemy
                    ArrayList<Integer> needed_machines = new ArrayList<>();
                    {
                        ComponentItem item = new ComponentItem(to_build, 0,0); // item, który chcemy wyprodukować
                        for(Item input_item : item.getRecipe().getInput()){

                            // Sprawdzamy czy przedmiot wejściowy jest wytwarzany przez jakąś maszynę
                            if(!(input_item instanceof ComponentItem)) continue;

                            // Sprawdzamy jaka maszyna wytwarza ten przedmiot wejściowy
                            // Dodajemy te maszynę do wymaganych w okolicy budowanej akutalnie budowanej maszyny
                            needed_machines.add(((ComponentItem)input_item).getRecipe().getMachine());
                        }
                    }

                    // Szukamy pól mapy, które są oddalone o co najwyżej range
                    final int RANGE = 5; // maksymalne oddalenie maszyny od maszyn wytwarzających jej materiały wejściowe
                    for(int x = 0; x < map.length; x++) {
                        for(int y = 0; y < map[x].length; y++){

                            // Sprawdzamy czy pole nie jest za daleko
                            {
                                int distance = (int)Math.floor(Math.sqrt( Math.pow(field.getCoordinates()[0] - x, 2) + Math.pow(field.getCoordinates()[1]-y,2) ));
                                if(distance > RANGE) continue;
                            }

                            // Sprawdzamy czy pole ma w sobie interesującą nas maszynę
                            for(int i = 0; i < needed_machines.size(); i++){
                                // Jeżeli taką zawiera to nie usuwamy maszynę z listy potrzebnych maszyn - nie musimy już jej szukać w innych polach
                                if(map[x][y].getMachine().getID() == needed_machines.get(i)) {
                                    needed_machines.remove(i);
                                    break;
                                }
                            }
                        }
                    }

                    // Jeżeli nie znaleziono w okolicy wszystkich potrzebnych maszyn to pole nie nadaje się pod budowę
                    if(needed_machines.size() > 0) continue;
                }

                // Dodajemy pole do puli potencjalnych miejsc pod budowę
                potential_field[0] = field.getCoordinates()[0]; // koordynat x
                potential_field[1] = field.getCoordinates()[1]; // koordynat y
                potential_field[2] = 0; // waga
                potential_fields.add(potential_field);
            }
        }

        // Ustalenie wag pól budowlanych na podstawie ich odległości od pola centralnego
        {
            // Szukamy pola centralnego
            int[] central = new int[2]; // koordyanty pola centralnego
            {
                boolean central_found = false;
                for (Field[] row : map){
                    for(Field field : row){
                        if(field instanceof CentralField){
                            central[0] = field.getCoordinates()[0];
                            central[1] = field.getCoordinates()[1];
                            central_found = true;
                            break;
                        }
                    }
                    if(central_found) break;
                }
            }

            for(Integer[] field : potential_fields){
                int distance = (int)Math.floor(Math.sqrt( Math.pow(field[0] - central[0], 2) + Math.pow(field[1] - central[1],2) ));
                field[2] += distance;
            }
        }

        // Ustalenie wag pól budowlanych na podstawie prawdopodobieństw wystąpienia w nich zakłóceń
        for(Integer[] field : potential_fields){
            for (Byte[] probability : map[field[0]][field[1]].getGlitch_probabilities())
                field[2] += probability[1];
        }

        // Wybieramy pod budowę pole o najmniejszej wadze
        // komórka 1 i 2 - koordynaty pola o minimalnej wadze
        // komórka 3 - minimalna waga
        Integer[] min = {potential_fields.get(0)[0], potential_fields.get(0)[1], potential_fields.get(0)[2]};
        for(Integer[] field : potential_fields)
            if(field[2] < min[2]) min = field;
        building_field[0] = min[0]; building_field[1] = min[1];
    }

    // wyznacza jaki budynek postawi pionier jako nastepny
    public void setNextBuilding(ArrayList<Integer> buildingOrder, Field[][] map)
    {
        // Sprawdzamy czy pionier zakończył już ostatnią budowę
        if(to_build != -1) return;

        // Sprawdzamy czy nie zaczyna brakować pionierowi przedmiotu, który nie został uwzględniony w kolejce budowy
        for(Item eq_item : inventory){
            if(eq_item.getIncome() < 0){
                boolean included = false;

                for(Integer order_item : buildingOrder)
                    if(order_item == eq_item.getID())
                        included = true;

                // Jeżeli przedmiotu zaczyna brakować, a nie ma w planach zbudowania produkującej do maszyny to dodajemy taką na początek kolejki
                if(!included) buildingOrder.add(eq_item.getID());
            }
        }

        // Pobieramy ID produkowanego przez następną potrzebną maszynę przedmiotu
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
