package simulation.terrain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.beans.FeatureDescriptor;

import static org.junit.jupiter.api.Assertions.*;

class SoilFieldTest {

    @Test
    void goThrough() {
    }

    @Test
    void constructor(){
        final int x = 0;
        final int y = 0;

        Field soil = new SoilField(x,y);
        Assertions.assertFalse(soil.getCoordinates()[0] != x, "Bledne ustalenie danych standardowego pola planszy. Niepoprawny koordynat x!");
        Assertions.assertFalse(soil.getCoordinates()[1] != y, "Bledne ustalenie danych standardowego pola planszy. Niepoprawny koordynat y!");
        Assertions.assertFalse(soil.getBase_move_points() != 5, "Bledne wczytanie danych standardowego pola planszy. Niepoprawna wartosc bazowych punktow ruchu!");
        Assertions.assertFalse(soil.isCanBuild() != true, "Bledne wczytanie danych standardowego pola planszy. Niepoprawna wartosc wskazujaca zdatnosc pola pod zabudowe!");
    }
}