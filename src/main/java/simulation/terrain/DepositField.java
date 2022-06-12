package simulation.terrain;

import main.Main;
import simulation.Item;
import simulation.Pioneer;
import simulation.ProductionMachine;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class DepositField extends Field {


    private final int item_id; // ID wydobywanego stąd itemu
    private int deposit_capacity; // maksymalna ilość surowca do wydobycia
    private static int move_cost = -1; // punkty ruchu odbierane pionierowi przy wejściu na niezabudowane pole tego typu
    private static float excavation_penalty=0.00f; // współczynnik określający ile punktów ruchu zabudowane pole odbierze pionierowi w stosunku do swojej niezabudowanej wersji

    // konstruktor
    public DepositField(int x, int y, int capacity, int item_id){
        super(x,y,2);

        // ustalamy wielkość złoża
        deposit_capacity = capacity;

        // co wydobywamy na polu
        this.item_id = item_id;

        // Tworzymy pomocnicze pole ziemi, z którego pobierzemy punkty ruchu potrzebne do wejścia na pole.
        // Jeżeli wartość move_cost jest inna niż -1 to znaczy, że nie musimy już wczytywać tej wartości
        if(move_cost == -1) {
            SoilField temp = new SoilField(0,0);
            move_cost = SoilField.getMove_cost();
        }

        // Wczytujemy z pliku dane o punktach zabieranych przez pole gdy jest ono zabudowane.
        // Jeżeli te informacje zostały już cześniej wczytane do klasy(są różne od 0) to nie musimy ich wczytywać drugi raz.
        if(excavation_penalty == 0.0f)
        {
            try{
                InputStream file_stream = new FileInputStream("database/terrain/deposit.txt");
                Scanner file = new Scanner(file_stream);

                while (file.hasNextLine())
                {
                    String line = file.nextLine();
                    Scanner line_scanner = new Scanner(line);
                    line_scanner.useDelimiter(":");
                    line_scanner.useLocale(Locale.US);
                    line_scanner.next();

                    // linia zawierająca informację o czasie potrzebnym do przepłynięcia pola
                    if(line.contains("\"penalty\":") && line_scanner.hasNextFloat()) excavation_penalty = line_scanner.nextFloat();

                    line_scanner.close();
                }
                file.close();
            }
            // zwracamy wyjątek gdy pliku nie udało się otworzyć
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Blad wczytywania danych dla pola ze złożem planszy! Nie udalo sie uzyskac dostepu do pliku z danymi!");
            }
        }
    }

    public void setCapacityOfDeposit(int capacityOfDeposit) {
        deposit_capacity = capacityOfDeposit;
    }
    public int getCapacityOfDeposit() {
        return deposit_capacity;
    }

    // metoda uszczuplająca złoże wraz z działaniem maszyny
    public void extract(ArrayList<Item> inventory) {

        // Jeżeli na polu jest maszyna wydobywająca surowiec lub generująca prąd to wyczerpuje ona zasoby.
        // Sprawdzamy czy pole zawiera maszyne.
        if(machine == null)
            return;

        // Sprawdzamy czy maszyna stojąca na polu jest aktywna
        if(machine.getActive() == 0)
            return;

        // Sprawdzamy czy na tym polu jest jeszcze cokolwiek do wydobycia
        // Jeżeli nie ma to maszyna się zatrzymuje
        if(deposit_capacity <= 0) {
            if(machine instanceof ProductionMachine) ((ProductionMachine)machine).stopProduction(inventory);
            else machine.stopProduction(inventory);
            machine.setActive(-1);
            Main.addToLog("\tMaszyna " + machine.getName() + " zosta\u0142a trwale wy\u0142\u0105czona ze wzgl\u0119du na wyczerpanie si\u0119 zasob\u00F3w pola (" + coordinates[0] + ", " + coordinates[1] + "), na kt\u00F3rym si\u0119 znajduje.");
            return;
        }

        // Sprawdzamy czy ta maszyna jest odpowiedniego typu
        if(machine.getProduced_item() != 0 && machine.getProduced_item() != item_id) return;

        if(machine.getProduced_item() == 0 ) deposit_capacity -= machine.getOutput()/2;

            // Maszyna wydobywa tyle jednostek surowca ile generuje produktu wyjściowego
        else deposit_capacity -= machine.getOutput();
    }

    public int getItem_id() {
        return item_id;
    }

    /**
     * Spowalnia znacząco bardziej pioniera, jeżeli na polu prowadzone prace wydobywcze(na polu stoi maszyna).
     * W przeciwnym wypadku pionier przemieszcza się po polu jak po zwykłym polu z ziemią.
     *
     * @param pioneer wchodzący na pole pionier
     * */
    @Override
    public boolean goInto(Pioneer pioneer) {
        // Jeżeli na polu nie stoi maszyna to koszt wejścia na pole nie zmienia się
        int move_cost = DepositField.move_cost;
        // Jeżeli na polu stoi maszyna to koszt wejścia na pole jest zmieniany przez modyfikator excavation_penalty
        if(super.machine != null) move_cost = (int)Math.floor(move_cost * excavation_penalty);

        // Sprawdzamy czy pionier ma odpowiednią ilość punktów ruchu.
        // Jeżeli nie ma odpowiedniej ilości to pole odbiera mu wszystkie punkty, ale nie pozwala mu wkroczyć na swój teren.
        if(pioneer.getMove_points() - move_cost < 0)
        {
            pioneer.setMove_points(0);
            return false;
        }

        // Jeżeli wystarczy mu punktów ruchu to pole odbiera mu ich odpowiednią ilosć i pozwala wkroczyć na swój teren.
        pioneer.setMove_points(pioneer.getMove_points() - move_cost);
        return true;
    }
}