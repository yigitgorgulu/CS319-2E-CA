package game.map;

import display.DefaultUISpecifications;

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
    public enum Types { CORNER, SIDE, TILE }
    Types type;

    Location() {
        this(0,0,null);
    }

    @Override
    public boolean equals(Object obj) {
        Location location = (Location)obj;
        return (y == location.y && x == location.x);
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
        return getAdjacentTiles( null);
    }

    ArrayList<Location> getAdjacentTiles ( Location exclude ) {
        //only for corners
        ArrayList<Location> res = new ArrayList<Location>();
        Location tile1;
        tile1 = new Location( x / 2, y, Types.TILE );
        res.add(tile1);
        if ( this.x % 2 == 0 ) {
            res.add( tile1.translated( -1, 0));
            res.add( tile1.translated( -1, -1));
        } else {
            res.add( tile1.translated( 0, -1));
            res.add( tile1.translated( -1, -1));
        }
        return res;
    }

    ArrayList<Location> getAdjacentSides() { return getAdjacentSides(new ArrayList<>()); };

    ArrayList<Location> getAdjacentSides( List<Location> exclude ) {
        ArrayList<Location> res = new ArrayList<>();
        exclude.add(this);
        if (this.type == Types.CORNER) {
            Location side1 = new Location( this.x / 2 * 3 + 1, this.y, Types.SIDE );
            if( this.x % 2 == 0 ) {
                Location temp = side1.translated(-1, 0);
                if( !exclude.contains(temp)) {
                    res.add(temp);
                }
                temp = side1.translated( -2, 0 );
                if( !exclude.contains(temp)) {
                    res.add(temp);
                }
            } else {
                Location temp = side1.translated( 1, 0 );
                if( !exclude.contains(temp)) {
                    res.add(temp);
                }
                temp = side1.translated( -1, -1 );
                if( !exclude.contains(temp)) {
                    res.add(temp);
                }
            }
            if(!exclude.contains(side1)) {
                res.add(side1);
            }
        } else if (this.type == Types.SIDE) {
            ArrayList<Location> corners = this.getAdjacentCorners();
            for (Location l : corners) {
                List<Location> sides = l.getAdjacentSides(exclude);
                res.addAll( sides );
            }
        }
        
        return res;
    }

    ArrayList<Location> getAdjacentCorners() {
        return getAdjacentCorners( null );
    }

    ArrayList<Location> getAdjacentCorners( List<Location> exclude ) {
        ArrayList<Location> res = new ArrayList<>();
        if (this.type == Types.CORNER) {
            res.add( translated( -1, 0) );
            res.add( translated( 1, 0 ) );
            if( x % 2 == 0 ) {
                res.add( translated( 1, 1 ));
            } else {
                res.add(translated(-1, -1));
            }
        } else if ( this.type == Types.SIDE ) {
            Location cor1 = new Location( this.x / 3 * 2, this.y, Types.CORNER );
            if ( this.x % 3 == 0 ) {
                res.add( cor1 );
                res.add( cor1.translated( 1, 1 ) );
            } else if ( this.x % 3 == 1 ) {
                res.add( cor1 );
                res.add( cor1.translated(  1, 0 ));
            } else {
                res.add( cor1.translated( 1, 0));
                res.add( cor1.translated( 2, 0));
            }
        } else if ( this.type == Types.TILE ) {
            Location cor1 = new Location( this.x * 2, this.y, Types.CORNER );
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
        double x = 0;
        double y = 0;
        double xOffset = DefaultUISpecifications.SCREEN_WIDTH/3;
        double yOffset = DefaultUISpecifications.SCREEN_HEIGHT/8;
        double spread;
        if ( type == Types.CORNER ) {
            spread = 50.0;
            x = (this.x - this.y) * Math.sqrt(3) * spread;
            y = ( ( this.y * 3 ) - (this.x % 2 == 0 ? 0 : 1 ) ) * spread;
            x += xOffset;
            y += yOffset;
        } else if ( this.type == Types.SIDE ) {
            ArrayList<Location> cors = this.getAdjacentCorners();
            Location cor1 = cors.get(0);
            Location cor2 = cors.get(1);
            Point2D pos1 = cors.get(0).getRawDisplayPosition();
            Point2D pos2 = cors.get(1).getRawDisplayPosition();
            x = ( pos1.getX() + pos2.getX() ) / 2.0;
            y = ( pos1.getY() + pos2.getY() ) / 2.0;
        } else if (type == Types.TILE ) {
            Point2D pos = ( new Location( this.x * 2, this.y, Types.CORNER ) ).getRawDisplayPosition();
            x = pos.getX();
            y = pos.getY();
        }
        return new Point2D.Double( x , y );
    }
}
