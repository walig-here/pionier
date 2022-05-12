package rendering;

import javax.swing.*;
import java.awt.*;

public class Kanwa1 extends JPanel {


    int sizeOfGrid;
    int dimension;
    Kanwa1(int sizeOfGrid){

        this.sizeOfGrid =sizeOfGrid;
        dimension = 20*sizeOfGrid;
        this.setPreferredSize(new Dimension(dimension,dimension));
    }

    public void paint(Graphics g) {

        Graphics2D rys1 = (Graphics2D) g;

        for (int i = 0; i< sizeOfGrid; i++)
        {
            rys1.drawLine(dimension/ sizeOfGrid *i,0,dimension/ sizeOfGrid *i,dimension);
            rys1.drawLine(0,dimension/ sizeOfGrid *i,dimension,dimension/ sizeOfGrid *i);
        }

    }

}
