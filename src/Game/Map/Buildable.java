package Game.Map;
import Game.Player.Player;

public interface Buildable {
    public void build();
    public Player.Actions getCost();
}
