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
        Field soil = new SoilField(0,0);
        Assertions.assertFalse(soil.getBase_move_points() != 5, "Bledne wczytanie danych pola planszy. Niepoprawna wartosc bazowych punktow ruchu!");
        Assertions.assertFalse(soil.isCanBuild() != true, "Bledne wczytanie danych pola planszy. Niepoprawna wartosc wskazujaca zdatnosc pola pod zabudowe!");
    }
}