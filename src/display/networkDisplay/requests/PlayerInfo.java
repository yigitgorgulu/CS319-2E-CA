package display.networkDisplay.requests;

import game.player.Civilization;
import game.player.Player;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    public String name;
    public int r,g,b;
    public Civilization civilization;

    public PlayerInfo(Player player) {
        this.name = player.name;
        this.r = (int)(player.getColor().getRed()  * 255);
        this.g = (int)(player.getColor().getGreen() * 255);
        this.b = (int)(player.getColor().getBlue() * 255);
        this.civilization = player.getCivilization();
    }
}
