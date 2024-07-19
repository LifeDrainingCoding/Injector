package com.lifedrained.injector.injector.controllers;

import com.lifedrained.injector.injector.backend.Singleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import static com.lifedrained.injector.injector.backend.Singleton.*;

public class GuideController {
    @FXML
    private Button playBtn, replayBtn, seekOneMBtn, seekOnePBtn, closeBtn;
    @FXML
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    @FXML
    void initialize() {
        initMediaPlayer();
        initBtns();
    }
    private void initBtns(){
        playBtn.setOnMouseClicked(event -> {
            if(isPlaying){
                mediaPlayer.pause();
                isPlaying = false;
            }else {
                mediaPlayer.play();
                isPlaying = true;
            }
        });
        replayBtn.setOnMouseClicked(event -> {
            mediaPlayer.seek(Duration.millis(0));
        });
        seekOneMBtn.setOnMouseClicked(event -> {
            Duration currentDurration=  mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentDurration.subtract(new Duration(1000)));
        });
        seekOnePBtn.setOnMouseClicked(event -> {
            Duration currentDurration=  mediaPlayer.getCurrentTime();
            mediaPlayer.seek(currentDurration.add(new Duration(1000)));
        });
        closeBtn.setOnMouseClicked(event -> {
            mediaPlayer.stop();
            isPlaying = false;
            mediaPlayer.dispose();
            Singleton.getInstance().getGuideStage().close();

        });
    }
   private void initMediaPlayer(){
        getInstance();
        Media media = new Media(getClass().getResource("/com/lifedrained/injector/injector/guide/guide.mp4").toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setPreserveRatio(true);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            isPlaying = true;
            mediaPlayer.setMute(true);
        });

   }
}
