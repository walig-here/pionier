package simulation.terrain;

import rendering.GridSprite;
import simulation.Machine;


public abstract class Field {


    private int[] coordinates =new int[2]; //koordynaty pola terenu
    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }


    private int TerrainId;  //id terenu
    public int getTerrainId() {
        return TerrainId;
    }

    public void setTerrainId(int terrainId) {
        TerrainId = terrainId;
    }


    private int baseMovPoints; //punkty na początku rundy jeśli jest to pole startu
    public int getBaseMovPoints() {
        return baseMovPoints;
    }

    public void setBaseMovPoints(int baseMovPoints) {
        this.baseMovPoints = baseMovPoints;
    }


    private int costOfMov;  //koszt wyjścia z pole
    public int getCostOfMov() {
        return costOfMov;
    }

    public void setCostOfMov(int costOfMov) {
        this.costOfMov = costOfMov;
    }


    private int ProbabilityOfGlitch; //szansa na zakłócenie
    public int getProbabilityOfGlitch() {
        return ProbabilityOfGlitch;
    }

    public void setProbabilityOfGlitch(int probabilityOfGlitch) {
        ProbabilityOfGlitch = probabilityOfGlitch;
    }


    private boolean canBuild;   //czy można na tym p[olu budować
    public boolean isCanBuild() {
        return canBuild;
    }

    public void setCanBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }


    private Machine machine; //maszyna stojąca na polu
    private GridSprite gridSprite; //render pola





}
