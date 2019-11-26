package game.player;

import display.networkDisplay.requests.PlayerInfo;
import game.Resource;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
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

    public Player(PlayerInfo playerInfo) {
        color = Color.rgb(playerInfo.r,playerInfo.g,playerInfo.b);
        res = new Resource( 4, 4, 4, 4, 4);
        civ = playerInfo.civilization;
        this.name = playerInfo.name;
    }

    public Player(Color c, Civilization.CivilizationEnum civilizationEnum, String name) {
        color = c;
        res = new Resource( 4, 4, 4, 4, 4);
        civ = new Civilization(civilizationEnum);
        this.name = name;
    }

    public Civilization getCivilization() {
        return civ;
    }

    @Override
    public boolean equals(Object object) {
        return name.equals(((Player)object).name);
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

    public boolean resourceGiveAway(){ // randomly removes cards
        if ( res.totalCount() > 7 ) {
            for ( int i = 0; i < res.totalCount()/2; i++){

            }
            return true;
        }
        return false;
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
