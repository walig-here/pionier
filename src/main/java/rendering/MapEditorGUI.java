package rendering;

import main.Main;
import map.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapEditorGUI extends JFrame implements MouseListener, ActionListener {

    private static JComboBox currentFieldTypeChooser;
    private static JButton continueButton;
    private static JButton saveButton;
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
        this.setSize(size*25+15+205, size*25+38);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);

        String[] fieldTypes = {"Ziemia","Woda", "\u0179r\u00F3d\u0142o zak\u0142\u00F3ce\u0144", "Drewno", "Ruda \u017Celaza", "Ruda miedzi", "Ruda w\u0119gla", "Ruda z\u0142ota", "Ropa", "Diamenty"};

        currentFieldTypeChooser = new JComboBox<>(fieldTypes);
        currentFieldTypeChooser.addActionListener(this);
        currentFieldTypeChooser.setBounds(size*25+50,50,120,25);
        this.add(currentFieldTypeChooser);

        continueButton=new JButton("<html>Wygeneruj <br>map\u0119</html>");
        continueButton.setBounds(size*25+50, 100, 100,50);
        continueButton.setFocusable(false);
        continueButton.addActionListener(this);
        this.add(continueButton);

        saveButton=new JButton("<html>Zapisz <br>map\u0119</html>");
        saveButton.setBounds(size*25+50, 200, 100,50);
        saveButton.setFocusable(false);
        saveButton.addActionListener(this);
        this.add(saveButton);


        buttonTab = new JButton[size][size];
        mapTab=new int[size][size];
        //this.addMouseListener(this);

        for (int i =0; i<size; i++){
            for (int j =0; j<size; j++){

                buttonTab[i][j]=new JButton();
                buttonTab[i][j].setFocusable(false);
                buttonTab[i][j].setBounds(25*i,25*j,25,25);
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
            dispose();
            //System.out.print(mapTab);
            MapGenerator.generateMap(size, mapTab);
        }else if(e.getSource()==saveButton){
            MapSaver.saveMap(size,mapTab);
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
            mapTab[temporal.getBounds().x/25][temporal.getBounds().y/25]=currentFieldType;
            switch (currentFieldType+1){
                case 1:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(null);
                    //soil
                    break;
                case 2:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.blue);
                    //water
                    break;
                case 3:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.red);
                    //glitch
                    break;
                case 4:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.green);
                    //forest
                    break;
                case 5:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.lightGray);
                    //iron
                    break;
                case 6:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.orange);
                    //copper
                    break;
                case 7:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.black);
                    //coal
                    break;
                case 8:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.yellow);
                    //gold
                    break;
                case 9:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(new Color(68,57,1));
                    //oil
                    break;
                case 10:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.cyan);
                    //diamond
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
            mapTab[temporal.getBounds().x/25][temporal.getBounds().y/25]=currentFieldType;
            switch (currentFieldType+1){
                case 1:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(null);
                    break;
                case 2:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.blue);
                    break;
                case 3:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.red);
                    break;
                case 4:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.green);
                    break;
                case 5:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.lightGray);
                    break;
                case 6:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.orange);
                    break;
                case 7:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.black);
                    break;
                case 8:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.yellow);
                    break;
                case 9:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(new Color(68,57,1));
                    break;
                case 10:
                    buttonTab[temporal.getBounds().x/25][temporal.getBounds().y/25].setBackground(Color.cyan);
                    break;

            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
