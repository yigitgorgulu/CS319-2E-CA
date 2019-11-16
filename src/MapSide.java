public class MapSide implements MapElement, Buildable {
    Location loc;
    boolean hasRoad;
    Player player = null;
    MapPort port = null;

    MapSide(Location l ) {
        loc = l;
    }

    public Location getLocation() {
        return loc;
    }

    public boolean isEmpty() {
        return !hasRoad;
    }

    public void build() {
        hasRoad = true;
    }

    public Player.Actions getCost() { return Player.Actions.BUILD_ROAD; };

}
