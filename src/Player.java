import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Player {
    String name;
    Color color = Color.DARKSLATEBLUE;
    Civilization civilization;
    Resource resources;
    int armySize = 0;
    int roadLength = 0;
    boolean largestArmy = false;
    boolean longestRoad = false;
    int vpFromSettlements = 0;
    int vpFromDevCards = 0;
    ArrayList<DevelopmentCards> devCards;

    int getVictoryPoints() {
        return vpFromSettlements + vpFromDevCards +
                ( largestArmy ? 3 : 0 ) + ( longestRoad ? 3 : 0 );
    }


}
