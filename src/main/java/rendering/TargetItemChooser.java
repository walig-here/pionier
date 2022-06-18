package rendering;

import simulation.Item;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import static main.Main.simulation_setup;

/**
 * Odpowiada za wyświetlanie ona wyboru przedmiotu docelowego symulacji
 */
public class TargetItemChooser extends JFrame implements ActionListener {

    private static JComboBox targetItemChooser;
    /**
     * Przycisk startujący symulację
     */
    private static JButton start_simulation;
    private static JButton[][] buttonTab;
    private static int size;
    /**
     * ID przedmiotu docelowego
     */
    private static int target_item_id;
    /**
     * Maksymalna liczba tur symulacji
     */
    int max_turns;

    /**
     * Konstruktor klasy TargetItemChooser. Wyświetla okno z wyborem celu symulacji
     * @param size
     * @param target_item_id - ID przedmiotu docelowego symulacji
     */
    public TargetItemChooser(int size, int target_item_id)
    {
        ImageIcon logo = new ImageIcon("logo.png");
        this.setTitle("Wybierz cel symulacji...");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(350, 125);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);


        ArrayList<String> fieldTypes = new ArrayList<>();
        for(int i = 0; i < 25; i++){
            Item item = new Item(i, 0,0);
            fieldTypes.add(item.getName());
        }

        targetItemChooser = new JComboBox<>(fieldTypes.toArray());
        targetItemChooser.addActionListener(this);
        targetItemChooser.setBounds(30,15,150,50);
        this.add(targetItemChooser);

        start_simulation =new JButton("<html>Rozpocznij symulacj\u0119</html>");
        start_simulation.setBounds(200, 15, 100,50);
        start_simulation.setFocusable(false);
        start_simulation.addActionListener(this);
        this.add(start_simulation);


        buttonTab = new JButton[size][size];
    }

    /**
     * Wyświetla okno pytające o maksymalny czas symulacji - waliduje, gdy wprowadzono nieprawidłowe dane. Jeśli z jakichś powodów symulacja nie może wystartować, wyświetla informację o przegranej.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==start_simulation && target_item_id != -1){

            Scanner answer_scanner;
            while(true) {
                String answer= JOptionPane.showInputDialog(null, "Podaj maksymalny czas trwania symulacji(w turach):");
                answer_scanner = new Scanner(answer);
                if(answer_scanner.hasNextInt()) break;
                else JOptionPane.showMessageDialog(null, "Wprowadzono nieodpowiednie dane!");
            }
            max_turns = answer_scanner.nextInt();
            answer_scanner.close();
            setVisible(false);
            dispose();

            NFrame simWindow;
            if(simulation_setup(target_item_id) == -1) {
                System.out.println("PORAŻKA!\nPionier nie był w stanie przybyć do tej okolicy!");
                JOptionPane.showMessageDialog(null,"PORA\u017BKA!\nPionier nie by\u0142 w stanie przyby\u0107 do tej okolicy!");
            }
            else simWindow =new NFrame(max_turns);
        }
        else if (e.getSource()==targetItemChooser){
            target_item_id = targetItemChooser.getSelectedIndex();
        }
    }
}