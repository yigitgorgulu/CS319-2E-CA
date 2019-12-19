package game;

import game.map.Location;
import game.map.Map;
import game.player.Civilization;
import game.player.DevelopmentCards;
import game.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Game implements Serializable {
    ArrayList<Player> players;
    private int currentPlayerNo = 0;
    Player currentPlayer;
    int noOfPlayers;
    public int gameTurns = 0;
    boolean builtRoad = false;
    boolean builtVillage = false;
    Map map;
    int die1 = 0;
    int die2 = 0;
    ArrayList<DevelopmentCards> developmentCards;
    Location loc = null;
    Player e = null;
    Player largestArmyOwner = null;

    public enum Events{MOVE_ROBBER, APOCALYPSE}

    public Game(Map m, ArrayList<Player> p) {
        map = m;
        players = p;
        currentPlayer = players.get(currentPlayerNo);
        noOfPlayers = p.size();
        setDevelopmentCards();
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    public void moveRobber(Location loc, boolean isKnight){
        if ( isKnight ){
            map.setRoberLocation(loc);
            if ( currentPlayer.getArmySize() >= 3 && (largestArmyOwner == null) ){
                largestArmyOwner = currentPlayer;
                currentPlayer.incrementVictoryPoints(2);
            }
            else if ( currentPlayer.getArmySize() > largestArmyOwner.getArmySize() ){
                largestArmyOwner.decreaseVictoryPoints(2);
                currentPlayer.incrementVictoryPoints(2);
                largestArmyOwner = currentPlayer;

            }
        }

        else if ( getDiceValue() == 7 ){
            // every player who has more than 7 resource should give half
            for ( int i = 0; i < players.size(); i++ ){
                boolean remove = (players.get(i)).totalResource() > 7;
                if ( remove ){
                    players.get(i).looseResource(players.get(i).totalResource()/2);
                }
            }
            map.setRoberLocation(loc);
            // steal one card from one of the robber-adj players -NOT ADDED YET
        }
    }

    public void setDevelopmentCards(){ // creates development cards array list considering the # of players
        developmentCards = new ArrayList<>();
        if ( noOfPlayers < 5 ){
            for ( int i = 0; i < 14; i++ )
                developmentCards.add(DevelopmentCards.KNIGHT);
            for ( int i = 14; i < 19; i++)
                developmentCards.add(DevelopmentCards.VICTORY_POINT);
            for ( int i = 19; i < 21; i++ )
                developmentCards.add(DevelopmentCards.ROAD_BUILDING);
            for ( int i = 21; i < 23; i++ )
                developmentCards.add(DevelopmentCards.YEAR_OF_PLENTY);
            for ( int i = 23; i < 24; i++ )
                developmentCards.add(DevelopmentCards.MONOPOLY);
        }
        else if ( noOfPlayers > 4 ) {
            for (int i = 0; i < 5; i++)
                developmentCards.add(DevelopmentCards.VICTORY_POINT);
            for (int i = 5; i < 8; i++)
                developmentCards.add(DevelopmentCards.KNIGHT);
            for (int i = 8; i < 28; i++)
                developmentCards.add(DevelopmentCards.ROAD_BUILDING);
            for (int i = 28; i < 31; i++)
                developmentCards.add(DevelopmentCards.YEAR_OF_PLENTY);
            for ( int i = 31; i < 31; i++ )
                developmentCards.add(DevelopmentCards.MONOPOLY);
        }
        shuffleDevelopmentCards(20);
    }

    public DevelopmentCards getDevelopmentCards(){ // assigns the DC on the top to the currentPlayer if it is affordable
        if ( currentPlayer.canAfford(Player.Actions.BUY_DEV_CARD) ){
            DevelopmentCards d = developmentCards.get(0);
            currentPlayer.getDevelopmentCard(d);
            developmentCards.remove(0);
            return d;
        }
        return null;
    }

    public void shuffleDevelopmentCards(int time){
        int count = 0;
        while ( count < time )
            count++;
            Collections.shuffle(developmentCards);
    }


    public boolean build(Location loc) {
        Player.Actions cost = map.getCost(loc);
        boolean settle = inSettlingPhase() &&
                ( ((cost == Player.Actions.BUILD_ROAD && !builtRoad)
                        || (cost == Player.Actions.BUILD_VILLAGE && !builtVillage)) );
        if ( ( currentPlayer.canAfford(cost) && !inSettlingPhase() ) || settle) {
            if (map.build(loc, currentPlayer)) {
                if (!settle)
                    currentPlayer.makeAction(cost);
                if( cost == Player.Actions.BUILD_VILLAGE ) {
                    builtVillage = true;
                }
                if( cost == Player.Actions.BUILD_ROAD ) {
                    builtRoad = true;
                } else {
                    if(currentPlayer.checkVictory()) {
                        System.out.println( currentPlayer.name + " Won");
                    }
                    currentPlayer.incrementVictoryPoints(1);
                }
                return true;
            }
        }
        return false;
    }

    public boolean buildWithCard(Location loc) { // NOT TESTED YET
        Player.Actions cost = map.getCost(loc);

        if ( ( !inSettlingPhase() ) ) {
            if (map.build(loc, currentPlayer)) {
                if( cost == Player.Actions.BUILD_VILLAGE ) {
                    builtVillage = true;
                }
                if( cost == Player.Actions.BUILD_ROAD ) {
                    builtRoad = true;
                } else {
                    if(currentPlayer.checkVictory()) {
                        System.out.println( currentPlayer.name + " Won");
                    }
                    currentPlayer.incrementVictoryPoints(1);
                }
                return true;
            }
        }
        return false;
    }


    public boolean playKnightCard(Location loc){
        if ( currentPlayer.playKnightCard() ){
            moveRobber(loc, true);
            return true;
        }
        return false;
    }

    public boolean playVictoryPointCard() {
        return currentPlayer.playVictoryPointCard();
    }

    public boolean playMonopolyCard( int resourceType ) {
        if ( currentPlayer.playMonopolyCard() ) {
            for ( int i = 0; i < players.size(); i++ ) {
                int add = ((players.get(i)).getRes()).monopolyDecrease(resourceType);
                (currentPlayer.getRes()).monopolyIncrease(resourceType,add);
            }
            return true;
        }
        return false;
    }

    public boolean playYearOfPlentyCard() {
        return currentPlayer.playYearOfPlentyCard();
    }

    public boolean playRoadBuilding(Location loc) {
        if ( currentPlayer.playRoadBuildingCard() ) {
            buildWithCard(loc);
            return true;
        }
        return false;
    }

    public boolean playPirateCard() {
        return currentPlayer.playKnightCard();
    }

    public int getDiceValue () {
        return die1 + die2;
    }

    public int rollDice() {
        if ( currentPlayer.getPirateCounter() == 0 ){ // pirate comes back, new resource's randomly generated and added
            Resource res = new Resource(0,0,0,0,0);
            res.generateRandom();
            currentPlayer.addResource(res);
            currentPlayer.resetPirateCounter();
        }
        for ( int i = 0; i < players.size(); i++ ) {
            if (players.get(i).getPirateCounter() > 0) {
                players.get(i).decreasePirateCounter();
            }
        }
        die1 = (int) (Math.random() * 6 + 1);
        die2 = (int) (Math.random() * 6 + 1);
        return getDiceValue();
    }

    public void endTurn () {
        int gameDir = 1;
        map.setInSettlingPhase(inSettlingPhase());
        if (inReverseSettlingPhase()) {
            gameDir = -1;
        }
        gameTurns++;
        System.out.println(gameTurns);
        currentPlayerNo = (currentPlayerNo + gameDir + players.size() ) % players.size();
        currentPlayer = players.get(currentPlayerNo);
        if ( !inSettlingPhase() )
            map.generateResource(rollDice());
        if ( endOfSettlingPhase() )
            map.generateResource(1);
        builtRoad = false;
        builtVillage = false;
    }

    public boolean inSettlingPhase () {
        return gameTurns < players.size() * 2;
    }

    public boolean inReverseSettlingPhase() {
        return gameTurns >= players.size() && gameTurns < players.size() * 2;
    }

    public boolean endOfSettlingPhase() {
        return gameTurns == players.size() * 2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentPlayerNo() {
        return currentPlayerNo;
    }

    // EVENTS -- NOT DONE YET

    public Events getEvent(){ // this function checks whether an event is occuring
        if ( getDiceValue() == 12 && (currentPlayer.getCivilizationType() == Civilization.CivilizationEnum.MAYA )) {
            currentPlayer.increaseDiceCounter();
            if ( currentPlayer.getDiceCounter() > 2 ){ // APOCALYPSE
                for ( int i = 0; i < players.size(); i++)
                    map.destroy(players.get(i));
            }
            return Events.APOCALYPSE;
        }
        return null; // no event
    }
}
