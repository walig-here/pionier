package map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Wczytuje mapę przechowywaną na dysku i umożliwia przeprowadzenie na niej symulacji.
 */
public class MapLoader {

    /**
     * Otwiera przeglądarkę plików, użytkownik może w niej wybrać mapę.
     */
    public static void loadMap(){

        int size;
        int[][] mapTab;

        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setDialogTitle("Otw\u00F3rz map\u0119");
        fileChooser.setCurrentDirectory(new File("."));

        FileNameExtensionFilter filter =new FileNameExtensionFilter("Mapa", "map");
        fileChooser.setFileFilter(filter);

        int fileState = fileChooser.showOpenDialog(null);
        if(fileState==JFileChooser.APPROVE_OPTION){
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            Scanner fileScanner=null;
            try{
                fileScanner=new Scanner(file);
                if (file.isFile()){
                    size=fileScanner.nextInt();
                    mapTab=new int[size][size];
                    for (int i=0;i<size;i++){
                        for (int j=0;j<size;j++){
                            mapTab[i][j]=fileScanner.nextInt();
                        }
                    }
                    MapGenerator.generateMap(size,mapTab);
                }
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }finally {
                fileScanner.close();
            }
        }

    }

}
