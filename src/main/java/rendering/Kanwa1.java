package rendering;
import map.MapGenerator;
import simulation.terrain.*;

import main.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static main.Main.simulation_setup;

public class Kanwa1 extends JPanel implements ActionListener{

    JLabel stats;

    Timer timer;
    int sizeOfGrid;
    int dimension;
    int max_turns;
    int turn;
    int score = 0;
    boolean isRunning;
    Kanwa1(int max_turns){

        isRunning=true;
        this.max_turns=max_turns;
        turn=0;
        sizeOfGrid= Main.getMap_size();
        dimension = sizeOfGrid*25;
        this.setBounds(0,0,dimension,dimension);
        //this.setPreferredSize(new Dimension(dimension,dimension));


        stats=new JLabel();
        Border border = BorderFactory.createLineBorder(Color.CYAN, 2);


        stats.setBounds(sizeOfGrid*25,0,200,sizeOfGrid*25 );
        stats.setVerticalAlignment(JLabel.TOP);
        stats.setHorizontalAlignment(JLabel.CENTER);

        String text ="<html>"+"test1"+"<br>"+"test2"+"</html>";
        stats.setText(text);
        stats.setBorder(border);

        timer=new Timer(200, this);
        timer.start();

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
                        break;
                    case 2:
                        switch (((DepositField)Main.map[i][j]).getItem_id()){

                            case 2:
                                rys1.setPaint(Color.green);
                                break;
                            case 6:
                                rys1.setPaint(Color.lightGray);
                                break;
                            case 3:
                                rys1.setPaint(Color.orange);
                                break;
                            case 1:
                                rys1.setPaint(Color.DARK_GRAY);
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
                    case 3: rys1.setPaint(Color.red); break;
                    case 4: rys1.setPaint(Color.PINK); break;

                }

                rys1.fillRect(i*25,j*25,25,25);

                // wstawić renderowanie maszyn
                if(Main.map[i][j].getMachine() != null){

                    if(Main.map[i][j].getMachine().getActive() == 1) rys1.setPaint(new Color(22, 1,98));
                    else rys1.setPaint(new Color(136, 143, 194));

                    rys1.setStroke(new BasicStroke(3));
                    rys1.drawLine(i*25,j*25,i*25+25,j*25+25);
                    rys1.drawLine(i*25+25,j*25,i*25,j*25+25);
                    rys1.setStroke(new BasicStroke(1));

                }


                // renderowanie pioniera
                if(Main.pioneer != null){
                    if(i==Main.pioneer.getCoordinates()[0]&&j==Main.pioneer.getCoordinates()[1]){
                        rys1.setPaint(Color.MAGENTA);
                        rys1.setStroke(new BasicStroke(3));
                        rys1.drawLine(i*25,j*25,i*25+25,j*25+25);
                        rys1.drawLine(i*25+25,j*25,i*25,j*25+25);
                        rys1.setStroke(new BasicStroke(1));
                    }
                }
            }
        }

        for (int i = 0; i< sizeOfGrid; i++)
        {

            rys1.setPaint(Color.black);
            rys1.drawLine(25 *i,0,25 *i,dimension);
            rys1.drawLine(0,25*i,dimension-1,25 *i);
        }

        //wymiary 0-854x0-831 przy 870x870
        rys1.drawLine(0, dimension-1,dimension-1,dimension-1);
        rys1.drawLine(dimension-1, 0,dimension-1,dimension-1);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //Main.pioneer.setCoordinates(Main.pioneer.getCoordinates()[0]+1, Main.pioneer.getCoordinates()[1]+1);
        repaint();

        if(isRunning){
            switch (Main.simulationLoop(turn, max_turns)) {
                case -1: {
                    System.out.println("PORA\u017BKA!\nPionier nie ma już gdzie zbudować niezbędnych maszyn.");
                    JOptionPane.showMessageDialog(this,"PORA\u017BKA!\nPionier nie ma ju\u017C gdzie zbudowa\u0107 niezb\u0119dnych maszyn.");
                    isRunning = false;
                } break;
                case -2: {
                    System.out.println("PORA\u017BKA!\nPionier nie zdążył wyprodukować pożądanego przedmiotu w danym mu czasie!");
                    JOptionPane.showMessageDialog(this,"PORA\u017BKA!\nPionier nie zd\u0105\u017Cy\u0142 wyprodukowa\u0107 po\u017C\u0105danego przedmiotu w danym mu czasie!");
                    isRunning = false;
                }break;
                case -3: {
                    System.out.println("PORA\u017BKA!\nPionier nie był w stanie założyć kompleksu przemysłowego!");
                    JOptionPane.showMessageDialog(this,"PORA\u017BKA!\nPionier nie by\u0142 w stanie zało\u017Cy\u0107 kompleksu przemys\u0142owego!");
                    isRunning = false;
                }break;
                case 1: {
                    System.out.println("ZWYCIESTWO!");
                    JOptionPane.showMessageDialog(this,"ZWYCI\u0118STWO!");
                    score += 10000;
                    isRunning = false;
                }break;
            }
            super.repaint();
            turn++;

            if(!isRunning){
                Main.saveLog();
                score += Main.getScore();
                System.out.println("PUNKTY: " + score);
                return;
            }
        }
    }
}
