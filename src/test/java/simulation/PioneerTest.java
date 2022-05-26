package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.terrain.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PioneerTest {


    @Test
    void test_findBuildingPlace() {

        /*
        *  C S S S S S S S S S
        *  S S S S S S S S S S
        *  S S S S S S S S S S
        *  S S S D S S G S S S
        *  S S S S S S S S S S
        *  S S S S S S S S S S
        *  S S S S S S S S S S
        *  S S S S S S S S S S
        *  S S S S S S D S S S
        *  S S S S S S S S S S
        * */

        // Mapa
        Field[][] map = new Field[10][10];
        for(int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                map[i][j] = new SoilField(i,j);
        map[3][3] = new DepositField(3,3,100,1);
        map[6][8] = new DepositField(6,8,100,2);

        map[6][3] = new GlitchSourceField(6,3,10,(byte)1);
        ((GlitchSourceField)map[6][3]).setProbabilities(map);

        map[0][0] = new CentralField(0,0);

        // Pionier
        Pioneer pioneer = new Pioneer(map[0][0]);
        ArrayList<Integer> to_build = new ArrayList<>();
        to_build.add(0);
        to_build.add(1);

        pioneer.setNextBuilding(to_build,map);

        System.out.println("DONE SKONCZONE!");
    }

    @Test
    void walk() {

        {
            // MAPA
            // S S S S
            // S S S S
            // S S S S
            // S S S S

            Field[][] map = new Field[4][4];
            for(int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    map[i][j] = new SoilField(i,j);

            Integer[] start = {0,0};
            Integer[] finish = {3,3};

            Pioneer pioneer = new Pioneer(map[start[0]][start[1]]);
            pioneer.calculatePath(map[finish[0]][finish[1]]);

            boolean starting;

            System.out.println("\nPRZEMIESZCZENIE PO MAPIE Z POLAMI ZIEMI");
            System.out.println("Pozycja startu pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
            for (int i = 1; pioneer.getPath().size() > 0; i++) {
                starting = true;
                do
                {
                    pioneer.walk(map,starting);
                    starting = false;
                    System.out.print("Tura: " + i + "\tAkutalna pozycja pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
                    System.out.println("\t Punkty ruchu: " + pioneer.getMove_points());
                } while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }

        {
            // MAPA
            // S S S S
            // S W W S
            // S W S S
            // S W S S
            Field[][] map = new Field[4][4];
            map[0][0] = new SoilField(0,0); map[1][0] = new SoilField(1,0); map[2][0] = new SoilField(2,0); map[3][0] = new SoilField(3,0);
            map[0][1] = new SoilField(0,1); map[1][1] = new WaterField(1,1); map[2][1] = new WaterField(2,1); map[3][1] = new SoilField(3,1);
            map[0][2] = new SoilField(0,2); map[1][2] = new WaterField(1,2); map[2][2] = new SoilField(2,2); map[3][2] = new SoilField(3,2);
            map[0][3] = new SoilField(0,3); map[1][3] = new WaterField(1,3); map[2][3] = new SoilField(2,3); map[3][3] = new SoilField(3,3);

            Integer[] start = {0,0};
            Integer[] finish = {3,3};

            Pioneer pioneer = new Pioneer(map[start[0]][start[1]]);
            pioneer.calculatePath(map[finish[0]][finish[1]]);

            boolean starting;

            System.out.println("\nPRZEMIESZCZENIE PO MAPIE Z DODANYMI POLAMI WODNYMI");
            System.out.println("Pozycja startu pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
            for (int i = 1; pioneer.getPath().size() > 0; i++) {
                starting = true;
                do
                {
                    pioneer.walk(map,starting);
                    starting = false;
                    System.out.print("Tura: " + i + "\tAkutalna pozycja pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
                    System.out.println("\t Punkty ruchu: " + pioneer.getMove_points());
                } while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }

        {
            // MAPA
            // S S S S
            // S C W S
            // S W S S
            // S W S S
            Field[][] map = new Field[4][4];
            map[0][0] = new SoilField(0,0); map[1][0] = new SoilField(1,0); map[2][0] = new SoilField(2,0); map[3][0] = new SoilField(3,0);
            map[0][1] = new SoilField(0,1); map[1][1] = new SoilField(1,1); map[2][1] = new WaterField(2,1); map[3][1] = new SoilField(3,1);
            map[0][2] = new SoilField(0,2); map[1][2] = new WaterField(1,2); map[2][2] = new SoilField(2,2); map[3][2] = new SoilField(3,2);
            map[0][3] = new SoilField(0,3); map[1][3] = new WaterField(1,3); map[2][3] = new SoilField(2,3); map[3][3] = new SoilField(3,3);

            Integer[] start = {0,0};
            Integer[] finish = {3,3};

            Pioneer pioneer = new Pioneer(map[start[0]][start[1]]);
            pioneer.calculatePath(map[finish[0]][finish[1]]);
            map[1][1] = new CentralField(map[1][1]);

            boolean starting;

            System.out.println("\nPRZEMIESZCZENIE PO MAPIE Z DODANYMI POLAMI WODNYMI ORAZ POLEM CENTRALNYM");
            System.out.println("Pozycja startu pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
            for (int i = 1; pioneer.getPath().size() > 0; i++) {
                starting = true;
                do
                {
                    pioneer.walk(map,starting);
                    starting = false;
                    System.out.print("Tura: " + i + "\tAkutalna pozycja pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
                    System.out.println("\t Punkty ruchu: " + pioneer.getMove_points());
                } while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }

        {
            // MAPA
            // S S S S S
            // S W W S S
            // S W S S S
            // S W S D S
            // S S S S S
            Field[][] map = new Field[5][5];
            map[0][0] = new SoilField(0,0); map[1][0] = new SoilField(1,0); map[2][0] = new SoilField(2,0); map[3][0] = new SoilField(3,0); map[4][0] = new SoilField(4,0);
            map[0][1] = new SoilField(0,1); map[1][1] = new WaterField(1,1); map[2][1] = new WaterField(2,1); map[3][1] = new SoilField(3,1); map[4][1] = new SoilField(4,1);
            map[0][2] = new SoilField(0,2); map[1][2] = new WaterField(1,3); map[2][2] = new SoilField(2,2); map[3][2] = new SoilField(3,2); map[4][2] = new SoilField(4,2);
            map[0][3] = new SoilField(0,3); map[1][3] = new WaterField(1,4); map[2][3] = new SoilField(2,3); map[3][3] = new DepositField(2,3, 0, 1); map[4][3] = new SoilField(4,3);
            map[0][4] = new SoilField(0,4); map[1][4] = new SoilField(1,5); map[2][4] = new SoilField(2,4); map[3][4] = new SoilField(3,4); map[4][4] = new SoilField(4,4);

            Integer[] start = {0,0};
            Integer[] finish = {4,4};

            Pioneer pioneer = new Pioneer(map[start[0]][start[1]]);
            pioneer.calculatePath(map[finish[0]][finish[1]]);

            boolean starting;

            System.out.println("\nPRZEMIESZCZENIE PO MAPIE Z DODANYMI POLAMI WODNYMI, POLEM CENTRALNYM I POLAMI ZLOZ");
            System.out.println("Pozycja startu pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
            for (int i = 1; pioneer.getPath().size() > 0; i++) {
                starting = true;
                do
                {
                    pioneer.walk(map,starting);
                    starting = false;
                    System.out.print("Tura: " + i + "\tAkutalna pozycja pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
                    System.out.println("\t Punkty ruchu: " + pioneer.getMove_points());
                } while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }

        {
            // MAPA
            // S S S S S
            // S S W S S
            // S W S S S
            // S W S G S
            // S S S S S
            Field[][] map = new Field[5][5];
            map[0][0] = new SoilField(0,0); map[1][0] = new SoilField(1,0); map[2][0] = new SoilField(2,0); map[3][0] = new SoilField(3,0); map[4][0] = new SoilField(4,0);
            map[0][1] = new SoilField(0,1); map[1][1] = new SoilField(1,1); map[2][1] = new WaterField(2,1); map[3][1] = new SoilField(3,1); map[4][1] = new SoilField(4,1);
            map[0][2] = new SoilField(0,2); map[1][2] = new WaterField(1,3); map[2][2] = new SoilField(2,2); map[3][2] = new SoilField(3,2); map[4][2] = new SoilField(4,2);
            map[0][3] = new SoilField(0,3); map[1][3] = new WaterField(1,4); map[2][3] = new SoilField(2,3); map[3][3] = new GlitchSourceField(2,3, 1, (byte)0); map[4][3] = new SoilField(4,3);
            map[0][4] = new SoilField(0,4); map[1][4] = new SoilField(1,5); map[2][4] = new SoilField(2,4); map[3][4] = new SoilField(3,4); map[4][4] = new SoilField(4,4);

            Integer[] start = {0,0};
            Integer[] finish = {4,4};

            Pioneer pioneer = new Pioneer(map[start[0]][start[1]]);
            pioneer.calculatePath(map[finish[0]][finish[1]]);

            boolean starting;

            System.out.println("\nPRZEMIESZCZENIE PO MAPIE Z DODANYMI POLAMI WODNYMI, POLEM CENTRALNYM, POLAMI ZLOZ I ZRODLAMI ZAKLOCEN");
            System.out.println("Pozycja startu pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
            for (int i = 1; pioneer.getPath().size() > 0; i++) {
                starting = true;
                do
                {
                    pioneer.walk(map,starting);
                    starting = false;
                    System.out.print("Tura: " + i + "\tAkutalna pozycja pioniera: " + pioneer.getCoordinates()[0] + ", " + pioneer.getCoordinates()[1]);
                    System.out.println("\t Punkty ruchu: " + pioneer.getMove_points());
                } while (pioneer.getMove_points() != 0 && pioneer.getPath().size() > 0);
            }
        }
    }

    @Test
    void buildMachine() {
    }

    @Test
    void calculatePath() {

        /* Wyznaczenie ścieżki między tymi samymi punktami - ponier nie powinien się ruszyć */
        {
            Field spawn = new SoilField(0,0);
            Pioneer pioneer = new Pioneer(spawn);
            pioneer.calculatePath(spawn);
            Assertions.assertFalse(pioneer.getPath().size() != 0, "Blad wyznaczenia sciezki miedzy tymi samymi punktami!");
        }

        /* Wyznaczenie ścieżki na pionowej linii(x == 1) - pionier idzie naprzód */
        {
            Field spawn = new SoilField(1,1);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(1,10);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);
                Assertions.assertFalse(pos[0] != pioneer.getCoordinates()[0], "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej pionowej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(pos[1] != pioneer.getCoordinates()[1] + i + 1, "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej pionowej(cofanie)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na pionowej linii - pionier cofa się */
        {
            Field spawn = new SoilField(1,10);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(1,1);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);
                Assertions.assertFalse(pos[0] != pioneer.getCoordinates()[0], "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej pionowej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(pos[1] != pioneer.getCoordinates()[1] - i - 1, "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej pionowej(cofanie)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na poziomej linii - pionier idzie naprzód */
        {
            Field spawn = new SoilField(2,5);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(6,5);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals(pioneer.getCoordinates()[0] + i + 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej poziomej(naprzod)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals(pioneer.getCoordinates()[1]), "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej poziomej(naprzod)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na poziomej linii - pionier cofa się */
        {
            Field spawn = new SoilField(6,5);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(1,5);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals(pioneer.getCoordinates()[0] - i - 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej poziomej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals(pioneer.getCoordinates()[1]), "Blad wyznaczenia sciezki miedzy punktami polozonymi na prostej poziomej(cofanie)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na stromej funkcji rosnącej - pionier idzie naprzód */
        {
            Field spawn = new SoilField(0,0);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(2,5);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals((int)Math.floor((pioneer.getCoordinates()[1] + i + 1)/2.5)), "Blad wyznaczenia sciezki miedzy punktami polozonymi na stromej funkcji liniowej(naprzod)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals(pioneer.getCoordinates()[1] + i + 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi stromej na funkcji(naprzod)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na stromej funkcji rosnącej - pionier cofa się */
        {
            Field spawn = new SoilField(2,5);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(0,0);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals((int)Math.floor((pioneer.getCoordinates()[1] - i - 1)/2.5)), "Blad wyznaczenia sciezki miedzy punktami polozonymi na stromej funkcji rosnacej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals(pioneer.getCoordinates()[1] - i - 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi stromej na funkcji rosnacej(cofanie)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na stromej funkcji malejącej - naprzód */
        {
            Field spawn = new SoilField(0,5);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(2,0);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals((int)Math.floor((pioneer.getCoordinates()[1] - i -6)/(-2.5))), "Blad wyznaczenia sciezki miedzy punktami polozonymi na stromej funkcji malejacej(naprzod)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals(pioneer.getCoordinates()[1] - i - 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi stromej na funkcji malejacej(naprzod)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na stromej funkcji malejącej - cofanie */
        {
            Field spawn = new SoilField(2,0);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(0,5);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals((int)Math.floor((pioneer.getCoordinates()[1] + i -4)/(-2.5))), "Blad wyznaczenia sciezki miedzy punktami polozonymi na stromej funkcji malejacej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals(pioneer.getCoordinates()[1] + i + 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi stromej na funkcji malejacej(cofanie)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na łagodnej funkcji rosnącej - naprzód */
        {
            Field spawn = new SoilField(0,0);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(5,1);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals(pioneer.getCoordinates()[0] + i +1), "Blad wyznaczenia sciezki miedzy punktami polozonymi na lagodnej funkcji rosnacej(naprzod)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals((int)Math.floor((pioneer.getCoordinates()[0] + i +1)*0.2)), "Blad wyznaczenia sciezki miedzy punktami polozonymi lagodnej na funkcji rosnacej(naprzod)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na łagodnej funkcji rosnącej - cofanie */
        {
            Field spawn = new SoilField(5,1);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(0,0);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals(pioneer.getCoordinates()[0] - i -1), "Blad wyznaczenia sciezki miedzy punktami polozonymi na lagodnej funkcji rosnacej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals((int)Math.floor((pioneer.getCoordinates()[0] - i -1)*0.2)), "Blad wyznaczenia sciezki miedzy punktami polozonymi lagodnej na funkcji rosnacej(cofanie)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na łagodnej funkcji malejacej - naprzod */
        {
            Field spawn = new SoilField(0,1);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(8,0);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals(pioneer.getCoordinates()[0] + i + 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi na lagodnej funkcji malejacej(naprzod)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals((int)Math.floor((pioneer.getCoordinates()[0] + i +1)*(-0.125))+1), "Blad wyznaczenia sciezki miedzy punktami polozonymi lagodnej na funkcji malejacej(naprzod)! Niezgodny koordynat y!");
            }
        }

        /* Wyznaczenie ścieżki na łagodnej funkcji malejacej - cofanie */
        {
            Field spawn = new SoilField(8,0);
            Pioneer pioneer = new Pioneer(spawn);
            Field destination = new SoilField(0,1);
            pioneer.calculatePath(destination);

            for(int i = 0; i < pioneer.getPath().size(); i++)
            {
                Integer[] pos = pioneer.getPath().get(i);

                Assertions.assertFalse(!pos[0].equals(pioneer.getCoordinates()[0] - i - 1), "Blad wyznaczenia sciezki miedzy punktami polozonymi na lagodnej funkcji malejacej(cofanie)! Niezgodny koordynat x!");
                Assertions.assertFalse(!pos[1].equals((int)Math.floor((pioneer.getCoordinates()[0] - i -1)*(-0.125))+1), "Blad wyznaczenia sciezki miedzy punktami polozonymi lagodnej na funkcji malejacej(cofanie)! Niezgodny koordynat y!");
            }
        }
    }

    @Test
    void findBuildingPlace() {
    }

    @Test
    void setNextBuilding() {
    }

    @Test
    void setMove_points() {
    }
}