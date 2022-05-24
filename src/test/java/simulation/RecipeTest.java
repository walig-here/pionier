package simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void constructor(){

        String filename = "";
        for(int i = 1; i < 24; i++){
            switch (i){
                // 0 - energia
                case 0: filename = "energy.txt"; break;

                // 1 - węgiel
                case 1: filename= "coal.txt"; break;

                // 2 - drewno
                case 2: filename= "wood.txt"; break;

                // 3 - drewno
                case 3: filename= "copper.txt"; break;

                // 4 - ropa
                case 4: filename= "oil.txt"; break;

                // 5 - złoto
                case 5: filename= "gold.txt"; break;

                // 6 - żelazo
                case 6: filename= "iron.txt"; break;

                // 7 - diament
                case 7: filename= "diamond.txt"; break;

                // 8 - diament
                case 8: filename= "petrol.txt"; break;

                // 9 - sztabka miedzi
                case 9: filename= "copper_ingot.txt"; break;

                // 10 - sztabka złota
                case 10: filename= "gold_ingot.txt"; break;

                // 11 - stal
                case 11: filename= "steel.txt"; break;

                // 12 - stal
                case 12: filename= "plastic.txt"; break;

                // 13 - kwas
                case 13: filename= "acid.txt"; break;

                // 14 - kabel
                case 14: filename= "cable.txt"; break;

                // 15 - trybik
                case 15: filename= "cog.txt"; break;

                // 16 - taśma produkcyjna
                case 16: filename= "production_belt.txt"; break;

                // 17 - element obudowy
                case 17: filename= "casing.txt"; break;

                // 18 - bateria
                case 18: filename= "battery.txt"; break;

                // 19 - silnik
                case 19: filename= "engine.txt"; break;

                // 20 - piła diamentowa
                case 20: filename= "diamond_saw.txt"; break;

                // 21 - układ elektroniczny
                case 21: filename= "electronic_circuit.txt"; break;

                // 22 - procesor
                case 22: filename= "cpu.txt"; break;

                // 23 - superkomputer
                case 23: filename= "supercomputer.txt"; break;
            }

            Recipe recipe = new Recipe(filename);
            System.out.println("RECEPTURA " + i);
            System.out.println("Maszyna: " + recipe.getMachine());
            System.out.print("Skladniki: ");
            for(Item component : recipe.getInput()){
                System.out.print(component.getAmount() + "x" + component.getName() + "\t");
            }
            System.out.println("\n");
        }
    }
}