package rendering;

import javax.swing.*;
import java.awt.*;

public class MapEditorGUI extends JFrame {

    MapEditorGUI(int size)
    {

        ImageIcon logo = new ImageIcon("logo.png");
        this.setTitle("Pionier w \u015Bwiecie maszyn");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(size*20+15, size*20+38);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);

        JButton[][] buttonTab = new JButton[size][size];

        for (int i =0; i<size; i++){
            for (int j =0; j<size; j++){

                buttonTab[i][j]=new JButton();
                buttonTab[i][j].setFocusable(false);
                buttonTab[i][j].setBounds(20*i,20*j,20,20);
                this.add(buttonTab[i][j]);

            }
        }



    }

}
