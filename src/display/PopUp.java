package display;

import display.networkDisplay.ClientLobbyScene;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
import network.Connection;
import network.ServerConnection;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Stack;
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

    /**
     *
     * @param key
     * PopUp Class has different pop-ups for different uses. Key is an identifier to be able to construct
     * the one we want. Code has different switch and cases.
     * @param paneWillBeBlurredOut
     * The pane which needs to be blurred.
     * @param stageItWillBeAdded
     * Stage which pop up will be shown-on.
     * @param connection
     * Connection is the connection which is creatin in Server Lobby Scene. Necessary to get necessary data about players.
     * @throws FileNotFoundException
     */
    public PopUp(String key, Pane paneWillBeBlurredOut, Stage stageItWillBeAdded, ServerConnection connection) throws FileNotFoundException {

        this.gameView = stageItWillBeAdded;
        this.paneWillBeBlurredOut = paneWillBeBlurredOut;
        this.connection = connection;

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

        switch (key){
            case "ASK_IP":
                askIP();
                break;
            case "WAITING":
                waiting();
                break;
        }
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

        Text hmPlayersArrived = new Text( "1/" + connection.getMaxPlayerCount());

        updateWaitingText(waiting, waitingTexts, hmPlayersArrived);

        VBox waitingText = new VBox();
        MenuButton returnButton = new MenuButton("return");
        returnButton.setOnMouseClicked(e->{
            if(paneWillBeBlurredOut != null)
                unblurBackground(paneWillBeBlurredOut);
            close();
        });
        waitingText.setAlignment(Pos.CENTER);
        waitingText.getChildren().addAll(waiting, hmPlayersArrived);
        VBox buttonAndTexts = new VBox();
        buttonAndTexts.getChildren().addAll(waitingText,returnButton);
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
     * @param waitingTexts
     */
    private void fillTheWaitingTexts(String[] waitingTexts) {
        waitingTexts[0] = "Waiting for other players";
        waitingTexts[1] = "Waiting for other players.";
        waitingTexts[2] = "Waiting for other players..";
        waitingTexts[3] = "Waiting for other players...";
    }

    /**
     * @param waiting
     * The text which will be updated every second.
     * @param waitingTexts
     * Array of "waiting for other players" which consist different dots.
     * @param hmPlayersArrived
     * information of how many players are connected so far
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
     * @throws FileNotFoundException
     */
    private void askIP() throws FileNotFoundException {
        Text typeInIP = new Text("Please type the IP address");

        TextField field = new TextField();
        field.setAlignment(Pos.CENTER);
        field.setMaxWidth(widthOfPopUp / 3);

        MenuButton exitButton = new MenuButton("Return");
        exitButton.setOnMouseClicked(e->{
            close();
            unblurBackground(paneWillBeBlurredOut);
        });

        MenuButton joinButton = new MenuButton("Join");
        joinButton.setOnMouseClicked(e->{
            if((field.getText().matches("[0-9.]*") && field.getText().length() > 2) || field.getText().equals("localhost") ){
                try {
                    ClientLobbyScene clientLobbyScene = new ClientLobbyScene(gameView,field.getText());
                    clientLobbyScene.setPopUp(window);
                    joinButton.setDisable(true);
                    joinButton.setOpacity(0.5);
                    //field.setEditable(false);
                    field.setDisable(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        HBox buttons = new HBox();
        buttons.getChildren().addAll(exitButton,joinButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        VBox layoutA = new VBox(10);
        layoutA.getChildren().addAll(typeInIP,field, buttons);
        layoutA.setAlignment(Pos.CENTER);

        Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);

        scene = new Scene(layoutA);

        window.setScene(scene);
        window.showAndWait();
        scene.setFill(lg1);
    }

    /**
     * Blurs the background pane
     * @param pane
     * pane which will be blurred
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
     * @param pane
     * pane which will be blurred
     */
    public void unblurBackground(Pane pane) {
        ColorAdjust adjustment = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
        adjustment.setInput(blur);
        pane.setEffect(adjustment);
    }

    public void close() {
        System.out.println("DFJKFKAS 250 POPUP");
        window.close();
    }
}
