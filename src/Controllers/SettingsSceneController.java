package Controllers;

import Main.Main;
import Scene.MainScene;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class SettingsSceneController implements Initializable {
    @FXML public Slider sound_slider;
    @FXML
    ImageView background_img;
    @FXML
    AnchorPane main_pane;
    @FXML
    Pane menu_pane;
    @FXML
    MenuButton display_menuBtn;

    @FXML
    public void backClicked() throws IOException {
        MainScene mainScene = new MainScene();
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.readFile();
        sound_slider.setValue(Main.mediaPlayer.getVolume() * 100);
        sound_slider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Main.mediaPlayer.setVolume(sound_slider.getValue() / 100);
            }
        });
    }

    @FXML
    public void saveClicked() throws IOException {
        String filePath = "src/Controllers/Settings.txt";
        Scanner sc = new Scanner(new File(filePath));
        StringBuffer buffer = new StringBuffer();

        while(sc.hasNextLine()) {
            buffer.append(sc.nextLine() + System.lineSeparator());
        }

        String fileContents = buffer.toString();
        System.out.println("Contents of the file: " + fileContents);
        sc.close();
        String oldFrameSize = "\\d+[x]\\d+";
        String newFrameSize = this.display_menuBtn.getText();
        String oldSound = "\\d+[.]\\d+";
        String newSound = String.valueOf(this.sound_slider.getValue());

        try {
            //AudioPlayer.player.stop(MainSceneController.audios);
            System.out.println("sound3" + newSound);
            fileContents = fileContents.replaceAll(oldSound, newSound);
            fileContents = fileContents.replaceAll(oldFrameSize, newFrameSize);
            System.out.println("sound4" + newSound);
        } catch (Exception var10) {
            JOptionPane.showMessageDialog((Component)null, "Error");
        }

        FileWriter writer = new FileWriter(filePath);
        System.out.println("new data: " + fileContents);
        writer.append(fileContents);
        writer.flush();
        this.applySettings();
    }

    public void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/Controllers/settings.txt"));
            String line = reader.readLine();
            this.display_menuBtn.setText(line);
            line = reader.readLine();
            this.sound_slider.adjustValue(Double.parseDouble(line));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void applySettings() throws IOException {
        int index;
        for(index = 0; this.display_menuBtn.getText().charAt(index) != 'x'; ++index) {
        }

        double width = Double.parseDouble(this.display_menuBtn.getText().substring(0, index));
        double height = Double.parseDouble(this.display_menuBtn.getText().substring(index + 1, this.display_menuBtn.getText().length()));
        System.out.println("W: " + width + "\nH: " + height);
        Main.stage.setHeight(height);
        Main.stage.setWidth(width);
        this.main_pane.setPrefHeight(height);
        this.main_pane.setPrefWidth(width);
        this.menu_pane.setPrefHeight(height);
        this.menu_pane.setPrefWidth(width);
        this.background_img.setFitHeight(height);
        this.background_img.setFitWidth(width);
        Main.stage.setMaximized(true);
    }

    @FXML
    public void menuItemClicked(ActionEvent event) {
        MenuItem clicked = (MenuItem)event.getSource();
        this.display_menuBtn.setText(clicked.getText());
    }

    public void mainMenuClicked() throws IOException {
        new MainScene();
    }
}
