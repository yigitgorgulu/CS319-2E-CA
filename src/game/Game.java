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
    Player longestRoadOwner;
    Player largestArmyOwner;

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

    public void moveRobber(int x, int y){
        if ( currentPlayer.playKnightCard() ){
            map.setRoberLocation(x, y);
        }

        else if ( getDiceValue() == 7 ){
            // every player who has more than 7 resource should give half
            for ( int i = 0; i < players.size(); i++ ){
                boolean remove = (players.get(i)).totalResource() > 7;
                if ( remove ){
                    players.get(i).looseResource(players.get(i).totalResource()/2);
                }
            }

            map.setRoberLocation(x, y); // move the robber to the given loc

            // steal one card from other robber-adj players

        }
    }

    public void setDevelopmentCards(){
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
        }
        shuffleDevelopmentCards(20);
    }

    public boolean getDevelopmentCards(){
        if ( currentPlayer.canAfford(Player.Actions.BUY_DEV_CARD) ){
            currentPlayer.getDevelopmentCard(developmentCards.get(0));
            developmentCards.remove(0);
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

    public int getDiceValue () {
        return die1 + die2;
    }

    public int rollDice() {
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

    public boolean getEvent(){ // this function checks the dice number
        if ( getDiceValue() == 7 ){ // this will move the robber
            //moveRobber(x,y);
            // how to get location 
        }
        else if ( getDiceValue() == 12 && (currentPlayer.getCivilizationType() == Civilization.CivilizationEnum.MAYA )) {
            currentPlayer.increaseDiceCounter();
            if ( currentPlayer.getDiceCounter() > 2 ){ // APOCALYPSE
                for ( int i = 0; i < players.size(); i++)
                    map.destroy(players.get(i));
            }
        }

        return false;
    }
}
