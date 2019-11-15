import java.util.ArrayList;
import java.util.List;


class Map {
    CornerButton[][] corners;
    CornerButton[][] sides;
    CornerButton[][] tiles;
    Location robber;

    Map() {
        generateMap( 3);
    }

    void generateMap(int noOfPlayers) {
        corners = new CornerButton[10][10];
        for( int y  = 0; y < corners.length; y++ ) {
            for( int x = 0; x < corners[y].length; x++ ) {
                CornerButton cor = new CornerButton( new Location(x, y, Location.Types.CORNER ) );
                cor.setOnMouseClicked(e -> {
                    cor.mark();
                });
                corners[x][y] = cor;
            }
        }
        sides = new CornerButton[10][10];
        for( int y  = 0; y < sides.length; y++ ) {
            for( int x = 0; x < sides[y].length; x++ ) {
                CornerButton sid = new CornerButton( new Location(x, y, Location.Types.SIDE ) );
                sid.setOnMouseClicked(e -> {
                    sid.mark();
                });
                sides[x][y] = sid;
            }
        }
    }

    boolean buildVillage(Location cor) {
        return false;
    }

    boolean buildRoad() {
        return false;
    }

    CornerButton getCorner(Location loc) {
        return corners[loc.x][loc.y];
    }

    List<CornerButton>  getAllCorners() {
        List<CornerButton> res = new ArrayList<>();
        for( CornerButton[] c : corners ) {
            for (CornerButton cx : c) {
                if(cx != null)
                    res.add(cx);
            }
        }
        for( CornerButton[] s : sides ) {
            for (CornerButton sx : s) {
                if(sx != null)
                    res.add(sx);
            }
        }
        return res;
    }
}