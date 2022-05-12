package rendering;

import javax.swing.*;
import java.awt.*;

public class NFrame extends JFrame {

    Kanwa1 kanwa;
    public NFrame(int sizeOfGrid){

        kanwa= new Kanwa1(sizeOfGrid);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,600);
        this.add(kanwa);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


}
