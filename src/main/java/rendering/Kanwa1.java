package rendering;
import simulation.terrain.*;

import main.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Kanwa1 extends JPanel {

    JLabel stats;

    int sizeOfGrid;
    int dimension;
    Kanwa1(){

        sizeOfGrid= Main.map_size;
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

        for (int i =0; i<sizeOfGrid;i++)
        {
            for (int j=0;j<sizeOfGrid;j++)
            {
                switch (Main.map[i][j].getTerrainId()){

                    case 0:
                        rys1.setPaint(Color.white);
                        break;
                    case 1:
                        rys1.setPaint(Color.blue);
                    case 2:
                        switch (((DepositField)Main.map[i][j]).getItem_id()){

                            case 2:
                                rys1.setPaint(new Color(184,105,27));
                                break;
                            case 6:
                                rys1.setPaint(Color.lightGray);
                                break;
                            case 3:
                                rys1.setPaint(Color.orange);
                                break;
                            case 1:
                                rys1.setPaint(Color.black);
                                break;
                            case 5:
                                rys1.setPaint(Color.yellow);
                                break;
                            case 4:
                                rys1.setPaint(new Color(68,57,1));
                                break;
                            case 7:
                                rys1.setPaint(Color.cyan);
                                break;
                        }
                        break;
                    case 3:
                        rys1.setPaint(Color.red);
                        break;

                }

                rys1.fillRect(i*20,j*20,20,20);
                if(i==Main.pioneer.getCoordinates()[0]&&j==Main.pioneer.getCoordinates()[1]){
                    rys1.setPaint(Color.black);
                    rys1.setStroke(new BasicStroke(3));
                    rys1.drawLine(i*20,j*20,i*20+20,j*20+20);
                    rys1.drawLine(i*20+20,j*20,i*20,j*20+20);
                    rys1.setStroke(new BasicStroke(1));
                }
            }
        }

        for (int i = 0; i< sizeOfGrid; i++)
        {

            rys1.setPaint(Color.black);
            rys1.drawLine(20 *i,0,20 *i,dimension);
            rys1.drawLine(0,20*i,dimension-1,20 *i);
        }

        //wymiary 0-854x0-831 przy 870x870
        rys1.drawLine(0, dimension-1,dimension-1,dimension-1);
        rys1.drawLine(dimension-1, 0,dimension-1,dimension-1);

    }

}
