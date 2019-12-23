package game.player;
import game.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Civilization implements Serializable {
    Resource roadCost;
    Resource villageCost;
    Resource cityCost;
    Resource devCardCost;
    CivType civType;
    List<CivBonus> civBonuses= new ArrayList<>();

    Civilization(CivType civ){
        roadCost = new Resource(1,1,0,0,0);
        villageCost = new Resource(1,1,1,1,0);
        cityCost = new Resource(0,0,1,2,3);
        devCardCost = new Resource(0,0,1,1,1);
        civType = civ;

        switch(civ) {
            case OTTOMANS:
                villageCost.substract(new Resource(0,0,0,1,0));
                break;
            case ENGLAND:
                devCardCost.add(new Resource(1,0,0,0,0));
                cityCost.substract(new Resource(0,0,1,0,0));
                break;
            case FRANCE:
                villageCost.substract(new Resource(0,1,0,0,0));
                break;
            case MAYA:
                roadCost.add(new Resource(0,0,1,0,0));
            case SPAIN:
                roadCost.substract(new Resource(1,0,0,0,0));
                roadCost.add(new Resource(0,1,0,0,0));
                break;
        }
    }

    public boolean hasBonus( CivBonus c ) {
        return civBonuses.contains(c);
    }

    public enum CivType {OTTOMANS, ENGLAND, FRANCE, MAYA, SPAIN, TURKEY};
    public enum CivBonus {HOLIDAY_OF_SACRIFICE, DOOMSDAY};

}
