package game.map;
import game.player.Player;

public interface Buildable {
    public void build( Player player );
    public Player.Actions getCost();
    public Player getPlayer();
}
