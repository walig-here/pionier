package simulation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.terrain.Field;
import simulation.terrain.SoilField;

import static org.junit.jupiter.api.Assertions.*;

class PioneerTest {

    @Test
    void walk() {
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