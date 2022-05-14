package simulation.terrain;

import simulation.Pioneer;

public class DepositField extends Field {

    private int itemID; // ID wydobywanego stąd itemu
    private int CapacityOfDeposit; // maksymalna ilość surowca do wydobycia


    public void setCapacityOfDeposit(int capacityOfDeposit) {
        CapacityOfDeposit = capacityOfDeposit;
    }
    public int getCapacityOfDeposit() {
        return CapacityOfDeposit;
    }

    // metoda uszczuplająca złoże wraz z działaniem maszyny
    public void extract()
    {

    }

    @Override
    public int goThrough(Pioneer pioneer)
    {
        return 0;
    }
}
