package Scene;

import Main.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class SettingScene
{
    public SettingScene() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/Scene/SettingsScene.fxml"));
        Main.stage.getScene().setRoot(root);
    }
}
