package game.player;

import game.Resource;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Player {
    public enum Actions { BUILD_ROAD, BUILD_VILLAGE, BUILD_CITY, BUY_DEV_CARD };
    public String name;


    Color color;
    Civilization civ;
    Resource res;
    int armySize = 0;
    int roadLength = 0;
    int roadBuildingCards = 0;
    int yearOfPlentyCards = 0;
    int knightCards = 0;
    int victoryPoints = 0;
    //ArrayList<DevelopmentCards> playerCards;


    public Player(Color c, Civilization.CivilizationEnum civilizationEnum, String name) {
        color = c;
        res = new Resource( 4, 4, 4, 4, 4);
        civ = new Civilization(civilizationEnum);
    }

    public Color getColor() {
        return color;
    }

    public boolean getDevelopmentCard( DevelopmentCards card ){
        if ( card == DevelopmentCards.KNIGHT){
            knightCards++;
            return true;
        }
        else if ( card == DevelopmentCards.ROAD_BUILDING){
            roadBuildingCards++;
            return true;
        }
        else if ( card == DevelopmentCards.VICTORY_POINT){
            victoryPoints++;
            return true;
        }
        else if ( card == DevelopmentCards.YEAR_OF_PLENTY ){
            yearOfPlentyCards++;
            return true;
        }
        return false;
    }

    public boolean canAfford(Actions a) {
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

    public boolean playKnightCard(){
        if ( knightCards > 0 ){
            armySize++;
            return true;
        }
        return false;
    }
    public Resource makeAction(Actions a) {
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

    public Resource addResource( Resource res ) {
        this.res.add( res );
        return this.res;
    }

    public Resource multiplyResource( Resource res ){
        this.res.multiply( res );
        return this.res;
    }

    public int getWood(){
        return res.getWood();
    }

    public int getSheep(){
        return res.getSheep();
    }

    public int getWheat(){
        return res.getWheat();
    }

    public int getOre(){
        return res.getOre();
    }

    public int getBrick(){
        return res.getBrick();
    }

    public int incrementVictoryPoints( int i) {
        victoryPoints += i;
        return victoryPoints;
    }

    public boolean checkVictory () {
        return victoryPoints >= 10;
    }
}
