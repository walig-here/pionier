package rendering;

import javax.swing.*;
import java.awt.*;

public class Kanwa1 extends JPanel {

    final int dimension = 600;
    int rozmiar;
    Kanwa1(int rozmiar){

        this.rozmiar=rozmiar;
        this.setPreferredSize(new Dimension(dimension,dimension));
    }

    public void paint(Graphics g) {

        Graphics2D rys1 = (Graphics2D) g;

        for (int i=0;i<rozmiar;i++)
        {
            rys1.drawLine(dimension/rozmiar*i,0,dimension/rozmiar*i,dimension);
            rys1.drawLine(0,dimension/rozmiar*i,dimension,dimension/rozmiar*i);
        }

    }

}
