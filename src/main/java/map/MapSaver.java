package map;

import rendering.MapEditorGUI;
import simulation.terrain.Field;

import javax.swing.*;

public class MapSaver {

    public static void saveMap(int size)
    {
        int[][] mapTab =new int[size][size];
        mapTab= MapEditorGUI.getMapTab();

        JFileChooser fileChooser=new JFileChooser();
        //fileChooser.showOpenDialog()

    }
    public static void saveSimulation(Field[][] map)
    {
        //wywołuje wszystkie gettery i zapisuje razem z mapą do pliku
    }

}
