package display.networkDisplay.requests;

import game.Resource;

import java.io.Serializable;

public class EndTurnInfo implements Serializable {
    private PlayerInfo currentPlayerInfo;
    private PlayerInfo playerInfo;
    private int die1;
    private int die2;

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public PlayerInfo getCurrentPlayerInfo() {
        return currentPlayerInfo;
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    public EndTurnInfo(PlayerInfo currentPlayerInfo, PlayerInfo playerInfo, int die1, int die2) {
        this.currentPlayerInfo = currentPlayerInfo;
        this.playerInfo = playerInfo;
        this.die1 = die1;
        this.die2 = die2;
    }
}
