package game.map;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import game.player.Civilization;
import game.player.Player;
import game.Resource;


public class Map implements Serializable {
    MapElement[][] corners;
    MapElement[][] sides;
    MapElement[][] tiles;
    Location robber;
    ArrayList<Point> directions = new ArrayList<>( Arrays.asList(
            new Point(0,1), new Point(1,1), new Point( 1, 0), new Point(0, -1),
            new Point(-1,-1), new Point(-1,0)
    ));

    boolean inSettlingPhase = true;


    @Override
    public String toString() {
        return "Map";
    }

    public Map() {
        generateMap( 3);
    }

    private void generateMap(int noOfPlayers) {
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
        for (int i = 0; i < 20; i++) {
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
        ArrayList<Integer> numberTokens = new ArrayList<>( Arrays.asList( 5, 2, 6, 3, 8, 10,
                9, 12, 11, 4, 8, 10,
                9, 4, 5, 6, 3, 11 ) );
        Location startLoc = new Location( 0, 0, Location.Types.TILE);
        Location currLoc = startLoc;
        Point dir = new Point( 0, 1 );
        MapTile currTile;
        while( !numberTokens.isEmpty() ) {
            currTile = (MapTile) getMapElement( currLoc );
            if ( currTile.type == MapTile.Types.DESERT ) {
                robber = currTile.getLocation();
                currTile.number = 0;
            }
            else
                currTile.number = numberTokens.remove(0);
                MapTile nextTile = (MapTile) getMapElement(currLoc.translated(dir.x, dir.y));
                while ( ( nextTile == null || nextTile.number != -1 ) && !numberTokens.isEmpty() ) {
                    dir = getNextDirection(dir);
                    nextTile = (MapTile) getMapElement(currLoc.translated(dir.x, dir.y));
                }
            currLoc = currLoc.translated( dir.x, dir.y );
        }
    }

    public void setInSettlingPhase(boolean b) {
        inSettlingPhase = b;
    }


    private Point getNextDirection( Point p ) {
        int i = directions.indexOf( p );
        return directions.get( ( i + 1 ) % directions.size() );
    }
    public boolean build(Location loc, Player currentPlayer) {
        MapElement me = getMapElement( loc );
        boolean canSettle = loc.type == Location.Types.CORNER && noAdjacentSettlements(me)
                && ( inSettlingPhase || isConnected(me, currentPlayer) );
        boolean canRoad = loc.type == Location.Types.SIDE && isConnected(me, currentPlayer);
        if( canSettle || canRoad )
        {
            ( (Buildable) me).build( currentPlayer );
            if(canRoad) {
                //System.out.println(roadLength(loc, new ArrayList<>(), currentPlayer));
            }
            return true;
        }
        return false;
    }

    public boolean destroy(Player player){
        for( int y  = 0; y < corners.length; y++ ) {
            for (int x = 0; x < corners[y].length; x++) {
                if (((corners[y][x]).getLocation()).type == Location.Types.CORNER) {
                    Location loc = (corners[y][x]).getLocation();
                    MapCorner mc = new MapCorner(loc);
                    if ( !mc.isEmpty() && player.getCivilizationType() != Civilization.CivilizationEnum.TURKEY ){
                        if ( mc.type == MapCorner.Types.CITY ){
                            mc.type = MapCorner.Types.EMPTY;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public void generateResource( int dice) {
        for( MapElement[] t : tiles ) {
            for( MapElement tx : t ) {
                MapTile tile = (MapTile) tx;
                if( tile != null && ( tile.number == dice || dice == 1 ) ) {
                    List<MapElement> els = getMapElement(tile.loc.getAdjacentCorners());
                    Resource res = tile.getResource();
                    for( MapElement e : els) {
                        MapCorner cor = (MapCorner) e;
                        if( cor.player != null ) {
                            if( cor.type == MapCorner.Types.VILLAGE)
                                cor.player.addResource( res );
                            if( cor.type == MapCorner.Types.CITY) {
                                cor.player.addResource( res );
                                cor.player.multiplyResource( res );
                            }
                        }
                    }
                }
            }
        }
    }

    public Player.Actions getCost(Location loc) {
        if ( loc.type == Location.Types.TILE)
            return null;
        Buildable b = (Buildable) getMapElement( loc );
        return b.getCost();
    }

    boolean noAdjacentSettlements( MapElement cor ) {
        boolean res = true;
        ArrayList<Location> locs = cor.getLocation().getAdjacentCorners();
        List<MapElement> cors = getMapElement( locs );
        for( MapElement c : cors ) {
            if( !c.isEmpty() )
                res = false;
        }
        return res;
    }

    boolean isConnected( MapElement el, Player p ) {
        boolean res = false;
        Location loc = el.getLocation();
        List<MapElement> els = getMapElement( loc.getAdjacentSides() );
        if ( loc.type == Location.Types.SIDE ) {
            els.addAll( getMapElement( loc.getAdjacentCorners() ) );
        }
        for ( MapElement e : els )
            if( ( (Buildable) e).getPlayer() == p )
                res = true;
        return res;
    }

    public MapElement getMapElement(Location loc ) {
        ArrayList<Location> locs = new ArrayList<>();
        locs.add( loc );
        List<MapElement> me = getMapElement( locs );
        if ( me.isEmpty() )
            return null;
        return me.get(0);
    }

    public int roadLength( Location location, List<Location> exclude, Player currentPlayer) {
        exclude.add(location);
        List<Location> newExclude = new ArrayList<>(exclude);
        int max = 0;
        int temp;
        for (Location loc: location.getAdjacentSides(exclude) ) {
            MapSide adjacentRoad = (MapSide) getMapElement(loc);
            if( adjacentRoad != null) {
                if (adjacentRoad.hasRoad
                        && adjacentRoad.player.equals(currentPlayer)) {
                    temp = roadLength(loc, newExclude, currentPlayer);
                    if (temp > max) {
                        max = temp;
                    }
                }
            }
        }
        return ++max;
    }


    List<MapElement> getMapElement(ArrayList<Location> locs ) {
        ArrayList<MapElement> res = new ArrayList<>();
        MapElement[][] arr = new MapCorner[0][0];
        for( Location l : locs ) {
            if( l.x < 0 || l.y < 0)
                continue;
            if (l.type == Location.Types.CORNER )
                arr = corners;
            else if ( l.type == Location.Types.SIDE )
                arr = sides;
            else if ( l.type == Location.Types.TILE )
                arr = tiles;
            if( l.y >= arr.length  || l.x >= arr[0].length || arr[l.y][l.x] == null)
                continue;
            res.add( arr[l.y][l.x] );
        }
        return res;
    }

    public List<MapElement> getAllElements() {
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
        for( MapElement[] t : tiles ) {
            for (MapElement tx : t) {
                if(tx != null)
                    res.add(tx);
            }
        }
        return res;
    }

    public MapElement getTile(int x, int y){
        return tiles[x][y];
    }

    public List<MapTile> getTileElements() {
        List<MapTile> res = new ArrayList<>();
        for( MapElement[] c : tiles ) {
            for (MapElement cx : c) {
                if(cx != null)
                    res.add((MapTile) cx);
            }
        }
        return res;
    }

    public List<MapElement> getNonTileElements() {
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

    public void setRoberLocation(Location loc ){
        robber.setY(loc.getY());
        robber.setX(loc.getX());
    }
}