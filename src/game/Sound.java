package game;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class Sound {

    private static Media bgMusic;
    private static MediaPlayer bgMusicMediaPlayer;
    private static Media click;
    private static MediaPlayer clickMediaPlayer;
    private static Media coolSelection;
    private static MediaPlayer coolSelectionPlayer;
    public static double soundFXVolume = 1;
    public static double musicVolume = 1;

    public Sound(){

    }

     public static void gymnopedie(Pane scene) {
        bgMusic = new Media(new File("res/sound/gymnopedie.mp3").toURI().toString());
        bgMusicMediaPlayer = new MediaPlayer(bgMusic);
        MediaView mediaView = new MediaView(bgMusicMediaPlayer);
        (scene).getChildren().add(mediaView);
         bgMusicMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
         bgMusicMediaPlayer.play();
    }

    public static void bgMusicControl(Slider volumeSlider){
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    bgMusicMediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
                    musicVolume = volumeSlider.getValue() / 100;
                }
            }
        });
    }

    public static void menuButtonSound(Pane scene) {
        click = new Media(new File("res/sound/click.mp3").toURI().toString());
        clickMediaPlayer = new MediaPlayer(click);

        MediaView mediaView = new MediaView(clickMediaPlayer);
        (scene).getChildren().add(mediaView);
        clickMediaPlayer.setCycleCount(1);
        clickMediaPlayer.setVolume(soundFXVolume);
        clickMediaPlayer.play();
    }

    public static void coolSelection(Pane scene){
        coolSelection = new Media(new File("res/sound/selection.mp3").toURI().toString());
        coolSelectionPlayer = new MediaPlayer(coolSelection);

        MediaView mediaView = new MediaView(coolSelectionPlayer);
        (scene).getChildren().add(mediaView);
        coolSelectionPlayer.setCycleCount(1);
        coolSelectionPlayer.setVolume(soundFXVolume);
        coolSelectionPlayer.play();
    }

    public static void soundFXVolumeController(Slider volumeSliderForSoundFX) {
        volumeSliderForSoundFX.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSliderForSoundFX.isValueChanging()) {
                    soundFXVolume = volumeSliderForSoundFX.getValue() / 100;
                }
            }
        });
    }
}