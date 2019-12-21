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
    int roadsBuilt = -1;
    int villagesBuilt = -1;
    Map map;
    int die1 = 0;
    int die2 = 0;
    ArrayList<DevelopmentCards> developmentCards;
    Location loc = null;
    Player longestRoadOwner = null;
    Player largestArmyOwner = null;

    public Game(Map m, ArrayList<Player> p) {
        map = m;
        players = p;
        currentPlayer = players.get(currentPlayerNo);
        noOfPlayers = p.size();
        setDevelopmentCards();
    }

    public ArrayList<Player> getPlayers() {
        return players;
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
        else if ( noOfPlayers >= 5 ) {
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
        Collections.shuffle(developmentCards);
        shuffleDevelopmentCards(20);
    }

    public boolean buyDevelopmentCard(){ // assigns the DC on the top to the currentPlayer if it is affordable
        if ( !inSettlingPhase() && currentPlayer.canAfford(Player.Actions.BUY_DEV_CARD) ){
            currentPlayer.addDevelopmentCard(developmentCards.get(0));
            developmentCards.remove(0);
            currentPlayer.payForAction(Player.Actions.BUY_DEV_CARD);
            return true;
        }
        return false;
    }

    public void shuffleDevelopmentCards(int time){
        int count = 0;
        while ( count < time )
            count++;
            Collections.shuffle(developmentCards);
    }


    public boolean build(Location loc) {
        Player.Actions cost = map.getCost(loc);
        boolean freeSettle =
                ( ((cost == Player.Actions.BUILD_ROAD && roadsBuilt < 0)
                        || (cost == Player.Actions.BUILD_VILLAGE && villagesBuilt < 0)) );
        boolean paidSettle = currentPlayer.canAfford(cost) && !inSettlingPhase();
        if ( paidSettle || freeSettle) {
            if (map.build(loc, currentPlayer)) {
                if (paidSettle) {
                    currentPlayer.payForAction(cost);
                }
                if( cost == Player.Actions.BUILD_VILLAGE ) {
                    villagesBuilt += 1;
                    currentPlayer.incrementVictoryPoints(1);
                }
                if( cost == Player.Actions.BUILD_CITY) {
                    currentPlayer.incrementVictoryPoints(1);
                }
                if( cost == Player.Actions.BUILD_ROAD ) {
                    roadsBuilt += 1;
                }
                if(currentPlayer.checkVictory()) {
                    System.out.println(currentPlayer.name + " Won");
                }
                return true;
            }
        }
        return false;
    }

    public boolean getEvent(){ // within a game, an event can only occur once
        if ( getDiceValue() == 12 ){
            Civilization.CivilizationEnum type = currentPlayer.getCivilizationType();
            if ( currentPlayer.getDiceCounter() != -1 )
                currentPlayer.increaseDiceCounter();
            if ( currentPlayer.getDiceCounter() > 2 ){
                switch (type){
                    case OTTOMANS:
                        currentPlayer.resetSheep();
                        (currentPlayer.getCivilization()).bereketMode();
                        break;
                    case TURKEY:
                        break;
                    case MAYA:
                        for ( int i = 0; i < players.size(); i++)
                            map.destroy(players.get(i));
                        break;
                    case SPAIN:
                        if ( (largestArmyOwner != currentPlayer) && (largestArmyOwner != null) ){
                            largestArmyOwner.decreaseArmySize(1);
                            currentPlayer.increaseArmySize(1);
                            if ( currentPlayer.getArmySize() > largestArmyOwner.getArmySize() ){
                                largestArmyOwner = currentPlayer;
                            }
                        }
                        break;
                    case ENGLAND:
                        Resource res = new Resource(0,0,0,0,3);
                        currentPlayer.addResource(res);
                        break;
                    case FRANCE:
                        currentPlayer.resetResources();
                        currentPlayer.incrementVictoryPoints(1);
                        break;
                }
                currentPlayer.resetDiceCounter();
                return true;
            }
        }
        return false;
    }

    public boolean playDevelopmentCard(DevelopmentCards devCard) {
        if(currentPlayer.playDevelopmentCard(devCard) ) {
            switch(devCard) {
                case KNIGHT:
                    moveRobber(loc, true);
                    if ( largestArmyOwner == null && currentPlayer.getArmySize() >= 3 ){
                        largestArmyOwner = currentPlayer;
                        currentPlayer.incrementVictoryPoints(2);
                    }
                    else if ( currentPlayer.getArmySize() > largestArmyOwner.getArmySize() ){
                        largestArmyOwner.decreaseVictoryPoints(2);
                        currentPlayer.incrementVictoryPoints(2);
                        largestArmyOwner = currentPlayer;
                    }
                    break;
                case MONOPOLY:
                    /*int add = ((players.get(i)).getRes()).monopolyDecrease(resourceType);
                    (currentPlayer.getRes()).monopolyIncrease(resourceType,add);*/
                    break;
                case ROAD_BUILDING:
                    roadsBuilt = Math.min(roadsBuilt-2,-2);
            }
            return true;
        }
        return false;
    }

    public int getDiceValue () {
        return die1 + die2;
    }

    public int rollDice() {
        if ( currentPlayer.getPirateCounter() == 0 ){
            Resource res = new Resource(0,0,0,0,0);
            res.generateRandom();;
            (currentPlayer.getRes()).add(res);
            // pirate comes with new resources
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
        map.setInSettlingPhase(inSettlingPhase());

        //calculateNextPlayerNo uses gameTurns. Therefore, line order is important.
        currentPlayerNo = calculateNextPlayerNo();
        gameTurns++;

        currentPlayer = players.get(currentPlayerNo);
        if ( !inSettlingPhase() ) {
            map.generateResource(rollDice());
            roadsBuilt = 0;
            villagesBuilt = 0;
        }
        if( inSettlingPhase() ) {
            roadsBuilt = -1;
            villagesBuilt = -1;
            map.setInSettlingPhase(true);
        }
        if ( endOfSettlingPhase() ) {
            roadsBuilt = 0;
            villagesBuilt = 0;
            map.generateResource(1);
            map.setInSettlingPhase(false);
        }
    }

    private int calculateNextPlayerNo() {
        int gameDir = 1;

        if ( gameTurns == noOfPlayers - 1 || gameTurns == 2 * noOfPlayers - 1 ) {
            gameDir = 0;
        } else if( gameTurns > noOfPlayers - 1 && gameTurns < 2 * noOfPlayers - 1 ) {
            gameDir = -1;
        }

        return (currentPlayerNo + gameDir + players.size() ) % players.size();
    }

    public boolean inSettlingPhase () {
        return gameTurns < players.size() * 2;
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

    public Player getNextPlayer() {
        return players.get(calculateNextPlayerNo());
    }
}
