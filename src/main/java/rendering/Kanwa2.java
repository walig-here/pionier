package rendering;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Kanwa2 extends JLabel {


    Border border = BorderFactory.createLineBorder(Color.CYAN, 2);

    Kanwa2(int sizeOfGrid){
        this.setBounds(sizeOfGrid*20,0,200,sizeOfGrid*20 );
        this.setVerticalAlignment(JLabel.TOP);
        this.setHorizontalAlignment(JLabel.CENTER);

        String text ="<html>"+"test1"+"<br>"+"test2"+"</html>";
        this.setText(text);
        this.setBorder(border);

    }
}
