package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    MediaPlayer player;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playID;

    @FXML
    private Slider progressID;

    @FXML
    private Slider volumeID;


    @FXML
    void openSongMenu(ActionEvent event) {
        try {
           FileChooser chooser = new FileChooser();
           File file = chooser.showOpenDialog(null);
           Media pick = new Media(file.toURI().toURL().toString());


            if (player != null) {

                player.dispose();
            }

            player = new MediaPlayer(pick);
            mediaView.setMediaPlayer(player);

            player.setOnReady(() -> {
                progressID.setMin(0);
                progressID.setMax(player.getMedia().getDuration().toMinutes());
                progressID.setValue(0);
            });

            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    Duration d = player.getCurrentTime();

                    progressID.setValue(d.toMinutes());
                }
            });

            progressID.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (progressID.isPressed()) {
                        double val = progressID.getValue();
                        player.seek(new Duration(val * 60 * 1000));
                    }
                }
            });

            volumeID.setValue(player.getVolume()*100);
            volumeID.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    player.setVolume(volumeID.getValue()/100);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void play(ActionEvent event) {

        try {

            MediaPlayer.Status status = player.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {
                player.pause();
               playID.setText("Play");
            } else{
                player.play();
                playID.setText("Pause");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void stopBtnClick(ActionEvent event) {
        player.stop();
        playID.setText("Play");
    }


    @FXML
    void preBtnClick(ActionEvent event) {
        double dur = player.getCurrentTime().toSeconds();

        dur = dur - 10;

        player.seek(new Duration(dur * 1000));

    }


    @FXML
    void fwdBtnClick(ActionEvent event) {
        double dur = player.getCurrentTime().toSeconds();

        dur = dur + 10;

        player.seek(new Duration(dur * 1000));
    }
}
