package rendering;

import main.Main;

import javax.swing.*;

/**
 * Odpowiada za wyświetlanie okienek.
 */
public class NFrame extends JFrame {

    Kanwa1 kanwa1;

    /**
     * Konstruktor klasy NFrame. Odpowiada za wyświetlanie okien, odpowiednie rozmieszczenie kanw.
     * @param max_turns - maksymalna liczba tur
     */
    public NFrame(int max_turns){
//sizeOfGrid musi być <=41 dla kratki wielkości 20
        int sizeOfGrid= Main.getMap_size();

        kanwa1=new Kanwa1(max_turns);

        ImageIcon logo = new ImageIcon("textures\\logo.png");
        this.setTitle("Pionier w \u015Bwiecie maszyn");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(sizeOfGrid*25+200+15,sizeOfGrid*25+38);//maksymalna wysokość okna przy skalowaniu 125%
        this.add(kanwa1);
        this.add(kanwa1.stats);
        this.setLayout(null);
        //this.setUndecorated(true);
        //this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void exit(){
        dispose();
    }
}
