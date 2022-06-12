package rendering;
import map.MapGenerator;
import simulation.Item;
import simulation.Machine;
import simulation.terrain.*;

import main.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import static main.Main.pioneer;
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
    String temp,temp2;

    Image coal_ore;
    Image oil_field;
    Image iron_ore;
    Image copper_ore;
    Image gold_ore;
    Image diamond_ore;
    Image forest;
    Image pionier;
    Image central_field;
    Image factory_on;
    Image factory_off;
    Image furnace_on;
    Image furnace_off;
    Image powerplant_on;
    Image powerplant_off;
    Image extractor_on;
    Image extractor_off;
    Image soil;
    Image water;
    Image glitch;

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


        stats.setBorder(border);

        //ładowanie textur

        coal_ore =new ImageIcon("src\\textures\\coal_ore.png").getImage();
        iron_ore =new ImageIcon("src\\textures\\iron_ore.png").getImage();
        copper_ore =new ImageIcon("src\\textures\\copper_ore.png").getImage();
        gold_ore =new ImageIcon("src\\textures\\gold_ore.png").getImage();
        diamond_ore =new ImageIcon("src\\textures\\diamond_ore.png").getImage();
        forest =new ImageIcon("src\\textures\\woods.png").getImage();
        pionier =new ImageIcon("src\\textures\\pionier.png").getImage();
        central_field =new ImageIcon("src\\textures\\central_field.png").getImage();
        factory_on =new ImageIcon("src\\textures\\factory_on.png").getImage();
        factory_off =new ImageIcon("src\\textures\\factory_off.png").getImage();
        furnace_on =new ImageIcon("src\\textures\\furnace_on.png").getImage();
        furnace_off =new ImageIcon("src\\textures\\furnace_off.png").getImage();
        powerplant_on =new ImageIcon("src\\textures\\powerplant_on.png").getImage();
        powerplant_off =new ImageIcon("src\\textures\\powerplant_off.png").getImage();
        extractor_on =new ImageIcon("src\\textures\\extractor_on.png").getImage();
        extractor_off =new ImageIcon("src\\textures\\extractor_off.png").getImage();
        soil =new ImageIcon("src\\textures\\soil.png").getImage();
        oil_field =new ImageIcon("src\\textures\\oil.png").getImage();
        water =new ImageIcon("src\\textures\\water.png").getImage();
        glitch =new ImageIcon("src\\textures\\glitch.png").getImage();


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
                rys1.drawImage(soil,i*25,j*25, null);
                switch (Main.map[i][j].getTerrainId()){
                    case 1:
                        rys1.drawImage(water,i*25,j*25, null);
                        break;
                    case 2:
                        switch (((DepositField)Main.map[i][j]).getItem_id()){

                            case 2:
                                rys1.drawImage(forest,i*25,j*25, null);
                                break;
                            case 6:
                                rys1.drawImage(iron_ore,i*25,j*25, null);
                                break;
                            case 3:
                                rys1.drawImage(copper_ore,i*25,j*25, null);
                                break;
                            case 1:
                                rys1.drawImage(coal_ore,i*25,j*25, null);
                                break;
                            case 5:
                                rys1.drawImage(gold_ore,i*25,j*25, null);
                                break;
                            case 4:
                                rys1.drawImage(oil_field,i*25,j*25, null);
                                break;
                            case 7:
                                rys1.drawImage(diamond_ore,i*25,j*25, null);
                                break;
                        }
                        break;
                    case 3:
                        rys1.drawImage(glitch,i*25,j*25, null);
                        break;
                    case 4: rys1.drawImage(central_field,i*25,j*25, null); break;
                }


                // wstawić renderowanie maszyn
                if(Main.map[i][j].getMachine() != null){

                    if(Main.map[i][j].getMachine().getActive() == 1){
                        if(Main.map[i][j].getMachine().getID()>=5){

                            rys1.drawImage(factory_on,i*25,j*25, null);

                        }else{
                            switch (Main.map[i][j].getMachine().getID()){

                                case 0:
                                    rys1.drawImage(powerplant_on,i*25,j*25, null);
                                    break;
                                case 3:
                                    rys1.drawImage(furnace_on,i*25,j*25, null);
                                    break;
                                case 1, 2, 4:
                                    rys1.drawImage(extractor_on,i*25,j*25, null);
                                    break;
                            }
                        }
                    }
                    else {
                        if(Main.map[i][j].getMachine().getID()>=5){

                            rys1.drawImage(factory_off,i*25,j*25, null);

                        }else{
                            switch (Main.map[i][j].getMachine().getID()){

                                case 0:
                                    rys1.drawImage(powerplant_off,i*25,j*25, null);
                                    break;
                                case 3:
                                    rys1.drawImage(furnace_off,i*25,j*25, null);
                                    break;
                                case 1, 2, 4:
                                    rys1.drawImage(extractor_off,i*25,j*25, null);
                                    break;
                            }
                        }
                    }
                }


                // renderowanie pioniera
                if(pioneer != null){
                    if(i== pioneer.getCoordinates()[0]&&j== pioneer.getCoordinates()[1]){
                        rys1.drawImage(pionier,i*25+5,j*25+5, null);
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
        if(Main.buildingQueue.size()!=0){
            Item i = new Item(Main.buildingQueue.get(0),0,0);
            temp= "Po\u017C\u0105dany przedmiot: ";
            temp+=i.getName();
        }else {
            temp="";
        }

        temp2="";
        for(Item item : pioneer.getInventory()){
            DecimalFormat df = new DecimalFormat("#.##");
            temp2 += item.getName()+ ": " +df.format(item.getAmount())+"   "+ df.format(item.getIncome())+"<br>";
        }

        String text ="<html>"+"Numer tury: "+turn+"<br>"+ "Ilo\u015B\u0107 maszyn: " + Machine.count +"(" + Machine.active_machines + " aktywnych)"+
                "<br>"+temp+
                "<br>"+"Ekwipunek: "+"<br>"+temp2+

                "</html>";
        stats.setText(text);
        stats.repaint();
        if(isRunning){
            switch (Main.simulationLoop(turn, max_turns)) {
                case -1: {
                    isRunning = false;
                    System.out.println("PORA\u017BKA!\nPionier nie ma już gdzie zbudować niezbędnych maszyn.");
                    JOptionPane.showMessageDialog(this,"PORA\u017BKA!\nPionier nie ma ju\u017C gdzie zbudowa\u0107 niezb\u0119dnych maszyn.");
                } break;
                case -2: {
                    isRunning = false;
                    System.out.println("PORA\u017BKA!\nPionier nie zdążył wyprodukować pożądanego przedmiotu w danym mu czasie!");
                    JOptionPane.showMessageDialog(this,"PORA\u017BKA!\nPionier nie zd\u0105\u017Cy\u0142 wyprodukowa\u0107 po\u017C\u0105danego przedmiotu w danym mu czasie!");
                }break;
                case -3: {
                    isRunning = false;
                    System.out.println("PORA\u017BKA!\nPionier nie był w stanie założyć kompleksu przemysłowego!");
                    JOptionPane.showMessageDialog(this,"PORA\u017BKA!\nPionier nie by\u0142 w stanie zało\u017Cy\u0107 kompleksu przemys\u0142owego!");
                }break;
                case 1: {
                    isRunning = false;
                    System.out.println("ZWYCIESTWO!");
                    JOptionPane.showMessageDialog(this,"ZWYCI\u0118STWO!");
                    score += 10000;
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