package game.player;
import game.Resource;

public class Civilization {
    Resource roadCost;
    Resource villageCost;
    Resource cityCost;
    Resource devCardCost;

    Civilization(CivilizationEnum civilizationEnum){
        roadCost = new Resource(1,1,0,0,0);
        villageCost = new Resource(1,1,1,1,0);
        cityCost = new Resource(0,0,1,2,3);
        devCardCost = new Resource(0,0,1,1,1);

        if ( civilizationEnum == CivilizationEnum.OTTOMANS ){
            villageCost.substract(new Resource(0,0,0,1,0));
        }

        else if ( civilizationEnum == CivilizationEnum.ENGLAND ){
            devCardCost.add(new Resource(1,0,0,0,0));
            cityCost.substract(new Resource(0,0,1,0,0));
        }

        else if ( civilizationEnum == CivilizationEnum.FRANCE){
            villageCost.substract(new Resource(0,1,0,0,0));
        }

        else if ( civilizationEnum == CivilizationEnum.MAYA ){
            roadCost.add(new Resource(0,0,1,0,0));
        }
    }

    public enum CivilizationEnum {OTTOMANS, ENGLAND, FRANCE, MAYA};

}
