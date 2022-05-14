package simulation;

/**
 * Prymitywny rodzaj przedmiotu, który może zostac pozyskany bez użycia receptury.
* */
public class Item {


    private int ID; // ID przedmiotu
    private String name;
    private int amount; // ilość przedmiotu
    private double income; // przyrost na turę
    private int productionTime; //bazowy czas produkcji przedmiotu (w turach)

    public Item(String name, int amount, double income, int productionTime) {
        this.name = name;
        this.amount = amount;
        this.income = income;
        this.productionTime = productionTime;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(int productionTime) {
        this.productionTime = productionTime;
    }
}
