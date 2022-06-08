package simulation;
import rendering.Sprite;
import simulation.terrain.CentralField;
import simulation.terrain.DepositField;
import simulation.terrain.Field;

import java.io.FileInputStream;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Pioneer {
    /**
     * Tablica liczb całkowitych. W pierwszej komórce przechowuje pozycję x pioniera na planszy. W drugiej przechowuje pozycję y pioniera na planszy.
     * */
    private int[] coordinates;

    /**
     * Lista przedmiotów posiadanych przez pioniera.
     * */
    private ArrayList<Item> inventory;
    /**
     * Punkty ruchu posiadane przez pioniera. Determinują one ilośc pól, jaką ten może się przemieścić.
     * */
    private int move_points;
    /**
     * Obiekt przechowujący dane o graficznej reprezentacji pioniera w oknie symulacji.
     * */
    private Sprite sprite;
    /**
     * Zmienna zawierająca informację co do możliwości zbudowania kolejnej maszyny przez pioniera w tej turze.
     * */
    private boolean could_build;

    /**
     * Przechowuje ID przedmiotu, którego maszynę go produkującą pionier ma zamiar postawić przy najbliżeszj okazji.
     * */
    private int to_build ;
    /**
     * Tablica liczb całkowitych przechowująca koordynaty pola, na którym pionier dokona najbliższej konstrukcji.
     * Pierwsza komórka odpowiada za współrzędną x, druga za współrzędną y.
     * */
    private int building_field[];
    /**
     * Lista dwuelementowych tablic, zawierających koordynaty pól, tworzących ścieżkę, po której będzie poruszał się pionier.
     * */
    private ArrayList<Integer[]> path;

    private boolean emergency_construction; // określa czy pionier aktualnie wykonuje prorytetową budowę


    // funkcja wyboru miejsca pod pole centralne
    public int chooseCentral(Field[][] map, ArrayList<Integer> buildingOrder){

        ArrayList<Integer[]> potential_centrals = new ArrayList<>();

        // Potencjalnymi polami są wszystkie pola, które nie są wodą, źródłem zakłóceń albo polem z surowcem, który będzie potrzebny
        for(Field[] row : map){
            for(Field field : row){

                // czy pole wodne lub pole zakłóceń
                if(field.getTerrain_id() == 1 || field.getTerrain_id() == 3) continue;

                // czy jest to pole z zasobem, który na pewno będzie potem potrzebny
                if(field instanceof DepositField){
                    boolean includes_needed_item = false;
                    for (Integer needed_item : buildingOrder) {
                        if(needed_item == ((DepositField)field).getItem_id()) {
                            includes_needed_item = true;
                            break;
                        }
                    }
                    if(includes_needed_item) continue;
                }

                // dodajemy pole do potencjalnych centrów
                Integer[] new_field = {field.getCoordinates()[0], field.getCoordinates()[1], 0};
                potential_centrals.add(new_field);
            }
        }

        // Jeżeli nie ma żadnych potencjalnych pól to pionier przegrywa symulacje
        if(potential_centrals.size() == 0) return -1;

        // Nadajemy polom, w których okolicy znajduje się więcej surowców większą wagę
        // Nadajemy polom, w których okolicy znajduje się większe prawdopodobieństwo zakłócenia mniejszą wagę
        // Nadajemu polom, w których okolicy znajduje się więcej pól wodnych mniejszą wagę(bo utrudniają przemieszczanie)
        final int max_distance = map.length / 3;
        for(Integer[] central : potential_centrals){

            for(Field[] row : map){
                for(Field map_field : row){
                    int distance = (int)Math.sqrt(Math.pow(map_field.getCoordinates()[0]-central[0],2)+Math.pow(map_field.getCoordinates()[1]-central[1],2));
                    if(distance > max_distance) continue;

                    if(map_field instanceof DepositField) {
                        central[2] += ((DepositField)map_field).getCapacityOfDeposit();
                        for (Integer needed_item : buildingOrder) {
                            if(needed_item == ((DepositField)map_field).getItem_id()) {
                                central[2] += 2 * ((DepositField)map_field).getCapacityOfDeposit();
                                break;
                            }
                        }
                    }
                    for(Byte[] probability : map_field.getGlitch_probabilities()) central[2] -= probability[1];
                    if(map_field.getTerrain_id() == 1) central[2] -= 10;
                }
            }

        }

        // wybieramy te pole, które ma największą wagę
        Integer[] best_cental = {potential_centrals.get(0)[0], potential_centrals.get(0)[1], potential_centrals.get(0)[2]};
        for(Integer[] central : potential_centrals){
            if(best_cental[2] < central[2])
            {
                best_cental[0] = central[0];
                best_cental[1] = central[1];
                best_cental[2] = central[2];
            }
        }
        map[best_cental[0]][best_cental[1]] = new CentralField(best_cental[0],best_cental[1]);
        return 0;
    }

    public void setEmergency_construction(boolean emergency_construction) {
        this.emergency_construction = emergency_construction;
    }

    public boolean getEmergency_construction() {
        return emergency_construction;
    }

    public void setTo_build(int to_build) {
        this.to_build = to_build;
    }

    /**
     * Konstruktor klasy pionier. Na podstawie danych pola, na którym na początku symulacji stoi pionier ustala część z jego parametrów.
     * Nadaje także wartości początkowe wszystkim innym polom klasy.
     *
     * @param spawn_filed pole, na którym na początku symulacji pojawił się pionier
     * */
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
        inventory = new ArrayList<>();
    }


    /**
     * Setter. Ustala punkty ruchu posiadane przez pioniera w danej turze.
     *
     * @param move_points nowa ilość punktów ruchu
     * */
    public void setMove_points(int move_points) {
        this.move_points = move_points;
    }


    /**
     * Getter. Pobiera liczbę punktów ruchu posiadanych przez pioniera.
     *
     * @return ilość punktów ruchu posiadaną przez pioniera
     * */
    public int getMove_points() {
        return move_points;
    }


    /**
     * Setter. Ustala położenie pioniera na planszy.
     *
     * @param x nowa pozycja x pioniera na planszy
     * @param y nowa pozycja y pioniera na planszy
     * */
    public void setCoordinates(int x, int y) {
        this.coordinates[0] = x;
        this.coordinates[1] = y;
    }


    /**
     * Getter. Pobiera koordynaty pioniera.
     *
     * @return Dwuelementową tablicę liczb całkowitych. Pierwszy element zawiera pozycję x pioniera, drugi pozycję y pioniera.
     * */
    public int[] getCoordinates() {
        return coordinates;
    }


    /**
     * Getter. Pobiera obraną przez pioniera ścieżkę przemarszu.
     *
     * @return Listę dwuelementowych tablic zawierającą liczby całkowite. Pierwszy element każdej tablicy zawiera pozycję x, drugi pozycję y pola, należącego do ścieżki przemarszu pioniera.
     * */
    public ArrayList<Integer[]> getPath() {
        return path;
    }


    /**
     * Odpowiada za mechanikę poruszania się pioniera po planszy. Pobiera pierwsze pole ze ścieżki przemarszu i próbuje przemieścić na nie pioniera.
     * Wywołuje przy tym odpowiednie mechanizmy, odpowiadające za wkroczenie pioniera na określony typ pola
     * W przypadku, gdy parametr starting przyjmie wartość 'ture' metoda będzie symulować pierwszy ruch pioniera w danej turze.
     * Wywoła ona wówczas odpowiednie mechanizmy odpowiadające za symulowanie wymarszu pioniera z pola, na którym aktualnie się znajduje.
     *
     * @param map zbiór pól składających się na planszę symulacji
     * @param starting wartość logiczna określająca czy wykonywany ruch będzie pierwszym w tej turze
     * **/
    public void walk(Field[][] map, boolean starting) {
        // Sprawdzamy czy pionier może wyjść z pola, na którym aktualnie stoi
        if(!map[coordinates[0]][coordinates[1]].goOut(this, starting)) return;

        // Sprawdzamy czy ścieżka nie jest pusta
        if(path.size() == 0) return;

        // Wybieramy z mapy pole, na które chcemy wejść. Następnie próbujemy wkroczyć pionierem na pole.
        // Jeżeli pionierowi nie uda się wejść na teren pola to nie przemieszcza się dalej w tej turze.
        // Jeżeli pionierowi uda się wejść na teren pola to przemieszcza się, a pole na które wkracza zostaje usunięte ze ścieżki,
        if(map[path.get(0)[0]][path.get(0)[1]].goInto(this)) {
            coordinates[0] = path.get(0)[0];
            coordinates[1] = path.get(0)[1];
            path.remove(0);
        }
    }


    /**
     * Odpowiada za wybudowanie maszyny produkującej przedmiot o ID, zawartym w polu 'to_build', na wcześniej wybranym polu, o koordynatach zawartych w polu 'building_field'.
     * Konstrukcja polega na stworzeniu obiektu klasy 'Machine' wewnątrz odpowiedniego pola terenu zawartego na planszy symulacji.
     * Po pomyślnym wykonaniu konstrukcji funkcja usuwa z ekwipunku pioniera odpowiednią ilość przedmiotów reprezentujących niezbędne materiały budowlane.
     * Usuwa także z kolejki budowlanej 'buildingOrder' dokonaną właśnie konstrukcję.
     *
     * @param map zbiór pól składających się na planszę symulacji
     * @param buildingOrder lista określająca kolejność konstrukcji, których ma podjąć się pionier
     * */
    public void buildMachine(Field[][] map, ArrayList<Integer> buildingOrder) {

        // Sprawdzamy czy pionier znajduje się na placu budowy w polu building_field
        if(coordinates[0] != building_field[0] || coordinates[1] != building_field[1]) return;

        // Sprawdzamy czy już przeszedł całą zaplanowaną drogę start->magazyn->budowa
        if(path.size() != 0) return;

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
        for(Item item_cost : new_building.getCost()){
            boolean owns_item = false;
            for(Item item_eq : inventory){
                if(item_cost.getID() == item_eq.getID())
                {
                    // Jeżeli nie ma wystarczająco itemu oraz ten item nie przyrasta to wybiera się na jego zbudowanie
                    if(item_eq.getAmount() - item_cost.getAmount() < 0) {
                        if(item_eq.getIncome() <= 0){
                            emergency_construction = false;
                            to_build = -1;
                            path.clear();
                            buildingOrder.add(0,item_cost.getID());
                            setNextBuilding(buildingOrder, map);
                            emergency_construction = true;
                        }
                        return;
                    }
                    owns_item = true;
                    break;
                }
            }
            // Jeżeli pionier wcale nie posiada potrzebnego itemu to również nie zbuduje maszyny
            if(!owns_item) return;
        }

        // Stawiamy maszynę na polu.
        map[building_field[0]][building_field[1]].setMachine(new_building, inventory);

        // Odejmujemy zasoby, które były potrzebne do budowy
        for(Item item_cost : new_building.getCost()){
            for(Item item_eq : inventory){
                if(item_cost.getID() == item_eq.getID())
                {
                    item_eq.setAmount(item_eq.getAmount() - item_cost.getAmount());
                    break;
                }
            }
        }

        // Resetujemy wybór budowy
        to_build = -1;

        // Usuwamy maszynę z kolejki budowy symulacji
        buildingOrder.remove(0);

        // Pionier nie może już w tej turze budować
        could_build = false;

        // Jeżeli budowa była priorytetowa to się kończy
        emergency_construction = false;

        // Nie wie też gdzie budować kolejną maszynę
        building_field[0] = building_field[1] = -1;
    }


    /**
     * Wyznacza ścieżkę przemarszu pioniera, prowadzącą od jego aktualnego położenia do pola zawartego w parametrze 'destination'.
     * Tworzona ścieżka jest zapisywana w polu 'path' i składa się z dwuelementowych tablic, zawierających koordynaty kolejnych pól tworzących sieżkę.
     * Koordynaty te leżą na prostej łączącej punkt początkowy i końcowy ścieżki, wyrażonej odpowiednią funkcją liniową.
     *
     * @param destination pole planszy, do którego ma prowadzić ścieżka
     * @param start pole planszy, do którego ma prowadzić ścieżka
     * */
    public void calculatePath(Field start, Field destination) {

        // Sprawdzamy różnice między koordynatami x oraz y celu względem pioniera
        int delta_x = destination.getCoordinates()[0] - start.getCoordinates()[0];
        int delta_y = destination.getCoordinates()[1] - start.getCoordinates()[1];

        // Jeżeli różnice między oboma koordynatami wynoszą zero, oznacza to, że pionier nie musi się przemieszczać i nie potrzebuje ścieżki
        if(delta_x == 0 && delta_y == 0) return;

        // Jeżeli różnica w koordynatach x wynosi 0 to funkcja ścieżki przyjmie postać x(y) = x_pioniera
        if(delta_x == 0) {

            // Jeżeli różnica w koordynatach y jest ujemna to cofamy się na osi OY o 1 jednostkę do momentu, aż osiągniemy koordynat x równy y_celu
            // Jeżeli różnica w koordynatach y jest dodatnia to idziemy naprzód na osi OY o 1 jednostkę do momentu, aż osiągniemy koordynat y równy y_celu
            // W czasie poruszania się dodajemy do ścieżki przemierzane pola o równaniu (x_pioniera, y)
            int y_motion = (delta_y < 0 ? -1 : 1);
            for (int current_y = start.getCoordinates()[1]+y_motion; current_y != destination.getCoordinates()[1]; current_y += y_motion){
                Integer[] field = new Integer[2];
                field[0] = start.getCoordinates()[0];
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
            for (int current_x = start.getCoordinates()[0]+x_motion; current_x != destination.getCoordinates()[0]; current_x += x_motion) {
                Integer[] field = new Integer[2];
                field[1] = start.getCoordinates()[1];
                field[0] = current_x;
                path.add(field);
            }

        }
        // Jeżeli obie różnice w koorydnatach są niezerowe to:
        // > Gdy wartość bezwzględna różnicy y jest większa od różnicy x to funkcja ścieżki przyjmie postać x(y) = podłoga( (y-b)/a )
        // > Gdy wartość bezwzględna różnicy x jest większa/równa od różnicy y to funkcja ścieżki przyjmie postać y(x) = podłoga( ax + b )
        else {
            double a = ((double)start.getCoordinates()[1] - destination.getCoordinates()[1])/(start.getCoordinates()[0]-destination.getCoordinates()[0]);
            double b = start.getCoordinates()[1] - start.getCoordinates()[0] * a;

            if(Math.abs(delta_x) >= Math.abs(delta_y)) {

                // Jeżeli różnica w koordynatach x jest ujemna to cofamy się na osi OX o 1 jednostkę do momentu, aż osiągniemy koordynat x równy x_celu
                // Jeżeli różnica w koordynatach x jest dodatnia to idziemy naprzód na osi OX o 1 jednostkę do momentu, aż osiągniemy koordynat x równy x_celu
                // W czasie poruszania się dodajemy do ścieżki przemierzane pola o równaniu (x, podłoga( ax + b ))
                int x_motion = (delta_x < 0 ? -1 : 1);
                for (int current_x = start.getCoordinates()[0]+x_motion; current_x != destination.getCoordinates()[0]; current_x += x_motion){
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
                for (int current_y = start.getCoordinates()[1]+y_motion; current_y != destination.getCoordinates()[1]; current_y += y_motion){
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


    /**
     * Wyznacza najlepsze pod względem prawdopodobieństwa wystąpienia zakłóceń oraz odległości od pola centralnego miejsce budowlane.
     *
     * @param map plansza symulacji
     * */
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
                if(!field.isCanBuild())
                    continue;

                // Elektrownie, tartaki, pompy ropy i maszyny wydobywcze potrzebują do działania stać na odpowiednim polu złoża
                if(to_build < 8)
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
                    final int RANGE = 3; // maksymalne oddalenie maszyny od maszyn wytwarzających jej materiały wejściowe
                    for(int x = 0; x < map.length; x++) {
                        for(int y = 0; y < map[x].length; y++){

                            // Sprawdzamy czy pole nie jest za daleko
                            {
                                int distance = (int)Math.floor(Math.sqrt( Math.pow(field.getCoordinates()[0] - x, 2) + Math.pow(field.getCoordinates()[1]-y,2) ));
                                if(distance > RANGE) continue;
                            }

                            // Sprawdzamy czy pole zawiera jakąkolwiek maszynę
                            if(map[x][y].getMachine() == null) continue;

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

        // Sprawdzamy czy pionier znalazł jakiekolwiek miejsce pod budowę
        if(potential_fields.size() == 0) return;

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


    /**
     * Ustala jaką maszynę pionier powinien zbudować jako pierwszą. Korzysta w tym celu z kolejki konstrukcji.
     * Funkcja może jednak wymusić na pionierze postawienie maszyny, produkującej przedmiot, którego zapasy znajduja się na wyczerpaniu.
     * Po wyborze nowego celu budowy, funkcja wyznacza miejsce pod nią.
     * Następnie wysyła pioniera do magazynu, symulując "zebranie niezbędnych przedmiotów", skąd ma udać się później do miejsca budowy.
     * Jeżeli nie uda się znaleźć żdanego miejsca pod budowę to oznacza, że pionier nie ma szans na wygraną w symulacji. Funkcja zwaraca wówaczas odpowiedni kod porażki.
     *
     * @param buildingOrder główna kolejka konstrukcji
     * @param map plansza symulacji
     *
     * @return 1 gdy nie ustalono nowego celu budowy, gdyż stary jeszcze jest aktywny
     * @return 0 gdy ustalono nowy cel budowy
     * @return -1 gdy nie ustalono nowego celu budowy ze względu na brak odpowiedniego miejsca na plaszny
     * */
    public int setNextBuilding(ArrayList<Integer> buildingOrder, Field[][] map) {

        // Jeżeli trwa budowa priorytetowa to na pewno nie możemy wyznaczyć kolejnej
        if(emergency_construction) return 1;
        /*
         {
            // Sprawdzamy czy nie zaczyna brakować pionierowi przedmiotu, który nie został uwzględniony w najbliższych planach kolejce budowy
            for(Item eq_item : inventory){
                if(eq_item.getIncome() < 0 && eq_item.getAmount() + 10 * eq_item.getIncome() <= 0){

                    if(buildingOrder.get(0) == eq_item.getID() || buildingOrder.get(1) == eq_item.getID()) continue;

                    // Jeżeli przedmiotu zaczyna brakować, a nie ma w planach zbudowania produkującej do maszyny to dodajemy taką na początek kolejki
                    buildingOrder.add(0,eq_item.getID());
                    path.clear();
                    to_build = -1;
                    emergency_construction = true;
                    break;
                }
            }
        }*/

        // Sprawdzamy czy pionier zakończył już ostatnią budowę
        if(to_build != -1) return 1;

        // Pobieramy ID produkowanego przez następną potrzebną maszynę przedmiotu
        to_build = buildingOrder.get(0);

        // Szukamy idealnego miejsca pod budowę wybranego obiektu.
        findBuildingPlace(map);

        // Jeżeli pionier nie znalazł żadnego miejsca pod budowę to sprawdzamy
        // czy pionier ma jakieś inne możliwości zdobycia zasobu, który miała produkować maszyna.
        if(building_field[0] == -1) {
            for(Item item : inventory){
                if(item.getID() == to_build)
                    // Jeżeli nie to pionier przegrywa symulację
                    if(item.getIncome() <= 0)
                        return -1;
            }

            // Jeżeli tak to pionier spróbuje kontynuować pracę
            return 1;
        }

        // Najpierw pionier musi udać się do magazynu po materiały, wyznaczamy mu ścieżkę do magazynu i z magazynu na plac budowy
        // Szukamy zatem koordynatów pola centralnego.
        {
            boolean central_found = false;
            for (Field[] row : map)
            {
                for(Field field : row){
                    if(field instanceof CentralField)
                    {
                        calculatePath(map[coordinates[0]][coordinates[1]],field);
                        calculatePath(field, map[building_field[0]][building_field[1]]);
                        central_found = true;
                        break;
                    }
                }
                if(central_found) break;
            }
        }

        return 0;
    }


    /**
     * Setter. Ustala punkty ruchu pioniera, którymi dysponuje na początku tury.
     * Są one przyznawane na podstawie typu i danych pola 'start_field', na którymn znajduje się pionier.
     *
     * @param start_filed pole, które posłuży do wyznaczenia przyznanych punktów ruchu
     * */
    private void setStartMovePoints(Field start_filed)
    {
        move_points = start_filed.getBase_move_points();
    }



    public void setCould_build(boolean could_build) {
        this.could_build = could_build;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }
}
