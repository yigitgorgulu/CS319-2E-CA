package game;

import game.map.Location;
import game.map.Map;
import game.player.Player;

import java.util.ArrayList;

public class Game {
    ArrayList<Player> players;
    private int currentPlayerNo = 0;
    Player currentPlayer;
    public int gameTurns = 0;
    boolean builtRoad = false;
    boolean builtVillage = false;
    Map map;
    int die1 = 0;
    int die2 = 0;

    public Game(Map m, ArrayList<Player> p) {
        map = m;
        players = p;
        currentPlayer = players.get(currentPlayerNo);
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
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
                    //currentPlayer.incrementVictoryPoints(1);
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

   // boolean checkVictory () {
     //   return currentPlayer.getVictoryPoints() >= 10;
    //}
}
