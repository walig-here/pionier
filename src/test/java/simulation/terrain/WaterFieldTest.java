package simulation.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaterFieldTest {

    @Test
    void contructor() {
        final int x = 0;
        final int y = 0;

        Field water = new WaterField(x,y);
        Assertions.assertFalse(water.getCoordinates()[0] != x, "Bledne ustalenie danych wodnego pola planszy. Niepoprawny koordynat x!");
        Assertions.assertFalse(water.getCoordinates()[1] != y, "Bledne ustalenie danych wodnego pola planszy. Niepoprawny koordynat y!");
        Assertions.assertFalse(water.getBase_move_points() != 0, "Bledne wczytanie danych wodnego pola planszy. Niepoprawna wartosc bazowych punktow ruchu!");
        Assertions.assertFalse(water.isCanBuild() != false, "Bledne wczytanie danych wodnego pola planszy. Niepoprawna wartosc wskazujaca zdatnosc pola pod zabudowe!");
        Assertions.assertFalse(WaterField.getSailing_time() != 2, "Bledne wczytanie danych wodnych pol planszy. Niepoprawna wartosc ilosci tur niezbednych do przeplyniecia pola!");
    }

    @Test
    void goInto() {
    }
}