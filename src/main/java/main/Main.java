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
    static private ArrayList<Item> children = new ArrayList<>();
    static private ArrayList<Integer> buildingQueue = new ArrayList<>();

    public static void main(String[] args) {
        
        setBuildingOrder();
        
        menu=new MenuGUI();
        // główna pętkla symulacji
        simulationLoop(100);
    }

    // pętla symulacji wykonująca się określoną ilość tur lub do osiągnięcia przez pioniera określonego celu
    private static void simulationLoop(int max_turns) {

        Field f = new SoilField(0,0);
        pioneer = new Pioneer(f);

        // pętla główna
        for (int turn = 0; turn < max_turns; turn++) {

            // Pętla działań wywoływanych co turę na kafelkach planszy
            for (Field[] row : map) {
                for(Field field : row){

                    // Ustalamy czy w tej turze na polu wystąpiło zakłócenie
                    field.activateGlitch();

                    // Dla każdego pola z surowcem preprowadzamy proces wydobycia
                    if(field instanceof DepositField) ((DepositField)field).extract();
                }
            }

            // Pionier podejmuje decyzję co do kolejnej budowy
            pioneer.setNextBuilding(buildingOrder,map);

            // Pętla ruchu - wykonuje się dopóki pionierowi starcza punktów ruchu lub aż dotrze do celu
            {
                boolean starting = true; // true - jest to pierwszy ruch pioniera w tej turze
                do{
                    // Przemieszczenie na pole
                    pioneer.walk(map, starting);
                    if(starting) starting = false;

                    // Pionier próbuje na aktualnie zajmowanym polu coś zbudować
                    pioneer.buildMachine(map, buildingOrder);
                }while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }
    }

    private static void setBuildingOrder() {
        Recipe targetItemRecipe = ((ComponentItem) targetItem).getRecipe();

        ArrayList<Item> relatedItems = getItemChildren(targetItemRecipe);
        relatedItems.add(targetItem);

        for (Item relatedItem : relatedItems) {
            buildingQueue.add(relatedItem.getID());
        }
    }

    //Rekurencyjnie zaczyna od docelowego przedmiotu, "rozwija" jego recepty i tak dochodzi do podstawowych przedmiotów
    private static ArrayList<Item> getItemChildren(Recipe recipe) {
        for (int i=0; i < recipe.getInput().size(); i++) {
            Item childItem = recipe.getInput().get(i);
            if (childItem instanceof ComponentItem) {
                Recipe childItemRecipe = ((ComponentItem) childItem).getRecipe();
                getItemChildren(childItemRecipe);
                children.add(childItem);
            }
        }

        return children;
    }

    // Wykomentowane bo to zwracało ID maszyny, jaką chcemy zbudować. To było bezsensu bo nie mówiło nam to jaki przedmiot taka maszyna miałaby produkować.
    // Na górze przepisałem identyczne funkcje, ale zwracają one ID przedmiotów, do których odpowiednie maszyny dobiera potem pionier.

    // ustala kolejkę budynków, które powinien zbudować pionier
    /*private static void setBuildingOrder() {
        Recipe targetItemRecipe = ((ComponentItem) targetItem).getRecipe();

        ArrayList<Recipe> relatedRecipes = getRecipeChildren(targetItemRecipe);
        relatedRecipes.add(targetItemRecipe);

        for (Recipe relatedRecipe : relatedRecipes) {
            buildingQueue.add(relatedRecipe.getMachine());
        }
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
    }*/
}
