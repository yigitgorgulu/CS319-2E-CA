package Game.Map;

import Display.DefaultUISpecifications;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Location {
    int x;
    int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public enum Types { CORNER, SIDE, TILE };
    Types type;

    Location() {
        this(0,0,null);
    }

    public Location(int x, int y, Types type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public String toString() {
        return "x: " + x + ", y: " + y + "type: " +  type.name();
    }

    Location translated(int x, int y) {
        return translated( x, y , null);
    }

    Location translated( int x, int y, Types type) {
        type = ( type == null ) ? this.type : type;
        return new Location( this.x + x, this.y + y, type);
    }

    ArrayList<Location> getAdjacentTiles() {
        return getAdjacentTiles( this, null);
    }

    ArrayList<Location> getAdjacentTiles( Location loc ) {
        return getAdjacentTiles( loc, null);
    }

    ArrayList<Location> getAdjacentTiles ( Location loc, Location exclude ) {
        //only for corners
        ArrayList<Location> res = new ArrayList<Location>();
        Location tile1;
        tile1 = new Location( x / 2, y, Types.TILE );
        res.add(tile1);
        if ( loc.x % 2 == 0 ) {
            res.add( tile1.translated( -1, 0));
            res.add( tile1.translated( -1, -1));
        } else {
            res.add( tile1.translated( 0, -1));
            res.add( tile1.translated( -1, -1));
        }
        return res;
    }

    ArrayList<Location> getAdjacentSides() { return getAdjacentSides( this ); };

    ArrayList<Location> getAdjacentSides( Location loc ) { return getAdjacentSides( loc, null ); };

    ArrayList<Location> getAdjacentSides( Location loc, Location exclude ) {
        ArrayList<Location> res = new ArrayList<>();
        if (loc.type == Types.CORNER) {
            Location side1 = new Location( loc.x / 2 * 3 + 1, loc.y, Types.SIDE );
            if( loc.x % 2 == 0 ) {
                res.add( side1.translated(-1, 0));
                res.add( side1.translated( -2, 0 ));
            } else {
                res.add( side1.translated( 1, 0 ));
                res.add( side1.translated( -1, -1 ));
            }
            res.add( side1 );
        } else if (loc.type == Types.SIDE) {
            ArrayList<Location> corners = getAdjacentCorners(loc);
            for (Location l : corners) {
                List<Location> sides = getAdjacentSides(l, loc);
                res.addAll( sides );
            }
        }
        return res;
    }

    ArrayList<Location> getAdjacentCorners() {
        return getAdjacentCorners( this );
    }

    ArrayList<Location> getAdjacentCorners( Location loc ) {
        return getAdjacentCorners( loc, null);
    }

    ArrayList<Location> getAdjacentCorners( Location loc, ArrayList<Location> exclude ) {
        ArrayList<Location> res = new ArrayList<>();
        if (loc.type == Types.CORNER) {
            res.add( translated( -1, 0) );
            res.add( translated( 1, 0 ) );
            if( x % 2 == 0 ) {
                res.add( translated( 1, 1 ));
            } else {
                res.add(translated(-1, -1));
            }
        } else if ( loc.type == Types.SIDE ) {
            Location cor1 = new Location( loc.x / 3 * 2, loc.y, Types.CORNER );
            if ( loc.x % 3 == 0 ) {
                res.add( cor1 );
                res.add( cor1.translated( 1, 1 ) );
            } else if ( loc.x % 3 == 1 ) {
                res.add( cor1 );
                res.add( cor1.translated(  1, 0 ));
            } else {
                res.add( cor1.translated( 1, 0));
                res.add( cor1.translated( 2, 0));
            }
        } else if ( loc.type == Types.TILE ) {
            Location cor1 = new Location( loc.x * 2, loc.y, Types.CORNER );
            res.add( cor1 );
            res.add( cor1.translated(1,0) );
            res.add( cor1.translated(2,0) );
            res.add( cor1.translated(1,1) );
            res.add( cor1.translated(2,1) );
            res.add( cor1.translated(3,1) );
        }
        return res;
    }

    public Point2D getRawDisplayPosition() {
        return getRawDisplayPosition(this );
    }

    Point2D getRawDisplayPosition( Location loc ) {
        double x = 0;
        double y = 0;
        double xOffset = DefaultUISpecifications.SCREEN_WIDTH/4;
        double yOffset = DefaultUISpecifications.SCREEN_HEIGHT/4;
        double spread;
        if ( loc.type == Types.CORNER ) {
            spread = 50.0;
            x = (loc.x - loc.y) * Math.sqrt(3) * spread;
            y = ( ( loc.y * 3 ) - (loc.x % 2 == 0 ? 0 : 1 ) ) * spread;
            x += xOffset;
            y += yOffset;
        } else if ( loc.type == Types.SIDE ) {
            ArrayList<Location> cors = getAdjacentCorners( loc );
            Location cor1 = cors.get(0);
            Location cor2 = cors.get(1);
            Point2D pos1 = cors.get(0).getRawDisplayPosition();
            Point2D pos2 = cors.get(1).getRawDisplayPosition();
            x = ( pos1.getX() + pos2.getX() ) / 2.0;
            y = ( pos1.getY() + pos2.getY() ) / 2.0;
        } else if (type == Types.TILE ) {
            Point2D pos = ( new Location( loc.x * 2, loc.y, Types.CORNER ) ).getRawDisplayPosition();
            x = pos.getX();
            y = pos.getY();
        }
        return new Point2D.Double( x , y );
    }
}
