package rendering;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

////////////////////////
///       Menu       ///
////////////////////////


public class MenuGUI extends JFrame implements ActionListener {

    JButton button1, button2, button3;

    public MenuGUI(){

        ImageIcon logo = new ImageIcon("logo.png");

        button1=new JButton(); //otwiera edytor map
        button1.setBounds(100,100,175,50);
        button1.addActionListener(this);
        button1.setFocusable(false);
        button1.setText("Stw\u00F3rz now\u0105 map\u0119");

        button2=new JButton(); //otwiera autoGenerator map
        button2.setBounds(100,175,175,50);
        button2.addActionListener(this);
        button2.setFocusable(false);
        button2.setText("Wygeneruj losow\u0105 map\u0119");

        button3=new JButton(); //wczytuje zapisaną mapę
        button3.setBounds(100,250,175,50);
        button3.addActionListener(this);
        button3.setFocusable(false);
        button3.setText("Wczytaj map\u0119");

        this.setTitle("Pionier w \u015Bwiecie maszyn");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setSize(400, 500);
        this.setVisible(true);
        this.add(button1);
        this.add(button2);
        this.add(button3);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==button1){

            String answer = JOptionPane.showInputDialog("Podaj wielko\u015B\u0107 boku mapy");
            try {
                new MapEditorGUI(Integer.parseInt(answer));
            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
            dispose();
        }
        if (e.getSource()==button2){

            //otwiera autoGenerator map

            map.MapGenerator.generateMap();

            dispose();
        }
        if (e.getSource()==button3){

            //wczytuje zapisaną mapę

            String answer = JOptionPane.showInputDialog("Podaj nazw\u0119 mapy");
            map.MapLoader.loadMap(answer);

            dispose();
        }
    }
}
