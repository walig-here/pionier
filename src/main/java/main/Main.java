package main;

import rendering.*;
import simulation.*;
import simulation.terrain.*;
import java.util.ArrayList;

public class Main {

    static private NFrame current_window; // akutalne okno symulacji
    static public int map_size; // rozmiar planszy(w polach) z przedziału [???]
    static public Field[][] map; // plansza na której odbywa się symulacja
    static private ArrayList<Integer> buildingOrder; // kolejka ID maszyn, które musi zbudować pionier aby wygrać
    static private MenuGUI menu;
    static private Pioneer pioneer; // pionier
    static private Item targetItem = new ComponentItem(16, 0, 0); // przedmiot, ktorego zdobycie, konczy symulacje (przedmiot docelowy)
    static private ArrayList<Recipe> children = new ArrayList<>();
    static private ArrayList<Integer> buildingQueue = new ArrayList<>();

    public static void main(String[] args) {
        setBuildingOrder();

    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    private static void simulationLoop(int max_turns) {

        Field f = new SoilField(0,0);
        pioneer = new Pioneer(f);

        // pętla główna
        for (;max_turns > 0; max_turns--) {


            // pętla ruchu - wykonuje się dopóki pionierowi starcza punktów ruchu lub kiedy dotrze do celu
            {
                boolean starting = true;
                do{
                    pioneer.walk(map, starting);
                    if(starting) starting = false;
                }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }
    }

    // ustala kolejkę budynków, które powinien zbudować pionier
    private static void setBuildingOrder() {
//moze sie przydac
//        ArrayList<Item> neededItems = new ArrayList<>();
//        ArrayList<Integer> idChecker = new ArrayList<>();

        Recipe targetItemRecipe = ((ComponentItem) targetItem).getRecipe();

        ArrayList<Recipe> relatedRecipes = getRecipeChildren(targetItemRecipe);
        relatedRecipes.add(targetItemRecipe);
//TODO: moze sie przydac
        //        for (Recipe relatedRecipe : relatedRecipes) {
//            for (int j = 0; j < relatedRecipe.getInput().size(); j++) {
//                Item currentItem = relatedRecipe.getInput().get(j);
//                int currentItemId = currentItem.getID();
//                if (!idChecker.contains(currentItemId)) neededItems.add(currentItem);
//                idChecker.add(currentItemId);
//            }
//        }


        for (Recipe relatedRecipe : relatedRecipes) {
            buildingQueue.add(relatedRecipe.getMachine());
        }
        System.out.println(buildingQueue);
    }

    //Rekurencyjnie zaczyna od docelowego przedmiotu, "rozwija" jego recepty i tak dochodzi do podstawowych przedmiotów
    private static ArrayList<Recipe> getRecipeChildren(Recipe recipe) {
        for (int i=0; i < recipe.getInput().size(); i++) {
            Item childItem = recipe.getInput().get(i);
            if (childItem instanceof ComponentItem) {
                Recipe childItemRecipe = ((ComponentItem) childItem).getRecipe();
                getRecipeChildren(childItemRecipe);
                children.add(childItemRecipe);
            }
        }

        return children;
    }
}