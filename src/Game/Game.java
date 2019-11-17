package Game;

import Game.Map.Location;
import Game.Map.Map;
import Game.Player.Player;

import java.util.ArrayList;

public class Game {
    ArrayList<Player> players;
    private int currentPlayerNo = 0;
    Player currentPlayer;
    Map map;
    int die1 = 0;
    int die2 = 0;

    public Game( Map m, ArrayList<Player> p ) {
        map = m;
        players = p;
        currentPlayer = players.get( currentPlayerNo );
    }

    public boolean build( Location loc ) {
        Player.Actions cost = map.getCost(loc);
        if( currentPlayer.canAfford(cost) ) {
            if( map.build(loc, currentPlayer) ) {
                currentPlayer.makeAction( cost );
                return true;
            }
        }
        return false;
    }

    public int getDiceValue() {
        return die1 + die2;
    }

    public int rollDice() {
        die1 = (int) ( Math.random() * 6 + 1 );
        die2 = (int) ( Math.random() * 6 + 1 );
        return getDiceValue();
    }

    public void endTurn() {
        currentPlayerNo = ( currentPlayerNo + 1 ) % players.size();
        currentPlayer = players.get( currentPlayerNo);
        map.generateResource( rollDice() );
    }

    boolean checkVictory() {
        return currentPlayer.getVictoryPoints() >= 10;
    }
}
