package rendering;

import java.awt.*;

/**
 * Graficzna reprezentacja dowolnego obiektu wyświetlanego na planszy
 * */
public class Sprite {
    private Image texture; // tekstura obiektu
    private int coordinates[] = new int[2]; // położenie obiektu w oknie
    private int dimensions[] = new int[2]; // wysokość i szerokość obiektu na ekranie
}
