package map;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Umożliwia zapisanie na dysku mapy stworzonej przez użytkownika w edytorze map
 */
public class MapSaver {

    /**
     * Otwiera systemowe okno przeglądarki plików, w której użytkownik może zapisać mapę
     * @param size wielkość mapy
     * @param mapTab mapa w postaci dwuwymiarowej tablicy (współrzędne X, Y) ID pól
     */
    public static void saveMap(int size, int[][] mapTab)
    {
        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setDialogTitle("Save map");
        fileChooser.setCurrentDirectory(new File("."));

        FileNameExtensionFilter filter =new FileNameExtensionFilter("Mapa", "map");
        fileChooser.setFileFilter(filter);

        int fileState = fileChooser.showSaveDialog(null);
        if(fileState==JFileChooser.APPROVE_OPTION){
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            PrintWriter fileWriter = null;
            try {
                fileWriter=new PrintWriter(file);
                fileWriter.println(size);
                for(int i =0;i<size;i++){
                    for (int j=0;j<size;j++){
                        fileWriter.println(mapTab[i][j]);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                fileWriter.close();
            }
        }


    }

}
