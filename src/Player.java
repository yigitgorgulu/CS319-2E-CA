import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Player {
    enum Actions { BUILD_ROAD, BUILD_VILLAGE, BUILD_CITY, BUY_DEV_CARD };
    String name;
    Color color = Color.RED;
    Civilization civ = new Civilization();
    Resource res;
    int armySize = 0;
    int roadLength = 0;
    int victortPoints = 0;

    Player() {
        res = new Resource( 4, 4, 4, 4, 4);
    }

    boolean canAfford( Actions a ) {
        switch(a) {
            case BUILD_ROAD:
                return res.biggerEquals(civ.roadCost);
            case BUILD_VILLAGE:
                return res.biggerEquals(civ.villageCost);
            case BUILD_CITY:
                return res.biggerEquals(civ.cityCost);
            case BUY_DEV_CARD:
                return res.biggerEquals(civ.devCardCost);
        }
        return false;
    }

    Resource makeAction( Actions a ) {
        switch(a) {
            case BUILD_ROAD:
                return res.substract(civ.roadCost);
            case BUILD_VILLAGE:
                return res.substract(civ.villageCost);
            case BUILD_CITY:
                return res.substract(civ.cityCost);
            case BUY_DEV_CARD:
                return res.substract(civ.devCardCost);
        }
        return res;
    }

    int getVictoryPoints() {
        return victortPoints;
    }


}
