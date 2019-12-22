package display;

import game.Game;
import game.map.*;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class GameScene {
    // properties
    public static double hexagon_short_diagonal_length;
    public static double hexagon_long_diagonal_length;
    public static double hexagon_edge_length;
    protected Group root;
    protected Scene scene;
    protected Map map;
    protected double differenceX;
    protected double differenceY;
    protected Font font;
    protected ResourceBox[] resourceBoxes;
    protected Rectangle separatorRectangle;
    protected Button endTurnButton;
    protected Button buyDevCardButton;
    protected Label turnOfPlayer;
    protected Die[] dice;
    protected HBox diceBox;
    protected ListView<Button> devCardList = new ListView<Button>();
    protected ListView<String> playerList = new ListView<>();
    List<Pair> tiles = new ArrayList<>();
    List<Pair> tokens = new ArrayList<>();
    List<Pair> tokenInfos = new ArrayList<>();
    List<MapButton> mapButtons = new ArrayList<>();
    VBox playerResourcesMenu;
    Rectangle resourcesBackground;

    double stretch = 1.0;
    Stage gameView;
    // constructors
    public GameScene(Stage gameView) throws IOException {
        this.gameView = gameView;
        root = new Group();
        resourceBoxes = new ResourceBox[5];
        dice = new Die[2];
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            System.out.println("Height: " + gameView.getHeight() + " Width: " + gameView.getWidth());
            try {
                updateSize(gameView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        gameView.widthProperty().addListener(stageSizeListener);
        gameView.heightProperty().addListener(stageSizeListener);
    }

    public void updateSize(Stage stage) throws IOException {
        stretch = Math.min( gameView.getWidth() / DefaultUISpecifications.SCREEN_WIDTH
                , gameView.getHeight() / DefaultUISpecifications.SCREEN_HEIGHT );
        for( Pair t : tiles ) {
            setTileDisplay((ImageView)t.getKey(), (MapElement) t.getValue() );
        }
        for( Pair t : tokens ) {
            setTokenDisplay((MapToken) t.getKey(), (Location) t.getValue());
        }
        for( Pair i : tokenInfos ) {
            setTokenInfoDisplay((VBox) i.getKey(), (Location) i.getValue());
        }
        for( MapButton mb : mapButtons ) {
            setMapButtonDisplay(mb);
        }
        setPlayerResourcesMenuPosition();
    }

    protected void createGameAndTiles() throws FileNotFoundException {
        font = javafx.scene.text.Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 30);
        differenceX = map.getTile(0,1).getLocation().getRawDisplayPosition().getX()
                - map.getTile(0,0).getLocation().getRawDisplayPosition().getX();

        hexagon_edge_length = (differenceX / Math.sqrt(3));
        hexagon_short_diagonal_length = differenceX;
        hexagon_long_diagonal_length = hexagon_edge_length * 2;

        //set tile images
        map.getTileElements().forEach(a -> {
            try {
                ImageView tile = getTileImage(a.getType());
                setTileDisplay(tile,a);
                tiles.add(new Pair(tile, a));
                root.getChildren().add(tile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        map.getNonTileElements().forEach(a -> {
            double x = a.getLocation().getRawDisplayPosition().getX();
            double y = a.getLocation().getRawDisplayPosition().getY();
            double r = 0.0;
            if (a instanceof MapCorner)
                r = 13.0;
            if (a instanceof MapSide)
                r = 11.0;
            MapButton mb = new MapButton(x, y, r, a);
            setMapButtonDisplay(mb);
            mapButtons.add(mb);
            mb.setFill(Color.GRAY);
            mb.setOpacity(0.5);
            nonTileMouseClicked(mb, a);
            root.getChildren().add(mb);
        });

        map.getTileElements().forEach(a->{
            if(a.getNumber() > 0) {
                double radius = 35;
                double x = a.getLocation().getRawDisplayPosition().getX();
                double y = a.getLocation().getRawDisplayPosition().getY();
                x = x + differenceX / 2;
                y = y + (differenceX / (2 * Math.sqrt(3)));
                MapToken nextToken = null;
                try {
                    nextToken = new MapToken(radius, x, y, a.getNumber());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                VBox vBox = (VBox) nextToken.getVBox();
                vBox.setTranslateX(x - radius);
                vBox.setPrefWidth(radius * 2);
                vBox.setPrefHeight(radius* 2);
                vBox.setTranslateY(y - radius);
                tokens.add(new Pair(nextToken, a.getLocation()) );
                tokenInfos.add(new Pair(vBox, a.getLocation()) );
                setTokenDisplay(nextToken, a.getLocation());
                setTokenInfoDisplay(vBox, a.getLocation() );
                root.getChildren().addAll(nextToken, vBox);
            }

        });

        //set tile selection buttons for moving robber
        map.getTileElements().forEach(a -> {
            double x = a.getLocation().getRawDisplayPosition().getX();
            double y = a.getLocation().getRawDisplayPosition().getY();
            double r = 20.0;
            MapButton mb = new MapButton(x+hexagon_short_diagonal_length / 2.0,y+hexagon_edge_length/2.0,r,a);
            mapButtons.add(mb);
            setMapButtonDisplay(mb);
            tileMouseClicked(mb, a);
            root.getChildren().add(mb);
        });
    }

    // methods
    protected void displayDice(int dieNum1, int dieNum2) {
        if ( root.getChildren().contains(diceBox) )
            root.getChildren().remove(diceBox);
        dice[0] = new Die(dieNum1);
        dice[1] = new Die(dieNum2);
        diceBox = new HBox(dice[0], dice[1]);
        diceBox.setTranslateX(200);
        diceBox.setTranslateY(200);
        root.getChildren().add(diceBox);
    }

    protected void checkGameEvent(Game game) {
        if( game.eventTiggered ) {
            String title = "";
            String explanation = "";
            switch (game.getCurrentPlayer().getCivilizationType()) {
                case OTTOMANS:
                case TURKEY:
                    title = "Kurban Bayramı (Holiday Of Sacrifices)";
                    explanation = "You sacrifice all your sheep but in return you will observe bereket (double resources in" +
                            "resource production for a long time";
                    break;
                case MAYA:
                    title = "The End Is Near";
                    if( game.getDoomsdayClock() == 1) {
                        explanation = "The foreseen apocalypse nears by as the god's roll a 12" +
                                "from now on all sheep will born as tweens";
                    }
                    else if( game.getDoomsdayClock() == 2) {
                        explanation = "DoomsDay is very close now. Because of the famines no crop generation will be possible " +
                                "from now on";
                        map.noCrops = true;
                    }
                    else if( game.getDoomsdayClock() == 3) {
                        explanation = "Doomsday have arrived most of the settlements are ruined";
                    }
                    break;
                case SPAIN:
                    title = "Siesta";
                    explanation = "Knights of other colonies are impressed by your flexible working hours and want to join";
                    break;
                case ENGLAND:
                    title = "Colonialism+";
                    explanation = "A soft breeze of colonialism brings you some" +
                            " ore from your other colonies";
                    break;
                case FRANCE:
                    title = "Le Revolution";
                    explanation = "A French Revolution has occured which destroyed all your resources but " +
                            "brought you one victory point closer to victory";
                    break;
            }
            try {
                EventPopUp popUp = new EventPopUp(title, explanation);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void addPlayerResourcesMenu() throws IOException {
        double widthOfRectangle = DefaultUISpecifications.SCREEN_WIDTH / 3;
        double heightOfRectangle = DefaultUISpecifications.SCREEN_HEIGHT / 7;
        double leftUpperCornerOfTheRectangleX = DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfRectangle / 2;
        double leftUpperCornerOfTheRectangleY = DefaultUISpecifications.SCREEN_HEIGHT - heightOfRectangle;
        resourcesBackground = new Rectangle(widthOfRectangle, heightOfRectangle);
        resourcesBackground.setTranslateX(leftUpperCornerOfTheRectangleX);
        resourcesBackground.setTranslateY(leftUpperCornerOfTheRectangleY - 20);
        resourcesBackground.setFill(Color.WHITESMOKE);
        root.getChildren().add(resourcesBackground);

        createPlayerResourceBoxes();
        separatorRectangle = new Rectangle(20,0);

        endTurnButton = new Button("End Turn");
        buyDevCardButton = new Button( "Buy Development Card");
        setupButtons();

        HBox hBox = new HBox();
        for ( ResourceBox rb : resourceBoxes )
            hBox.getChildren().add(rb);
        hBox.getChildren().addAll(separatorRectangle, endTurnButton, buyDevCardButton);

        turnOfPlayer = new Label("Turn of player 1");
        turnOfPlayer.setFont(new Font("Calibri", 14));
        turnOfPlayer.setTextFill(Color.BROWN);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(turnOfPlayer, hBox);
        vBox.setTranslateX(leftUpperCornerOfTheRectangleX);
        vBox.setTranslateY(leftUpperCornerOfTheRectangleY - 20);

        vBox.setPadding(new Insets(4,4,60,4));
        playerResourcesMenu = vBox;
        root.getChildren().add(vBox);
    }

    void setPlayerResourcesMenuPosition() {
        double widthOfRectangle = DefaultUISpecifications.SCREEN_WIDTH / 3;
        double heightOfRectangle = DefaultUISpecifications.SCREEN_HEIGHT / 7;
        double leftUpperCornerOfTheRectangleX = ( DefaultUISpecifications.SCREEN_WIDTH / 2 ) * stretch - widthOfRectangle / 2;
        double leftUpperCornerOfTheRectangleY = ( gameView.getHeight() )  - heightOfRectangle;
        playerResourcesMenu.setTranslateX(leftUpperCornerOfTheRectangleX);
        playerResourcesMenu.setTranslateY(leftUpperCornerOfTheRectangleY - 20);
        resourcesBackground.setTranslateX(leftUpperCornerOfTheRectangleX);
        resourcesBackground.setTranslateY(leftUpperCornerOfTheRectangleY - 20);
    }

    protected abstract void createPlayerResourceBoxes() throws IOException;

    protected abstract void setupButtons();

    protected void addBackground() {
        Rectangle bg = new Rectangle(DefaultUISpecifications.SCREEN_WIDTH, DefaultUISpecifications.SCREEN_HEIGHT);
        bg.setFill(Color.LIGHTSKYBLUE);
        root.getChildren().add(bg);
    }

    protected void createDevCardList() {
        root.getChildren().add(devCardList);
    }
    protected void createPlayerList() { root.getChildren().add(playerList); }


    protected abstract void tileMouseClicked(MapButton mb, MapTile a);

    protected ImageView getTileImage(MapTile.Types a) throws IOException{
        InputStream is;
        is = Files.newInputStream(Paths.get("res/images/tiles/brick.png"));
        switch (a) {
            case MOUNTAIN:
                is = Files.newInputStream(Paths.get("res/images/tiles/mountain.png"));
                break;
            case DESERT:
                is = Files.newInputStream(Paths.get("res/images/tiles/desert.png"));
                break;
            case PASTURE:
                is = Files.newInputStream(Paths.get("res/images/tiles/pasture.png"));
                break;
            case FIELD:
                is = Files.newInputStream(Paths.get("res/images/tiles/field.png"));
                break;
            case FOREST:
                is = Files.newInputStream(Paths.get("res/images/tiles/forest.png"));
                break;
        }
        Image img = new Image(is);
        is.close();
        ImageView tile = new ImageView(img);
        return tile;
    }

    protected void setTileDisplay(ImageView tile, MapElement a) throws IOException {
        Location loc = a.getLocation();
        double x = loc.getRawDisplayPosition().getX();
        double y = loc.getRawDisplayPosition().getY();
        tile.setPreserveRatio(true);
        tile.setFitWidth(differenceX * stretch);
        tile.setX(x * stretch );
        tile.setY( ( y - hexagon_edge_length / 2 ) * ( stretch ) );
        tile.setOpacity(1);
    }

    protected void setTokenDisplay(MapToken mt, Location loc) {
        double radius = 35 * stretch;
        double x = loc.getRawDisplayPosition().getX() * stretch;
        double y = loc.getRawDisplayPosition().getY() * stretch;
        x = x + ( differenceX / 2 * stretch );
        y = y + (differenceX / (2 * Math.sqrt(3))) * stretch;
        MapToken nextToken = null;
        //mt = new MapToken(radius, x, y, a.getNumber() );
        mt.setCenterX(x);
        mt.setCenterY(y);
        mt.setRadius(radius);
    }

    protected void setTokenInfoDisplay(VBox vBox, Location loc) {
        double radius = 35 * stretch;
        double x = loc.getRawDisplayPosition().getX() * stretch;
        double y = loc.getRawDisplayPosition().getY() * stretch;
        x = x + differenceX / 2 * stretch;
        y = y + (differenceX / (2 * Math.sqrt(3))) * stretch;
        vBox.setTranslateX(x - radius);
        vBox.setPrefWidth(radius * 2);
        vBox.setPrefHeight(radius* 2);
        vBox.setTranslateY(y - radius);
    }

    protected void setMapButtonDisplay( MapButton mb ) {
        MapElement a = mb.getMapElement();
        double x = a.getLocation().getRawDisplayPosition().getX();
        double y = a.getLocation().getRawDisplayPosition().getY();
        double r = 0.0;
        if (a instanceof MapCorner)
            r = 13.0;
        if (a instanceof MapSide)
            r = 11.0;
        if (a instanceof MapTile ) {
            r = 20.0;
            x += hexagon_short_diagonal_length / 2.0;
            y += hexagon_edge_length/2.0;
        }
        x *= stretch;
        y *= stretch;
        r *= stretch;
        mb.setCenterX(x);
        mb.setCenterY(y);
        mb.setRadius(r);
        mb.setFill(Color.GRAY);
        mb.setOpacity(0.5);
    }

    protected abstract void nonTileMouseClicked(MapButton mb, MapElement a);

    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }
}
