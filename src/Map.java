import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        corners = new MapCorner[6][12];
        for( int y  = 0; y < corners.length; y++ ) {
            for( int x = 0; x < corners[y].length; x++ ) {
                if( x >= 2*y - 5 && x <= 6 + 2*y ) {
                    MapElement cor = new MapCorner(new Location(x, y, Location.Types.CORNER));
                    /*cor.setOnMouseClicked(e -> {
                        if (noAdjacentSettlements(cor))
                            cor.construct();
                    });*/
                    corners[y][x] = cor;
                } else
                    corners[y][x] = null;
            }
        }
        sides = new MapSide[6][17];
        for( int y  = 0; y < sides.length; y++ ) {
            for( int x = 0; x < sides[y].length; x++ ) {
                if ( x >= 3*y - 7 && x <= 9 + 3*y && !( y == sides.length - 1 && x % 3 == 0)) {
                    MapElement sid = new MapSide(new Location(x, y, Location.Types.SIDE));
                    /*sid.setOnMouseClicked(e -> {
                        if ( isConnected(sid) )
                            sid.construct();
                    });*/
                    sides[y][x] = sid;
                }
            }
        }
        ArrayList<MapTile.Types> tileStack = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if( i < 3 ) {
                tileStack.add(MapTile.Types.MOUNTAIN);
                tileStack.add(MapTile.Types.HILL);
            }
            if( i < 4) {
                tileStack.add(MapTile.Types.PASTURE);
                tileStack.add(MapTile.Types.FIELD);
                tileStack.add(MapTile.Types.FOREST);
            }
        }
        tileStack.add(MapTile.Types.DESERT);
        Collections.shuffle(tileStack);
        tiles = new MapTile[5][5];
        for( int y  = 0; y < tiles.length; y++ ) {
            for( int x = 0; x < tiles[y].length; x++ ) {
                if ( x >= y - 2 && x <= y + 2 ) {
                    MapElement tile = new MapTile(new Location(x, y, Location.Types.TILE), tileStack.remove(0) );
                    tiles[y][x] = tile;
                }
            }
        }
    }

    boolean build(Location loc ) {
        MapElement me = getMapElement( loc );
        if( loc.type == Location.Types.CORNER && noAdjacentSettlements(me)
            || loc.type == Location.Types.SIDE && isConnected(me) )
        {
            ( (Buildable) me).build();
            return true;
        }
        return false;
    }

    Player.Actions getCost( Location loc ) {
        if ( loc.type == Location.Types.TILE)
            return null;
        Buildable b = (Buildable) getMapElement( loc );
        return b.getCost();
    }

    boolean noAdjacentSettlements( MapElement cor ) {
        var res = true;
        ArrayList<Location> locs = cor.getLocation().getAdjacentCorners();
        List<MapElement> cors = getMapElements( locs );
        for( MapElement c : cors ) {
            if( !c.isEmpty() )
                res = false;
        }
        return res;
    }

    boolean isConnected( MapElement el ) {
        boolean res = false;
        Location loc = el.getLocation();
        List<MapElement> els = getMapElements( loc.getAdjacentSides() );
        if ( loc.type == Location.Types.SIDE ) {
            els.addAll( getMapElements( loc.getAdjacentCorners() ) );
        }
        for ( MapElement e : els )
            if( !e.isEmpty() )
                res = true;
        return res;
    }

    MapElement getMapElement( Location loc ) {
        ArrayList<Location> locs = new ArrayList<>();
        locs.add( loc );
        List<MapElement> me = getMapElements( locs );
        if ( me.isEmpty() )
            return null;
        return me.get(0);
    }

    List<MapElement> getMapElements(ArrayList<Location> locs ) {
        ArrayList<MapElement> res = new ArrayList<>();
        MapElement[][] arr = new MapCorner[0][0];
        for( Location l : locs ) {
            if( l.x < 0 || l.y < 0)
                continue;
            if (l.type == Location.Types.CORNER )
                arr = corners;
            else if ( l.type == Location.Types.SIDE )
                arr = sides;
            if( l.y >= arr.length  || l.x >= arr[0].length || arr[l.y][l.x] == null)
                continue;
            res.add( arr[l.y][l.x] );
        }
        return res;
    }

    List<MapElement> getAllElements() {
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
        /*for( MapElement[] t : tiles ) {
            for (MapElement tx : t) {
                if(tx != null)
                    res.add(tx);
            }
        }*/
        return res;
    }
}