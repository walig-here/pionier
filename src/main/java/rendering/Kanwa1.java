package rendering;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Kanwa1 extends JPanel {

    JLabel stats;

    int sizeOfGrid;
    int dimension;
    Kanwa1(int sizeOfGrid){

        this.sizeOfGrid =sizeOfGrid;
        dimension = sizeOfGrid*20;
        this.setBounds(0,0,dimension,dimension);


        stats=new JLabel();
        Border border = BorderFactory.createLineBorder(Color.CYAN, 2);


        stats.setBounds(sizeOfGrid*20,0,200,sizeOfGrid*20 );
        stats.setVerticalAlignment(JLabel.TOP);
        stats.setHorizontalAlignment(JLabel.CENTER);

        String text ="<html>"+"test1"+"<br>"+"test2"+"</html>";
        stats.setText(text);
        stats.setBorder(border);

        //this.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
        //this.setPreferredSize(new Dimension(dimension,dimension));
    }



    public void paint(Graphics g) {

        Graphics2D rys1 = (Graphics2D) g;

        for (int i = 0; i< sizeOfGrid; i++)
        {
            rys1.drawLine(20 *i,0,20 *i,dimension);
            rys1.drawLine(0,20*i,dimension-1,20 *i);
        }

        //wymiary 0-854x0-831 przy 870x870
        rys1.drawLine(0, dimension-1,dimension-1,dimension-1);
        rys1.drawLine(dimension-1, 0,dimension-1,dimension-1);

    }

}
