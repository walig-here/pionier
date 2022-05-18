package simulation.terrain;

import rendering.GridSprite;
import simulation.Machine;
import simulation.Pioneer;

import java.util.ArrayList;


public abstract class Field {


    protected int[] coordinates; //koordynaty pola terenu
    private int TerrainId;  //id terenu
    private Machine machine; //maszyna stojąca na polu
    private GridSprite gridSprite; //render pola
    private int baseMovPoints; //punkty na początku rundy jeśli jest to pole startu
    private ArrayList<Integer> ProbabilityOfGlitch; //szanse na zakłócenie
    private boolean canBuild;   //czy można na tym polu budować

    // konstruktor
    public Field(int x, int y){

        // miejsce pola na planszy
        coordinates = new int[2];
        coordinates[0] = x;
        coordinates[1] = y;
    }

    public int[] getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(int x, int y) {
       coordinates[0] = x;
       coordinates[1] = y;
    }

    // zwaraca koszt przejścia przez pole w punktach ruchu
    abstract public int goThrough(Pioneer pioneer);

    public int getTerrainId() {
        return TerrainId;
    }

    public void setTerrainId(int terrainId) {
        TerrainId = terrainId;
    }

    public int getBaseMovPoints() {
        return baseMovPoints;
    }

    public void setBaseMovPoints(int baseMovPoints) {
        this.baseMovPoints = baseMovPoints;
    }

    public boolean isCanBuild() {
        return canBuild;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    // odpowiada z wystąpienie glitcha
    public void activateGlitch() {

    }
}
