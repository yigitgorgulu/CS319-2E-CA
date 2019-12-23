package display;

import com.sun.glass.ui.Menu;
import display.networkDisplay.ClientLobbyScene;
import game.Sound;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.ServerConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PopUp {
    Font fNormal;
    Font fLarge;
    final double LARGE_FONT_SIZE = 120;
    final double NORMAL_FONT_SIZE = 30;

    private Stage window;
    private Stage gameView;
    private Scene scene;
    private Pane paneWillBeBlurredOut;
    private Double widthOfPopUp;
    private Double heightOfPopUp;
    private ServerConnection connection;
    private int i;
    private SimpleBooleanProperty boolProperty;
    private int noOfPlayers;
    private CountDownLatch cc;
    /**
     * @param key                  PopUp Class has different pop-ups for different uses. Key is an identifier to be able to construct
     *                             the one we want. Code has different switch and cases.
     * @param paneWillBeBlurredOut The pane which needs to be blurred.
     * @param stageItWillBeAdded   Stage which pop up will be shown-on.
     * @param connection           Connection is the connection which is creatin in Server Lobby Scene. Necessary to get necessary data about players.
     * @param booleanProperty
     * @throws FileNotFoundException
     */
    public PopUp(String key, Pane paneWillBeBlurredOut, Stage stageItWillBeAdded, ServerConnection connection, SimpleBooleanProperty booleanProperty) throws FileNotFoundException {

        this.gameView = stageItWillBeAdded;
        this.paneWillBeBlurredOut = paneWillBeBlurredOut;
        this.connection = connection;
        this.boolProperty = booleanProperty;;

        fLarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);
        fNormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        widthOfPopUp = DefaultUISpecifications.SCREEN_WIDTH / 3;
        heightOfPopUp = DefaultUISpecifications.SCREEN_HEIGHT / 3;

        window.setWidth(widthOfPopUp);
        window.setHeight(heightOfPopUp);

        //Put window to the center
        window.setX(DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfPopUp / 2);
        window.setY(DefaultUISpecifications.SCREEN_HEIGHT / 2 - heightOfPopUp / 2);

        //remove the Windows frame around the stage
        window.initStyle(StageStyle.UNDECORATED);

        //Blur the background, then constructing the pop-up
        blurBackground(paneWillBeBlurredOut);

        switch (key) {
            case "ASK_IP":
                askIP();
                break;
            case "WAITING":
                waiting();
                break;
            case "SETTINGS":
                settings();
                break;
            case "SINGLE_PLAYER":
                singlePlayer();
                break;
        }
    }

    private void singlePlayer() throws FileNotFoundException {
        Text playerQuantity = new Text("Number Of Players");
        final ComboBox<Integer> playerCount = new ComboBox<>();
        playerCount.getItems().addAll(2,3,4,5,6);
        playerCount.setStyle("-fx-focus-color: transparent; -fx-border-color: transparent; -fx-alignment: center");
        VBox playerStuff = new VBox(15);
        playerStuff.getChildren().addAll(playerQuantity,playerCount);
        playerStuff.setAlignment(Pos.CENTER);

        MenuButton exitButton = new MenuButton("Return",paneWillBeBlurredOut);
        exitButton.setOnMouseClicked(e -> {
            close();
        });
        MenuButton joinButton = new MenuButton("Continue",paneWillBeBlurredOut);
        joinButton.setOnMouseClicked(e -> {
            noOfPlayers = playerCount.getValue();
            close();
            try {
                new CivilizationSelectionScene(gameView,null,noOfPlayers,"SINGLE");
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        HBox buttons = new HBox(5);
        buttons.getChildren().addAll(exitButton,joinButton);
        buttons.setAlignment(Pos.CENTER);

        VBox all = new VBox();
        all.getChildren().addAll(playerStuff,buttons);
        all.setAlignment(Pos.CENTER);
        all.setSpacing(20);

        scene = new Scene(all);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void continueToRun(CountDownLatch cc){
        cc.countDown();
    }


    private void settings() throws FileNotFoundException {
        Text textVolumeSliderForMusic = new Text("Volume");

        Slider volumeSliderForMusic = new Slider();
        volumeSliderForMusic.setPrefWidth( 2 * widthOfPopUp / 3);
        volumeSliderForMusic.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSliderForMusic.setMinWidth(40);
        volumeSliderForMusic.setValue(Sound.musicVolume * 100);
        volumeSliderForMusic.setStyle("-fx-focus-color: gray; ");
        volumeSliderForMusic.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                volumeSliderForMusic.setValueChanging(true);
                double value = (event.getX()/volumeSliderForMusic.getWidth())*volumeSliderForMusic.getMax();
                volumeSliderForMusic.setValue(value);
                volumeSliderForMusic.setValueChanging(false);
            }
        });

        VBox volumeSliderAndText = new VBox(10);
        volumeSliderAndText.setAlignment(Pos.CENTER);
        volumeSliderAndText.setSpacing(20);
        volumeSliderAndText.getChildren().addAll(textVolumeSliderForMusic,volumeSliderForMusic);

        Text textVolumeSliderForSoundFX = new Text("Sound FX");

        Slider volumeSliderForSoundFX = new Slider();
        volumeSliderForSoundFX.setPrefWidth(2 * widthOfPopUp / 3);
        volumeSliderForSoundFX.setMaxWidth(Region.USE_PREF_SIZE);
        volumeSliderForSoundFX.setMinWidth(40);
        volumeSliderForSoundFX.setValue(Sound.soundFXVolume * 100);
        volumeSliderForSoundFX.setStyle("-fx-focus-color: gray; ");
        volumeSliderForSoundFX.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                volumeSliderForSoundFX.setValueChanging(true);
                double value = (event.getX()/volumeSliderForSoundFX.getWidth())*volumeSliderForSoundFX.getMax();
                volumeSliderForSoundFX.setValue(value);
                volumeSliderForSoundFX.setValueChanging(false);
            }
        });

        VBox fxSliderAndText = new VBox(10);
        fxSliderAndText.setSpacing(20);
        fxSliderAndText.setAlignment(Pos.CENTER);
        fxSliderAndText.getChildren().addAll(textVolumeSliderForSoundFX,volumeSliderForSoundFX);

        MenuButton returnButton = new MenuButton("Return",paneWillBeBlurredOut);
        returnButton.setOnMouseClicked(e->{
            close();
        });


        VBox allSettings = new VBox(20);
        allSettings.setSpacing(20);
        allSettings.setAlignment(Pos.CENTER);
        allSettings.getChildren().addAll(volumeSliderAndText,fxSliderAndText,returnButton);

        Sound.bgMusicControl(volumeSliderForMusic);
        Sound.soundFXVolumeController(volumeSliderForSoundFX);


        scene = new Scene(allSettings);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * waiting is the pop-up function which is written to construct a pop-up for the ServerLobbyScene.
     * This pop-up shows up when the creation of server is started, it ends when all players are connected.
     * It shows the current player's quantity and names. It updates itself every second.
     */
    private void waiting() throws FileNotFoundException {
        Text waiting = new Text("Waiting for other players");
        waiting.setFont(fNormal);

        String[] waitingTexts = new String[4];
        fillTheWaitingTexts(waitingTexts);

        Text hmPlayersArrived = new Text("1/" + connection.getMaxPlayerCount());

        updateWaitingText(waiting, waitingTexts, hmPlayersArrived);

        VBox waitingText = new VBox();
        MenuButton returnButton = new MenuButton("return" ,paneWillBeBlurredOut);
        returnButton.setOnMouseClicked(e -> {
            if (paneWillBeBlurredOut != null){
                unblurBackground(paneWillBeBlurredOut);
                boolProperty.set(false);
            }
            close();
        });
        waitingText.setAlignment(Pos.CENTER);
        waitingText.getChildren().addAll(waiting, hmPlayersArrived);
        VBox buttonAndTexts = new VBox();
        buttonAndTexts.getChildren().addAll(waitingText, returnButton);
        buttonAndTexts.setSpacing(50);
        buttonAndTexts.setAlignment(Pos.CENTER);

        //Sets pop up to the window
        scene = new Scene(buttonAndTexts);
        window.setScene(scene);
        connection.setPopUp(window);
        window.showAndWait();
    }

    /**
     * fills the waiting texts array. This function was necessary to make waiting() more clear.
     *
     * @param waitingTexts
     */
    private void fillTheWaitingTexts(String[] waitingTexts) {
        waitingTexts[0] = "Waiting for other players";
        waitingTexts[1] = "Waiting for other players.";
        waitingTexts[2] = "Waiting for other players..";
        waitingTexts[3] = "Waiting for other players...";
    }

    /**
     * @param waiting          The text which will be updated every second.
     * @param waitingTexts     Array of "waiting for other players" which consist different dots.
     * @param hmPlayersArrived information of how many players are connected so far
     */
    private void updateWaitingText(Text waiting, String[] waitingTexts, Text hmPlayersArrived) {
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                waiting.setText(waitingTexts[i % 4] + "");
                hmPlayersArrived.setText(connection.getCurrentPlayerCount() + "/" + connection.getMaxPlayerCount());
                i++;
            }
        };
        waiting.setText(waitingTexts[0] + "");
        exec.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * askIP is for Main Menu. when The user clicks on the join game button, it constructs this pop-up.
     *
     * @throws FileNotFoundException
     */
    private void askIP() throws FileNotFoundException {
        Text typeInIP = new Text("Please type the IP address");

        TextField field = new TextField();
        field.setAlignment(Pos.CENTER);
        field.setMaxWidth(widthOfPopUp / 3);
        field.setStyle("-fx-focus-color: transparent;");

        MenuButton exitButton = new MenuButton("Return",paneWillBeBlurredOut);
        exitButton.setOnMouseClicked(e -> {
            close();
        });
        SimpleBooleanProperty connectionFailed = new SimpleBooleanProperty(false);
        MenuButton joinButton = new MenuButton("Join",paneWillBeBlurredOut);
        joinButton.setOnMouseClicked(e -> {
            if ((field.getText().matches("[0-9.]*") && field.getText().length() > 2) || field.getText().equals("localhost")) {
            }
            try {
                connectionFailed.set(false);
                ClientLobbyScene clientLobbyScene = new ClientLobbyScene(gameView, field.getText(), connectionFailed);
                clientLobbyScene.setPopUp(window);
                joinButton.setOpacity(0.5);
                field.setDisable(true);
                field.disableProperty().bind(connectionFailed.not());
                joinButton.disableProperty().bind(connectionFailed.not());
                joinButton.opacityProperty().bind(Bindings.when(connectionFailed).then(1).otherwise(0.5));
                typeInIP.textProperty().bind(Bindings.when(connectionFailed).then("Connection failed. Please check your connection status and IP.").otherwise("Trying to connect"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(exitButton, joinButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        VBox layoutA = new VBox(10);
        layoutA.getChildren().addAll(typeInIP, field, buttons);
        layoutA.setAlignment(Pos.CENTER);

        Stop[] stops = new Stop[]{new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        scene = new Scene(layoutA);

        window.setScene(scene);
        window.showAndWait();
        scene.setFill(lg1);
    }

    /**
     * Blurs the background pane
     *
     * @param pane pane which will be blurred
     * @throws FileNotFoundException
     */
    public void blurBackground(Pane pane) throws FileNotFoundException {
        ColorAdjust adjustment = new ColorAdjust(0, -0.2, -0.2, 0);
        GaussianBlur blur = new GaussianBlur(145); // 55 is just to show edge effect more clearly.
        adjustment.setInput(blur);
        pane.setEffect(adjustment);
    }

    /**
     * Unblurs the background pane
     *
     * @param pane pane which will be blurred
     */
    public void unblurBackground(Pane pane) {
        ColorAdjust adjustment = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
        adjustment.setInput(blur);
        pane.setEffect(adjustment);
    }

    public void close() {
        window.close();
        unblurBackground(paneWillBeBlurredOut);
    }



    public int getPlayerCount() {
        return noOfPlayers;
    }

    public void setCC(CountDownLatch cc) {
        this.cc = cc;
    }
}
