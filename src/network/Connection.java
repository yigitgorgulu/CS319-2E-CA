package network;

import display.networkDisplay.NetworkGameScene;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

public abstract class Connection extends Thread {
    // properties
    ObjectOutputStream os;
    Stage gameView;
    Stage popUp;
    NetworkGameScene networkGameScene;
    CountDownLatch mapLatch;

    // constructors
    public Connection(Stage gameView) {
        super();
        this.gameView = gameView;
    }

    // methods
    public abstract void send(Serializable data) throws Exception;

    @Override
    public abstract void run();


    public void closePopUp(){
        popUp.close();
    }

    public void setPopUp(Stage window) {
        popUp = window;
    }
}
