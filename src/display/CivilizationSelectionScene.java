package display;

import game.Sound;
import game.player.Civilization;
import game.player.Player;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CivilizationSelectionScene {

    private int currenti = 0;
    Civilization.CivType selectedCiv = null;
    String name;
    private Color selectedColor = Color.BLACK;
    private Font fnormal;
    private Font flarge;
    final int NORMAL_FONT_SIZE = (int)DefaultUISpecifications.SCREEN_WIDTH / 110;
    final int LARGE_FONT_SIZE = (int)DefaultUISpecifications.SCREEN_WIDTH / 60;
    private Stage gameview;
    private Player[] players;
    ListView<HBox> playersListView;
    private String selectedColorString;

    public CivilizationSelectionScene(Stage gameview, CountDownLatch cc, int numberOfPlayers, String gametype) throws IOException, InterruptedException {
        super();
        this.gameview = gameview;
        players = new Player[numberOfPlayers];
        double heightOfACivBack = DefaultUISpecifications.SCREEN_HEIGHT / 3;
        double widthOfACivBack = DefaultUISpecifications.SCREEN_WIDTH / 7;

        if(gametype.equals("SINGLE")) {
            playersListView = new ListView<>();
            playersListView.setPrefSize(DefaultUISpecifications.SCREEN_WIDTH / 4, NORMAL_FONT_SIZE * numberOfPlayers);
            playersListView.setStyle(".list-cell:empty-fx-opacity: 0;");
            playersListView.setEditable(false);
        }

        StackPane pane = new StackPane();
        fnormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);
        flarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);

        DropShadow drop = new DropShadow(5, Color.WHITE);
        drop.setInput(new Glow(0.6));

        Rectangle rectangle = new Rectangle(DefaultUISpecifications.SCREEN_WIDTH,DefaultUISpecifications.SCREEN_HEIGHT);
        rectangle.setOpacity(0.1);


        HBox lineOne = new HBox();
        HBox lineTwo = new HBox();
        VBox vBoxCivilizations = new VBox();
        VBox texts = new VBox();
        VBox textsAndCivilizations = new VBox();
        int i = 0;

        Text typeYourNameText = new Text("Please Type Your Name & Choose A Color");
        typeYourNameText.setFont(fnormal);
        typeYourNameText.setFill(Color.WHITE);
        TextField textFieldTypeYourName = new TextField();
        textFieldTypeYourName.setPrefSize(DefaultUISpecifications.SCREEN_WIDTH / 5, NORMAL_FONT_SIZE);
        textFieldTypeYourName.setMaxWidth(DefaultUISpecifications.SCREEN_WIDTH / 5);
        textFieldTypeYourName.setStyle("-fx-focus-color: transparent;");
        textFieldTypeYourName.setAlignment(Pos.CENTER);

        Text currentPlayersText = new Text("");

        texts.getChildren().addAll(typeYourNameText,textFieldTypeYourName);
        texts.setAlignment(Pos.CENTER);
        texts.setSpacing(DefaultUISpecifications.SCREEN_HEIGHT / 140);

        HBox textsAndColors = new HBox();
        final ComboBox<String>[] colorNames = new ComboBox[]{new ComboBox<>()};
        colorNames[0].setStyle("-fx-focus-color: transparent; -fx-border-color: transparent; -fx-alignment: center");
        List<Color> colorlist = Arrays.asList(Color.BLACK,Color.BLUE,Color.ORANGE,Color.YELLOW,Color.RED,Color.GRAY,Color.GREEN);
        ArrayList<Color> colors = new ArrayList<>();
        colors.addAll(colorlist);

        colorNames[0].getItems().addAll("Black", "Blue", "Orange", "Yellow", "Red", "Purple", "Green");
        textsAndColors.getChildren().addAll(texts, colorNames[0]);

        colorNames[0].getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldvalue, final String newvalue)
            {
                switch (newvalue){
                    case "Black":
                       selectedColor = Color.BLACK;
                       selectedColorString = "Black";
                       break;
                    case "Blue":
                        selectedColor = Color.BLUE;
                        selectedColorString = "Blue";
                        break;
                    case "Orange":
                        selectedColor = Color.ORANGE;
                        selectedColorString = "Orange";
                        break;
                    case "Yellow":
                        selectedColor = Color.YELLOW;
                        selectedColorString = "Yellow";
                        break;
                    case "Red":
                        selectedColor = Color.RED;
                        selectedColorString = "Red";
                        break;
                    case "Purple":
                        selectedColor = Color.PURPLE;
                        selectedColorString = "Purple";
                        break;
                    case "Green":
                        selectedColor = Color.GREEN;
                        selectedColorString = "Green";
                        break;
                    default:
                        selectedColor = Color.TRANSPARENT;
                        selectedColorString = "";
                        break;
                }
                rectangle.setFill(selectedColor);

            }});

        textsAndColors.setAlignment(Pos.CENTER);
        textsAndColors.setSpacing(10);

        VBox textsAndSelectionText = new VBox(DefaultUISpecifications.SCREEN_HEIGHT / 100);
        Text youveSelected = new Text("");
        youveSelected.setFont(flarge);
        youveSelected.setFill(Color.WHITE);

        textsAndSelectionText.setAlignment(Pos.CENTER);
        textsAndSelectionText.getChildren().addAll(textsAndColors);

        StackPane[] civilizationBoxes = new StackPane[6];
        for(Civilization.CivType e: Civilization.CivType.values()){
            StackPane civilizationBox = new StackPane();
            Rectangle backgroundForCivilizations = new Rectangle(widthOfACivBack,heightOfACivBack);
            backgroundForCivilizations.setFill(Color.DARKGREY);
            civilizationBox.setOnMouseEntered(event->{
                civilizationBox.setEffect(drop);
                civilizationBox.scaleXProperty().set(1.02);
                civilizationBox.scaleYProperty().set(1.02);
            });
            civilizationBox.setOnMouseExited(event -> {
                boolean b = true;
                try {
                    b = !selectedCiv.name().equals(e.name());
                }catch (NullPointerException m){
                }
                if(b){
                    civilizationBox.setEffect(null);
                    civilizationBox.scaleXProperty().set(1);
                    civilizationBox.scaleYProperty().set(1);
                }
            });
            civilizationBox.setOnMouseClicked(event -> {
                selectedCiv = e;
                for (int s = 0; s<Civilization.CivType.values().length; s++){
                    civilizationBoxes[s].setEffect(null);
                    civilizationBoxes[s].scaleXProperty().set(1);
                    civilizationBoxes[s].scaleYProperty().set(1);
                }
                civilizationBox.setEffect(drop);
                civilizationBox.scaleXProperty().set(1.02);
                civilizationBox.scaleYProperty().set(1.02);
                selectedCiv = e;
                Sound.coolSelection(pane);
                youveSelected.setText(capitalizeFirstLetter(e.name()));
                youveSelected.setEffect(new DropShadow(5, Color.WHITE));
            });

            MenuButton selectButton = new MenuButton(e.name(),pane);
            selectButton.setDisable(true);
            selectButton.setPrefWidth(widthOfACivBack);

            VBox thingsOtherThanBackground = new VBox();
            thingsOtherThanBackground.getChildren().add(selectButton);

            thingsOtherThanBackground.setAlignment(Pos.BOTTOM_CENTER);
            Image bg = getSelectedCivBackgrounds(e);
            backgroundForCivilizations.setFill(new ImagePattern(bg));
            civilizationBox.getChildren().addAll(backgroundForCivilizations,thingsOtherThanBackground);
            if(i < 3)
                lineOne.getChildren().add(civilizationBox);
            else
                lineTwo.getChildren().add(civilizationBox);
            civilizationBoxes[i] = civilizationBox;
            i++;
        }

        lineOne.setSpacing(DefaultUISpecifications.SCREEN_WIDTH / 50);
        lineOne.setAlignment(Pos.CENTER);

        lineTwo.setSpacing(DefaultUISpecifications.SCREEN_WIDTH / 50);
        lineTwo.setAlignment(Pos.CENTER);

        vBoxCivilizations.setAlignment(Pos.CENTER);
        vBoxCivilizations.setSpacing(DefaultUISpecifications.SCREEN_WIDTH / 140);
        vBoxCivilizations.getChildren().addAll(youveSelected,lineOne,lineTwo);

        MenuButton returnBack = new MenuButton("Exit",pane);
        returnBack.setOnMouseClicked(e->{
            System.exit(0);
        });

        MenuButton continueToGame = new MenuButton("Continue",pane);
        continueToGame.setDisable(true);
        continueToGame.setOpacity(0.5);
        BooleanBinding booleanBind = textFieldTypeYourName.textProperty().isEmpty().or(youveSelected.textProperty().isEmpty()).or(colorNames[0].valueProperty().isNull());
        continueToGame.disableProperty().bind(booleanBind);
        continueToGame.opacityProperty().bind(Bindings.when(booleanBind).then(0.5).otherwise(1));
        continueToGame.setOnMouseClicked(e->{
            name = textFieldTypeYourName.getText();
            try {
                colorNames[0].getSelectionModel().clearSelection();
                colorNames[0].getItems().remove(selectedColorString);
            }catch (Exception ex){};




            if(gametype.equals("MULTI")) {
                cc.countDown();
            }else if(gametype.equals("SINGLE")){
                if(currenti < numberOfPlayers - 1){
                    players[currenti] = new Player(selectedColor,selectedCiv,name);
                    System.out.println("CURRENT I IS " + currenti);
                    HBox a = new HBox(3);
                    Text nameOfThePlayer = new Text(players[currenti].name);
                    nameOfThePlayer.setText(nameOfThePlayer.getText() + " as");
                    Text civOfThePlayer = new Text(players[currenti].getCivilizationType().name());
                    StringBuilder sb = new StringBuilder(civOfThePlayer.getText().toLowerCase());
                    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                    civOfThePlayer.setText(sb.toString());
                    a.getChildren().addAll(nameOfThePlayer,civOfThePlayer);
                    playersListView.getItems().add(a);
                    textFieldTypeYourName.setText("");
                    youveSelected.setText("");
                    currenti++;
                } else{
                    try {
                        players[currenti] = new Player(selectedColor,selectedCiv,name);
                        gameview.setScene(new SingleGameScene(gameview,players,numberOfPlayers).getScene());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        playersListView.setStyle("-fx-control-inner-background: darkgray;-fx-text-fill: white;");
        playersListView.setOpacity(0.7);

        VBox playerListBox = new VBox();
        playerListBox.getChildren().add(playersListView);
        playerListBox.setAlignment(Pos.CENTER_RIGHT);

        HBox buttons = new HBox(widthOfACivBack / 8);
        buttons.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(returnBack,continueToGame, playerListBox);


        textsAndCivilizations.setAlignment(Pos.CENTER);
        textsAndCivilizations.getChildren().addAll(textsAndSelectionText,vBoxCivilizations,buttons);
        textsAndCivilizations.setSpacing(DefaultUISpecifications.SCREEN_HEIGHT / 40);

        ColorAdjust adjustment = new ColorAdjust(0, -0.5, -0.5, 0);

        ImageView a = (ImageView) getBackgroundImage();
        a.setEffect(adjustment);

        pane.getChildren().addAll(a,rectangle,textsAndCivilizations);
        Scene scene;
  /*      if(gametype.equals("SINGLE"))
             scene = new Scene(addPlayerList);
        else {*/
             scene = new Scene(pane);
        //}
        gameview.setScene(scene);
        gameview.show();
    }

    private String capitalizeFirstLetter(String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    private Node getBackgroundImage() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/MainMenu/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }

    private Image getSelectedCivBackgrounds(Civilization.CivType type) throws IOException{
        InputStream is;
        Image img;
        switch (type){
            case OTTOMANS:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/ottomanselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case SPAIN:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/spainselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case FRANCE:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/franceselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case MAYA:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/mayaselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case ENGLAND:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/englandselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case TURKEY:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/turkeyselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
        }

        return null;
    }

    public Color getColor() {
        return selectedColor;
    }

    public Civilization.CivType getCivType() {
        return selectedCiv;
    }

    public String getName(){
        return name;
    }
}