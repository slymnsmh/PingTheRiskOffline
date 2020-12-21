package Controllers;

import Scene.CreditsScene;
import Scene.HowToPlayScene;
import Scene.SettingScene;
import StorageRelatedClasses.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable
{
    @FXML public VBox nicknames_vbox;
    @FXML public TextField nickname1_tf, nickname2_tf, nickname3_tf, nickname4_tf;
    @FXML public Button newGame_btn, settings_btn, credits_btn, howToPlay_btn, exit_btn;
    ArrayList<Player> players = new ArrayList<>();
    //static AudioStream audios;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //this.music();
    }

    @FXML
    private void newGameClicked (ActionEvent e) throws Exception
    {
        
    }

    @FXML
    private void settingsClicked (ActionEvent e) throws Exception
    {
        SettingScene settingScene = new SettingScene();
    }

    @FXML
    private void creditsClicked (ActionEvent e) throws Exception
    {
        CreditsScene creditsScene = new CreditsScene();
    }

    @FXML
    private void howToPlayClicked (ActionEvent e) throws Exception
    {
        HowToPlayScene howToPlayScene = new HowToPlayScene();
    }

    @FXML
    private void exitClicked (ActionEvent e) throws Exception
    {
        System.exit(0);
    }

    /*public void music() {
        try {
            InputStream music = new FileInputStream(new File("src/Controllers/Solar.wav"));
            audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        } catch (Exception var3) {
            JOptionPane.showMessageDialog((Component)null, "Error");
        }

    }

    public static AudioStream getMusic() {
        return audios;
    }*/

    /*private void changeScene(String filePath) throws IOException
    {
        Parent newGameMenuParent = FXMLLoader.load(getClass().getResource(filePath));
        Scene newGameMenuScene = new Scene(newGameMenuParent);
        sample.Main.stage.setScene(newGameMenuScene);
        sample.Main.stage.show();
        CommonUIController.isSoundOn = isSoundOn;
    }*/
}
