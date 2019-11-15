import java.util.ArrayList;
import java.util.List;


class Map {
    MapElement[][] corners;
    MapElement[][] sides;
    MapElement[][] tiles;
    Location robber;



    Map() {
        generateMap( 3);
    }

    private void generateMap(int noOfPlayers) {
        int mapHeight = 10;
        int[] leftCornerLimits = new int[6];
        int[] rightCornerLimits = new int[6];
        corners = new MapElement[6][12];
        for( int y  = 0; y < corners.length; y++ ) {
            for( int x = 0; x < corners[y].length; x++ ) {
                if( x >= 2*y - 5 && x <= 6 + 2*y ) {
                    MapElement cor = new MapElement(new Location(x, y, Location.Types.CORNER));
                    cor.setOnMouseClicked(e -> {
                        if (noAdjacentSettlements(cor))
                            cor.construct();
                    });
                    corners[y][x] = cor;
                } else
                    corners[y][x] = null;
            }
        }
        sides = new MapElement[6][17];
        for( int y  = 0; y < sides.length; y++ ) {
            for( int x = 0; x < sides[y].length; x++ ) {
                if ( x >= 3*y - 7 && x <= 9 + 3*y && !( y == sides.length - 1 && x % 3 == 0)) {
                    MapElement sid = new MapElement(new Location(x, y, Location.Types.SIDE));
                    sid.setOnMouseClicked(e -> {
                        if ( isConnected(sid) )
                            sid.construct();
                    });
                    sides[y][x] = sid;
                }
            }
        }
    }

    boolean buildVillage(Location cor) {
        return false;
    }

    boolean buildRoad() {
        return false;
    }

    MapElement getCorner(Location loc) {
        return corners[loc.x][loc.y];
    }

    boolean noAdjacentSettlements( MapElement cor ) {
        var res = true;
        ArrayList<Location> locs = cor.loc.getAdjacentCorners();
        List<MapElement> cors = getLocations( locs );
        for( MapElement c : cors ) {
            if( c.type != MapElement.Types.EMPTY )
                res = false;
        }
        return res;
    }

    boolean isConnected( MapElement el ) {
        boolean res = false;
        Location loc = el.loc;
        List<MapElement> els = getLocations( loc.getAdjacentSides() );
        if ( loc.type == Location.Types.SIDE ) {
            els.addAll( getLocations( loc.getAdjacentCorners() ) );
        }
        for ( MapElement e : els )
            if( e.type != MapElement.Types.EMPTY )
                res = true;
        return res;
    }

    List<MapElement> getLocations(ArrayList<Location> locs ) {
        ArrayList<MapElement> res = new ArrayList<>();
        MapElement[][] arr = new MapElement[1][1];
        for( Location l : locs ) {
            if( l.x < 0 || l.y < 0)
                continue;
            if (l.type == Location.Types.CORNER )
                arr = corners;
            else if ( l.type == Location.Types.SIDE )
                arr = sides;
            if( l.y > arr.length  || l.x > arr[0].length || arr[l.y][l.x] == null)
                continue;
            res.add( arr[l.y][l.x] );
        }
        return res;
    }

    List<MapElement>  getAllCorners() {
        List<MapElement> res = new ArrayList<>();
        for( MapElement[] c : corners ) {
            for (MapElement cx : c) {
                if(cx != null)
                    res.add(cx);
            }
        }
        for( MapElement[] s : sides ) {
            for (MapElement sx : s) {
                if(sx != null)
                    res.add(sx);
            }
        }
        return res;
    }
}