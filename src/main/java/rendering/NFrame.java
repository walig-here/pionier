package rendering;

import javax.swing.*;

public class NFrame extends JFrame {

    Kanwa1 kanwa1;
    public NFrame(int sizeOfGrid){
//sizeOfGrid musi być <=41 dla kratki wielkości 20
        ImageIcon logo = new ImageIcon("logo.png");
        this.setTitle("Pionier w \u015Bwiecie maszyn");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(sizeOfGrid*20+200+15,sizeOfGrid*20+38);//maksymalna wysokość okna przy skalowaniu 125%
        this.add(kanwa1=new Kanwa1(sizeOfGrid));
        this.add(kanwa1.stats);
        this.setLayout(null);
        //this.setUndecorated(true);
        //this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
