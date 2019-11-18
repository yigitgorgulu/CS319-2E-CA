package game;

import game.map.Location;
import game.map.Map;
import game.player.DevelopmentCards;
import game.player.Player;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
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
    Location robber; // default ayarlanmali

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

    public void moveRobber(Location loc){
        if ( currentPlayer.playKnightCard() ){
            robber.setX(loc.getX());
            robber.setY(loc.getY());
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
        if (inReverseSettilingPhase()) {
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
        return gameTurns <= players.size();
    }

    public boolean inReverseSettilingPhase() {
        return gameTurns > players.size() && gameTurns <= players.size() * 2;
    }

    public boolean endOfSettlingPhase() {
        return gameTurns == players.size() * 2;
    }

}
