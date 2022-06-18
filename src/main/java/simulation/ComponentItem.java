package simulation;

/**
 * Skomplikowany przedmiot, który może zostać pozyskany tylko z użyciem receptury; Rozszerza klasę Item, dodając do niego recepturę (Recipe)
 * */
public class ComponentItem extends Item {

    /**
     * Receptura na przedmiot — lista przedmiotów potrzebnych do wyprodukowania go i ID maszyny, w której się go produkuje
     */
    private final Recipe recipe; // receptura, którą trzeba wykonać, aby otrzymać ten obiekt

    /**
     * Konstruktor klasy ComponentItem. Rozszerza konstruktor klasy Item, na podstawie ID przedmiotu, dobiera odpowiednią recepturę do jego wykonania (pobiera ją z bazy danych - database/recipes).
     *
     * @param id id przedmiotu, na podstawie którego przeszukiwana jest baza danych receptur
     * @param amount ilość przedmiotu
     * @param income przyrost przedmiotu na turę
     */
    public ComponentItem(int id, int amount, double income) {

        // Wywołujemy konstruktor rodzica
        super(id, amount,income);

        // Na podstawie ID przedmiotu wczytujemy odpowiednią recepturę.
        String recipe_name = "";
        switch (id){
            // 0 - energia
            case 0: recipe_name += "energy.txt"; break;

            // 1 - węgiel
            case 1: recipe_name += "coal.txt"; break;

            // 2 - drewno
            case 2: recipe_name += "wood.txt"; break;

            // 3 - drewno
            case 3: recipe_name += "copper.txt"; break;

            // 4 - ropa
            case 4: recipe_name += "oil.txt"; break;

            // 5 - złoto
            case 5: recipe_name += "gold.txt"; break;

            // 6 - żelazo
            case 6: recipe_name += "iron.txt"; break;

            // 7 - diament
            case 7: recipe_name += "diamond.txt"; break;

            // 8 - diament
            case 8: recipe_name += "petrol.txt"; break;

            // 9 - sztabka miedzi
            case 9: recipe_name += "copper_ingot.txt"; break;

            // 10 - sztabka złota
            case 10: recipe_name += "gold_ingot.txt"; break;

            // 11 - stal
            case 11: recipe_name += "steel.txt"; break;

            // 12 - stal
            case 12: recipe_name += "plastic.txt"; break;

            // 13 - kwas
            case 13: recipe_name += "acid.txt"; break;

            // 14 - kabel
            case 14: recipe_name += "cable.txt"; break;

            // 15 - trybik
            case 15: recipe_name += "cog.txt"; break;

            // 16 - taśma produkcyjna
            case 16: recipe_name += "production_belt.txt"; break;

            // 17 - element obudowy
            case 17: recipe_name += "casing.txt"; break;

            // 18 - bateria
            case 18: recipe_name += "battery.txt"; break;

            // 19 - silnik
            case 19: recipe_name += "engine.txt"; break;

            // 20 - piła diamentowa
            case 20: recipe_name += "diamond_saw.txt"; break;

            // 21 - układ elektroniczny
            case 21: recipe_name += "electronic_circuit.txt"; break;

            // 22 - procesor
            case 22: recipe_name += "cpu.txt"; break;

            // 23 - superkomputer
            case 23: recipe_name += "supercomputer.txt"; break;

            // 24 - sztabka żelaza
            case 24: recipe_name += "iron_ingot.txt"; break;
        }
        recipe = new Recipe(recipe_name);
    }

    public Recipe getRecipe() {
        return recipe;
    }

}