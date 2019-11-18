package game.map;
import game.Resource;

public class MapTile implements MapElement {
    public enum Types { FOREST, MOUNTAIN, HILL, FIELD, DESERT, PASTURE };
    Types type;
    Location loc;
    int number = -1;

    MapTile(Location l, Types t ) {
        this.loc = l;
        this.type = t;
    }

    Resource getResource() {
        if ( type == Types.FOREST )
            return new Resource( 0, 1, 0, 0, 0);
        if ( type == Types.MOUNTAIN )
            return new Resource( 0, 0, 0, 0, 1);
        if ( type == Types.HILL )
            return new Resource( 1, 0, 0, 0, 0);
        if ( type == Types.FIELD )
            return new Resource( 0, 0, 0, 1, 0);
        if ( type == Types.DESERT )
            return new Resource( 0, 0, 0, 0, 0);
        if ( type == Types.PASTURE )
            return new Resource( 0, 0, 1, 0, 0);
        return null;
    }

    public Location getLocation() {
        return loc;
    }

    public boolean isEmpty() {
        return false;
    }

    public Types getType(){
        return type;
    }

    public int getNumber() {
        return number;
    }
}
