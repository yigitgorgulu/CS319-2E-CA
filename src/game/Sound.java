package game;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class Sound {

    public Sound(){

    }

     public static void gymnopedie(Pane scene) {
        String musicFile = "res/sound/gymnopedie.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        MediaView mediaView = new MediaView(mediaPlayer);
        (scene).getChildren().add(mediaView);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public static void menuButtonSound(Pane scene) {
        String musicFile = "res/sound/click.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        MediaView mediaView = new MediaView(mediaPlayer);
        (scene).getChildren().add(mediaView);

        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }

    public static void coolSelection(Pane scene){
        String musicFile = "res/sound/selection.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        MediaView mediaView = new MediaView(mediaPlayer);
        (scene).getChildren().add(mediaView);

        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }
}