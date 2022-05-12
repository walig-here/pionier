package simulation.terrain;

public class DepositField extends Field {

    private String typeOfOre;
    public String getTypeOfOre() {
        return typeOfOre;
    }

    public void setTypeOfOre(String typeOfOre) {
        this.typeOfOre = typeOfOre;
    }


    private int CapacityOfDeposit;
    public void setCapacityOfDeposit(int capacityOfDeposit) {
        CapacityOfDeposit = capacityOfDeposit;
    }

    public int getCapacityOfDeposit() {
        return CapacityOfDeposit;
    }


}
