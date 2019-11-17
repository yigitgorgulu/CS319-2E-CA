package Game.Map;
import Game.Player.Player;

public interface Buildable {
    public void build( Player player );
    public Player.Actions getCost();
}
