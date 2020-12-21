package Controllers;

import Scene.GameScene;
import StorageRelatedClasses.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable
{
    @FXML public static Pane map_pane;
    @FXML public ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16,
            img17, img18, img19, img20, img21, img22, img23, img24, img25, img26, img27, img28, img29, img30, img31, img32, img33, img34,
            img35, img36;
    @FXML public Label num1_lbl, num2_lbl, num3_lbl, num4_lbl, num5_lbl, num6_lbl, num7_lbl, num8_lbl, num9_lbl, num10_lbl, num11_lbl, num12_lbl,
            num13_lbl, num14_lbl, num15_lbl, num16_lbl, num17_lbl, num18_lbl, num19_lbl, num20_lbl, num21_lbl, num22_lbl, num23_lbl,
            num24_lbl, num25_lbl, num26_lbl, num27_lbl, num29_lbl, num30_lbl, num31_lbl, num32_lbl, num33_lbl, num34_lbl, num35_lbl, num36_lbl;
    @FXML public Label p1Nick_lbl, p2Nick_lbl, p3Nick_lbl, p4Nick_lbl;
    @FXML public GridPane settings_grid;
    @FXML public VBox nicknames_vbox;
    int turnOwner = 1;
    ArrayList<Player> players;

    @Override
    public void initialize( URL location, ResourceBundle resources )
    {
        players = GameScene.players;
        int index = 0;
        for (Player p : players)
        {
            Label label = (Label) nicknames_vbox.getChildren().get(index);
            label.setText(p.getNickname());
            index++;
        }
        p1Nick_lbl.setStyle("-fx-border-color: white;");
    }

    public ArrayList<String> divideString(String strWillBeDivided)
    {
        ArrayList<String> allParts = new ArrayList<>();

            while (!strWillBeDivided.equals("")) {
                int index = 0;
                if (strWillBeDivided.contains(",")) {
                    while (strWillBeDivided.charAt(index) != ',') {
                        index++;
                    }
                    String part = strWillBeDivided.substring(0, index);
                    allParts.add(part);
                    strWillBeDivided = strWillBeDivided.substring(index + 1);
                }
                else {
                    allParts.add(strWillBeDivided);
                    strWillBeDivided = "";
                }
            }
        return allParts;
    }

    @FXML
    public void regionClicked(MouseEvent e)
    {
        ImageView x = (ImageView)e.getSource();
        System.out.println(x.getId());
        int countryIndex = Integer.parseInt(x.getId().substring(3));
    }

    @FXML
    public void howToPlayClicked()
    {
    }

    @FXML
    public void settingsClicked(MouseEvent e)
    {
        if (settings_grid.isVisible())
            settings_grid.setVisible(false);
        else
            settings_grid.setVisible(true);
    }

    @FXML
    public void cardsClicked()
    {

    }
}