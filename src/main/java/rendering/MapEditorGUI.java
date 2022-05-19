package rendering;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapEditorGUI extends JFrame implements MouseListener, ActionListener {
//uzupełnić typy pól gruntu, przesyłanie tablicy dalej

    private static JComboBox currentFieldTypeChooser;
    private static JButton[][] buttonTab;
    private static int[][] mapTab;
    private static int currentFieldType;
    private static boolean clickState;

    MapEditorGUI(int size)
    {

        ImageIcon logo = new ImageIcon("logo.png");
        this.setTitle("Pionier w \u015Bwiecie maszyn");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(size*20+15+205, size*20+38);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);

        String[] fieldTypes = {"Woda", "\u0179r\u00F3d\u0142o zak\u0142\u00F3ce\u0144", "Ruda \u017Celaza", "Ruda miedzi"};

        currentFieldTypeChooser = new JComboBox<>(fieldTypes);
        currentFieldTypeChooser.addActionListener(this);
        currentFieldTypeChooser.setBounds(size*20+50,size*5,120,25);
        this.add(currentFieldTypeChooser);

        buttonTab = new JButton[size][size];
        mapTab=new int[size][size];
        //this.addMouseListener(this);

        for (int i =0; i<size; i++){
            for (int j =0; j<size; j++){

                buttonTab[i][j]=new JButton();
                buttonTab[i][j].setFocusable(false);
                buttonTab[i][j].setBounds(20*i,20*j,20,20);
                buttonTab[i][j].addMouseListener(this);
                this.add(buttonTab[i][j]);

            }
        }

    }


    //for coombobox
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==currentFieldTypeChooser){
            currentFieldType=currentFieldTypeChooser.getSelectedIndex();
        }
    }

    //MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {


    }

    @Override
    public void mousePressed(MouseEvent e) {

        clickState=true;

        JButton temporal = (JButton)e.getSource();
        mapTab[temporal.getBounds().x/20][temporal.getBounds().y/20]=currentFieldType+1;
        switch (currentFieldType+1){
            case 1:
                buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.blue);
                break;
            case 2:
                buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.red);
                break;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickState=false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if(clickState){
            JButton temporal = (JButton)e.getSource();
            mapTab[temporal.getBounds().x/20][temporal.getBounds().y/20]=currentFieldType+1;
            switch (currentFieldType+1){
                case 1:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.blue);
                    break;
                case 2:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.red);
                    break;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
