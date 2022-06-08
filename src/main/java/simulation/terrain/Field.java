package simulation.terrain;

import rendering.GridSprite;
import simulation.*;
import simulation.Pioneer;
import simulation.ProductionMachine;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public abstract class Field {


    protected int[] coordinates; //koordynaty pola terenu

    private int terrain_id;  //id terenu

    protected Machine machine; //maszyna stojąca na polu
    private GridSprite gridSprite; //render pola
    private int base_move_points; //punkty na początku rundy jeśli jest to pole startu

    private ArrayList<Byte[]> glitch_probabilities; // szanse na zakłócenie, pierwsza komórka danej pozycji to ID zakłócenia, druga to szansa na jego wystąpienie na tym polu
    private boolean canBuild;   //czy można na tym polu budować

    /**
     * Tworzy obiekt klasy Filed, znajdujący się w podanym punkcie planszy i zawierający w sobie typ terenu o określonym ID.
     * Reszta danych na temat pola wczytywana jest przez konstruktor z plików na dysku systemowym.
     *
     * @param terrain_id ID typu terenu występującego na tym polu
     * @param x koordynat x pola na planszy symulacji
     * @param y koordynat y pola na planszy symulacji
     * **/
    public Field(int x, int y, int terrain_id){

        // miejsce pola na planszy
        coordinates = new int[2];
        coordinates[0] = x;
        coordinates[1] = y;

        // ustalamy ID terenu
        this.terrain_id = terrain_id;

        // rezerwujemy miejsce pod listę zakłóceń
        glitch_probabilities = new ArrayList<>();

        // na podstawie typu terenu dobieramy blik z którego wczytane zostaną dane
        String path = "database/terrain/";
        switch (terrain_id)
        {
            // ID 0 - pole z ziemią
            case 0: path += "soil.txt"; break;

            // ID 1 - pole wodne
            case 1: path += "water.txt"; break;

            // ID 2 - pole z surowcem
            case 2: path += "deposit.txt"; break;

            // ID 3 - pole z zakłóceniem
            case 3: path += "glitch.txt"; break;

            // ID 4 - pole centralne
            case 4: path += "central.txt"; break;
        }

        // pobieramy dane z zadanego pliku
        try{
            InputStream file_stream = new FileInputStream(path);
            Scanner file = new Scanner(file_stream);

            while (file.hasNextLine())
            {
                String line = file.nextLine();
                Scanner line_scanner = new Scanner(line);
                line_scanner.useDelimiter(":");
                line_scanner.next();

                // linia zawierająca informację o bazowych punktach ruchu
                if(line.contains("\"move points\":") && line_scanner.hasNextInt()) base_move_points = line_scanner.nextInt();
                    // linia zawierająca informację na temat zdatności pod zabudowę
                else if(line.contains("\"can build\":") && line_scanner.hasNextBoolean()) canBuild = line_scanner.nextBoolean();

                line_scanner.close();
            }
            file.close();
        }
        // zwracamy wyjątek gdy pliku nie udało się otworzyć
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Blad wczytywania danych dla pola planszy! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            return;
        }
    }

    public int getTerrain_id() {
        return terrain_id;
    }

    public int[] getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    public void setMachine(Machine machine, ArrayList<Item> inventory) {
        if(machine instanceof ProductionMachine) {
            this.machine = new ProductionMachine((ProductionMachine)machine);
            ((ProductionMachine)this.machine).startProduction(inventory);
        }
        else {
            this.machine = new Machine(machine);
            this.machine.startProduction(inventory);
        }
        canBuild = false;
        Machine.count++;
    }

    // pobiera listę prawdopodobieństw wystąpienia zakłóceń
    public ArrayList<Byte[]> getGlitch_probabilities() {
        return glitch_probabilities;
    }

    public void setGlitch_probabilities(ArrayList<Byte[]> glitch_probabilities) {
        this.glitch_probabilities = glitch_probabilities;
    }

    // ustala wpływ pola na wchodzącego na nie pioniera oraz zwraca informacje czy udało mu się wkroczyć na jego teren
    abstract public boolean goInto(Pioneer pioneer);

    public int getTerrainId() {
        return terrain_id;
    }

    public void setTerrainId(int terrainId) {
        terrain_id = terrainId;
    }

    public int getBase_move_points() {
        return base_move_points;
    }

    /**
     * Obsługuje mechanizm wyjścia pioniera z danego pola. Dla większości pól planszy przypisuje
     * pionierowi punkty ruchu, które może wykorzystać w danej turze.
     *
     * @param pioneer wchodzący na pole pionier
     * @param starting_point określa czy pole, z którego wychodzimy jest punktym startowym marszu pioniera w tej turze
     *
     * @return Wartość boolowska określająca czy pionier może wyjść z pola.
     */
    public boolean goOut(Pioneer pioneer, boolean starting_point) {
        // dodajemy startowe punkty ruchu tylko wtedy, gdy pole jest polem startowym
        if(starting_point) pioneer.setMove_points(base_move_points);
        return true;
    }

    public void setBase_move_points(int base_move_points) {
        this.base_move_points = base_move_points;
    }

    public boolean isCanBuild() {
        return canBuild;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    // odpowiada z wystąpienie glitcha
    public void activateGlitch() {

        // Jeżeli na polu nie ma maszyny to na pewno nie wystąpi zakłócenie
        if(machine == null) return;

        // Jeżeli w maszynie aktywny jest już jakieś zakłócenie to również już nie sprawdzamy czy wystąpi kolejne
        if(machine.getGlitch() != null) return;

        // Sprawdzamy kolejne zakłócenia, które mogą wystąpić na tym polu
        Random rng = new Random();
        for(Byte[] glitch : glitch_probabilities){

            // Losujemy liczbę, która zdeterminuje wystąpienie zakłócenia
            // Przy jego wystąpieniu wywołujemy w maszynie stojącej na polu zakłócenie
            if(glitch[1] >= rng.nextInt(99) + 1){
                machine.activateGlitch(glitch[0]);
                break;
            }
        }
    }

    public Machine getMachine() {
        return machine;
    }
}