package game.player;
import game.Resource;

public class Civilization {
    Resource roadCost;
    Resource villageCost;
    Resource cityCost;
    Resource devCardCost;

    Civilization(CivilizationEnum civilization) {
        if(civilization.equals(CivilizationEnum.OTTOMANS)) {
            roadCost = new Resource(1, 1, 0, 0, 0);
            villageCost = new Resource(1, 1, 1, 1, 0);
            cityCost = new Resource(0, 0, 0, 2, 3);
            devCardCost = new Resource(0, 1, 0, 1, 1);
        }
        else if(civilization.equals(CivilizationEnum.SPAIN)) {
            roadCost = new Resource(1, 0, 0, 0, 1);
            villageCost = new Resource(1, 1, 0, 2, 0);
            cityCost = new Resource(2, 0, 0, 0, 3);
            devCardCost = new Resource(0, 0, 1, 0, 2);
        }
    }

    public enum CivilizationEnum {SPAIN, OTTOMANS};

}
