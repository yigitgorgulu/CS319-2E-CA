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

    public Game( Map m, ArrayList<Player> p ) {
        map = m;
        players = p;
        currentPlayer = players.get( currentPlayerNo);
    }

    public boolean build( Location loc ) {
        Player.Actions cost = map.getCost(loc);
        if( currentPlayer.canAfford(cost) ) {
            if( map.build(loc) ) {
                currentPlayer.makeAction( cost );
                return true;
            }
        }
        return false;
    }

    public void endTurn() {
        currentPlayerNo = ( currentPlayerNo + 1 ) % players.size();
        currentPlayer = players.get( currentPlayerNo);
    }
}
