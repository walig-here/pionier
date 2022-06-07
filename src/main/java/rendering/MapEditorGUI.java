package rendering;

import map.MapGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapEditorGUI extends JFrame implements MouseListener, ActionListener {

    private static JComboBox currentFieldTypeChooser;
    private static JButton continueButton;
    private static JButton[][] buttonTab;

    public static int[][] getMapTab() {
        return mapTab;
    }

    private static int[][] mapTab;
    private static int currentFieldType;
    private static boolean clickState;
    private static int size;

    MapEditorGUI(int size)
    {

        this.size=size;
        ImageIcon logo = new ImageIcon("logo.png");
        this.setTitle("Pionier w \u015Bwiecie maszyn");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(size*20+15+205, size*20+38);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);

        String[] fieldTypes = {"Ziemia","Woda", "\u0179r\u00F3d\u0142o zak\u0142\u00F3ce\u0144", "Drewno", "Ruda \u017Celaza", "Ruda miedzi", "Ruda w\u0119gla", "Ruda z\u0142ota", "Ropa", "Diamenty"};

        currentFieldTypeChooser = new JComboBox<>(fieldTypes);
        currentFieldTypeChooser.addActionListener(this);
        currentFieldTypeChooser.setBounds(size*20+50,50,120,25);
        this.add(currentFieldTypeChooser);

        continueButton=new JButton("<html>Wygeneruj <br>map\u0119</html>");
        continueButton.setBounds(size*20+50, 100, 100,50);
        continueButton.setFocusable(false);
        continueButton.addActionListener(this);
        this.add(continueButton);


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
        }else if (e.getSource()==continueButton){
            MapGenerator.generateMap(size, mapTab);
            dispose();
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
            mapTab[temporal.getBounds().y/20][temporal.getBounds().x/20]=currentFieldType;
            switch (currentFieldType+1){
                case 1:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(null);
                    break;
                case 2:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.blue);
                    break;
                case 3:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.red);
                    break;
                case 4:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(new Color(184,105,27));
                    break;
                case 5:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.lightGray);
                    break;
                case 6:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.orange);
                    break;
                case 7:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.black);
                    break;
                case 8:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.yellow);
                    break;
                case 9:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(new Color(68,57,1));
                    break;
                case 10:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.cyan);
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
            mapTab[temporal.getBounds().y/20][temporal.getBounds().x/20]=currentFieldType;
            switch (currentFieldType+1){
                case 1:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(null);
                    break;
                case 2:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.blue);
                    break;
                case 3:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.red);
                    break;
                case 4:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(new Color(184,105,27));
                    break;
                case 5:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.lightGray);
                    break;
                case 6:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.orange);
                    break;
                case 7:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.black);
                    break;
                case 8:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.yellow);
                    break;
                case 9:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(new Color(68,57,1));
                    break;
                case 10:
                    buttonTab[temporal.getBounds().x/20][temporal.getBounds().y/20].setBackground(Color.cyan);
                    break;

            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
