package Managers;

import Scene.GameScene;
import StorageRelatedClasses.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("deprecation")
public class GameManager implements Initializable {
    public static Country baseCountry;
    public static Country targetCountry;
    private final int TOTAL_NUM_OF_COUNTRIES = 36;
    private final int TOTAL_NUM_OF_HACKERS = 120;
    @FXML
    public Pane map_pane;
    @FXML
    public ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15,
            img16,
            img17, img18, img19, img20, img21, img22, img23, img24, img25, img26, img27, img28, img29, img30, img31,
            img32, img33, img34,
            img35, img36;
    @FXML
    public Label num1_lbl, num2_lbl, num3_lbl, num4_lbl, num5_lbl, num6_lbl, num7_lbl, num8_lbl, num9_lbl, num10_lbl,
            num11_lbl, num12_lbl,
            num13_lbl, num14_lbl, num15_lbl, num16_lbl, num17_lbl, num18_lbl, num19_lbl, num20_lbl, num21_lbl,
            num22_lbl, num23_lbl,
            num24_lbl, num25_lbl, num26_lbl, num27_lbl, num28_lbl, num29_lbl, num30_lbl, num31_lbl, num32_lbl,
            num33_lbl,
            num34_lbl, num35_lbl, num36_lbl;
    @FXML
    public Label p1Nick_lbl, p2Nick_lbl, p3Nick_lbl, p4Nick_lbl;
    @FXML
    public Pane settings_pane;
    @FXML
    public HBox dice_pane;
    @FXML
    public VBox nicknames_vbox;
    @FXML
    public HBox cards_hbox;
    @FXML
    public Pane c1_img, c2_img, c3_img, c4_img, c5_img, c6_img, c7_img, c8_img, c9_img, c10_img, c11_img;
    @FXML
    public Button combineCards_btn;
    @FXML
    public Label info_lbl;
    @FXML
    public Pane cards_pane;
    @FXML
    public Pane howToPlay_pane;
    @FXML
    public Button hire_btn, hack_btn, fortify_btn;
    @FXML
    public ImageView cards_img;
    @FXML
    public SplitMenuButton hackerNum_menu;
    @FXML
    public Label infoGame_lbl;
    @FXML
    public Button nextPhase_btn;
    @FXML
    public ImageView chosenCard1, chosenCard2, chosenCard3;
    public int turnOwner = 1;
    public boolean first = true;
    @FXML
    public AnchorPane anchor_pane;
    ArrayList<Player> players;
    int playerNumber;
    String[] allColors = {"BLUE", "RED", "GREEN", "ORANGE"};
    int hackerNumBeginning;
    int countryNumBeginning;
    ArrayList<Integer> givenCountries = new ArrayList<>();
    ArrayList<Country> allCountries = new ArrayList<>();
    int turnType;
    int click = 0;
    boolean put = false;
    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            MenuItem item = (MenuItem) event.getSource();
            hackerNum_menu.setText(item.getText());
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        turnType = 1;
        players = GameScene.players;
        playerNumber = players.size();
        hackerNumBeginning = TOTAL_NUM_OF_HACKERS / playerNumber;
        countryNumBeginning = TOTAL_NUM_OF_COUNTRIES / playerNumber;
        try {
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void nextPhaseClicked() {
        if (nextPhase_btn.getText().equals("NEXT PHASE")) {
            turnType++;
            setPhase();
            baseCountry = null;
            targetCountry = null;
            first = true;
            updateCardsScene();
        } else {
            endTurn();
        }
    }

    public void getCountriesFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/StorageRelatedClasses/Countries.txt"));
        String line = reader.readLine();
        while (line != null) {
            line = line.substring(1);
            String countryId = line.substring(0, line.indexOf("|"));
            line = line.substring(line.indexOf("|") + 1);
            String countryName = line.substring(0, line.indexOf("|"));
            line = line.substring(line.indexOf("|") + 1);
            String location = line;
            Country country = new Country(Integer.parseInt(countryId), countryName, location);
            allCountries.add(country);
            line = reader.readLine();
        }
    }

    public void startGame() throws IOException {
        getCountriesFromFile();
        showNicknames();
        showTurnOwner();
        assignColorsToPlayers();
        assignHackerNumsToPlayers();
        assignCountriesToPlayers();
        assignHackerNumsToCountries();
        setCountryColors();
        setCountryHackerNumLabels();
        assignBonusHackers();
        updatePlayerProperties();
        setPhase();
        setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false, false);
    }

    public void assignBonusHackers() {
        if (first) {
            for (Player p : players) {
                p.setNumOfBonusHackers(40);
            }
            first = false;
        }
    }

    public void startTurn(int turnType) {
        decidePart(turnType);
    }

    public void setHackerNumMenu(int hackerNum, boolean zero, boolean lowerToBigger) {
        hackerNum_menu.getItems().clear();
        if (hackerNum == 0) {
            MenuItem item = new MenuItem("0");
            item.setOnAction(event);
            hackerNum_menu.getItems().add(item);
            hackerNum_menu.setText("0");
        } else {
            for (int i = 1; i <= hackerNum; i++) {
                if (zero) {
                    i = -1;
                    zero = false;
                    continue;
                }
                MenuItem item = new MenuItem(i + "");
                item.setOnAction(event);
                hackerNum_menu.getItems().add(item);
            }
            if (lowerToBigger)
                hackerNum_menu.setText(hackerNum_menu.getItems().get(0).getText());
            else
                hackerNum_menu.setText(hackerNum_menu.getItems().get(hackerNum - 1).getText());
        }
    }

    public void showNicknames() {
        int index = 0;
        for (Player p : players) {
            HBox hBox =
                    (HBox) nicknames_vbox.getChildren().get(nicknames_vbox.getChildren().size() - players.size() + index);
            Label nickname = (Label) hBox.getChildren().get(0);
            nickname.setText(p.getNickname());
            index++;
        }
        for (int i = 0; i < nicknames_vbox.getChildren().size() - players.size(); i++) {
            HBox hBox = (HBox) nicknames_vbox.getChildren().get(i);
            hBox.setVisible(false);
        }
    }

    public void showTurnOwner() {
        for (int i = 0; i < nicknames_vbox.getChildren().size(); i++) {
            HBox hBox = (HBox) nicknames_vbox.getChildren().get(i);
            Label label = (Label) hBox.getChildren().get(0);
            label.setStyle("-fx-border-color: transparent;");
        }
        HBox hBox =
                (HBox) nicknames_vbox.getChildren().get(nicknames_vbox.getChildren().size() - players.size() + turnOwner - 1);
        Label label = (Label) hBox.getChildren().get(0);
        label.setStyle("-fx-border-color: white;");
    }

    public void setPhase() {
        if (turnType == 1) {
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Hire");
            disableOtherPlayersCountries(players.get(turnOwner - 1));
            setHackerNumMenu(0, true, true);
            hire_btn.setDisable(false);
            hack_btn.setDisable(true);
            fortify_btn.setDisable(true);
            cards_pane.setVisible(false);
            combineCards_btn.setDisable(false);
            info_lbl.setText("You can combine your cards to get extra hackers.");
        } else if (turnType == 2) {
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Hack");
            disableOtherPlayersCountries(players.get(turnOwner - 1));
            setHackerNumMenu(0, true, true);
            hire_btn.setDisable(true);
            hack_btn.setDisable(false);
            fortify_btn.setDisable(true);
            cards_pane.setVisible(false);
            combineCards_btn.setDisable(true);
            info_lbl.setText("You can combine cards only in Hire Phase.");
        } else if (turnType == 3) {
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Fortify");
            disableOtherPlayersCountries(players.get(turnOwner - 1));
            setHackerNumMenu(0, true, true);
            hire_btn.setDisable(true);
            hack_btn.setDisable(true);
            fortify_btn.setDisable(false);
            cards_pane.setVisible(false);
            combineCards_btn.setDisable(false);
            info_lbl.setText("You can combine cards only in Hire Phase.");
            nextPhase_btn.setText("END TURN");
        }
    }

    public void decidePart(int turnType) {
        if (turnType == 1) {
            startHirePart();
        } else if (turnType == 2) {
            startHack();
        } else if (turnType == 3) {
            startFortifyPart();
        } else
            endTurn();
    }

    public void startHirePart() {
        if (baseCountry != null) {
            if (players.get(turnOwner - 1).getNumOfBonusHackers() != 0) {
                Hire hire = new Hire(players.get(turnOwner - 1), baseCountry,
                        Integer.parseInt(hackerNum_menu.getText()));
            }
            setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false, false);
        }
    }

    public void startHack() {
        Player attacker = players.get(turnOwner - 1);
        int win = attacker.getNumOfWins();
        Hack hack = new Hack(baseCountry, targetCountry, Integer.parseInt(hackerNum_menu.getText()), first);
        showDice(hack);
        updateScene(baseCountry, targetCountry);
        updatePlayerProperties();
        if (win != attacker.getNumOfWins()) {
            setCountryColors();
            infoGame_lbl.setText("Choose number of hackers to move to gained country!");
            setHackerNumMenu(baseCountry.getHackerNumber() - 1, true, true);
            hack_btn.setText("MOVE");
            nextPhase_btn.setDisable(true);
            first = false;
        } else {
            enableMap();
            setHackerNumMenu(Math.min(baseCountry.getHackerNumber() - 1, 3), false, false);
        }
        updateCardsScene();
    }

    public void disableMap() {
        for (int i = 0; i < TOTAL_NUM_OF_COUNTRIES; i++) {
            map_pane.getChildren().get(i).setDisable(true);
        }
    }

    public void enableMap() {
        for (int i = 0; i < TOTAL_NUM_OF_COUNTRIES; i++) {
            map_pane.getChildren().get(i).setDisable(false);
        }
    }

    public void disableOtherPlayersCountries(Player currentPlayer) {
        disableMap();
        for (Country c : currentPlayer.getCountries()) {
            map_pane.getChildren().get(c.getId() - 1).setDisable(false);
        }
    }

    public void startFortifyPart() {
        Fortify fortify = new Fortify(baseCountry, targetCountry, Integer.parseInt(hackerNum_menu.getText()));
    }

    public void endTurn() {
        for(int i = 0; i < players.size(); i++){
            if (players.get(i).getCountries().size() == 0) {
                players.remove(players.get(i));
                showNicknames();
            }
        }
        for (Player p : players) {
            if (p.getCountries().size() == 0) {
                players.remove(p);
                showNicknames();
            }
        }
        if (players.size() >= 2) {
            if (players.size() == turnOwner)
                turnOwner = 0;
            turnOwner++;
            updateCardsScene();
            turnType = 1;
            first = true;
            showTurnOwner();
            nextPhase_btn.setText("NEXT PHASE");
            setPhase();
            baseCountry = null;
            targetCountry = null;
            assignBonusHackers();
            setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false, false);
            startTurn(turnType);
        } else {
            endGame();
        }
    }

    public void setCountryColors() {

        for (int i = 0; i < TOTAL_NUM_OF_COUNTRIES; i++) {
            ImageView countryImageView = (ImageView) map_pane.getChildren().get(i);
            countryImageView.setPickOnBounds(false);
            if (playerNumber >= 1) {
                if (allCountries.get(i).getOwner() == players.get(0)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "b.png"));
                    continue;
                }
            }
            if (playerNumber >= 2) {
                if (allCountries.get(i).getOwner() == players.get(1)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "r.png"));
                    continue;
                }
            }
            if (playerNumber >= 3) {
                if (allCountries.get(i).getOwner() == players.get(2)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "g.png"));
                    continue;
                }
            }
            if (playerNumber >= 4) {
                if (allCountries.get(i).getOwner() == players.get(3)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "o.png"));
                }
            }
        }
    }

    public void setCountryHackerNumLabels() {
        for (int i = TOTAL_NUM_OF_COUNTRIES; i < 2 * TOTAL_NUM_OF_COUNTRIES - 1; i++) {
            Label label = (Label) map_pane.getChildren().get(i);
            label.setText(allCountries.get(i - TOTAL_NUM_OF_COUNTRIES).getHackerNumber() + "");
        }
    }

    public void assignColorsToPlayers() {
        int i = 0;
        for (Player p : players) {
            p.setColor(allColors[i]);
            i++;
        }
    }

    public void assignHackerNumsToPlayers() {
        for (Player p : players) {
            p.setNumOfHackers(hackerNumBeginning);
        }
    }

    public void assignCountriesToPlayers() {
        for (Player p : players) {
            ArrayList<Country> countries = new ArrayList<>();
            ArrayList<Integer> countryNumbers = generateRandomCountryNumbers(); //25 - 32 - 21
            for (int i : countryNumbers) {
                countries.add(allCountries.get(i - 1));
            }
            p.setCountries(countries);
            p.setNumOfCountries(countryNumBeginning);
            for (Country c : countries) {
                c.setOwner(p);
            }
        }
    }

    public ArrayList<Integer> generateRandomCountryNumbers() {
        int[] countryNumbers = new int[countryNumBeginning];
        int counter = 0;
        while (counter != countryNumbers.length) {
            int randomNumber = new Random().nextInt(TOTAL_NUM_OF_COUNTRIES) + 1;
            boolean isThere = false;
            for (int i = 0; i < countryNumbers.length; i++) {
                if (countryNumbers[i] == randomNumber)
                    isThere = true;
            }
            for (int i = 0; i < givenCountries.size(); i++) {
                if (givenCountries.get(i) == randomNumber)
                    isThere = true;
            }
            if (!isThere) {
                countryNumbers[counter] = randomNumber;
                givenCountries.add(randomNumber);
                counter++;
            }
        }
        ArrayList<Integer> countryNums = new ArrayList<>();
        for (int a : countryNumbers) {
            countryNums.add(a);
        }
        return countryNums;
    }

    public void assignHackerNumsToCountries() {
        int minHackerForACountry = hackerNumBeginning / countryNumBeginning;
        String checker = "";
        for (Country country : allCountries) {
            if (checker.equals("..")) {
                country.setHackerNumber(minHackerForACountry + 1);
                checker = "";
            } else if (checker.equals("") || checker.equals(".")) {
                country.setHackerNumber(minHackerForACountry);
                checker += ".";
            }
        }
    }

    @FXML
    public void regionClicked(MouseEvent e) {
        ImageView x = (ImageView) e.getSource();
        int countryIndex = Integer.parseInt(x.getId().substring(3));
        if (turnType == 1) {
            baseCountry = allCountries.get(countryIndex - 1);
        } else if (turnType == 2) {
            if (allCountries.get(countryIndex - 1).getOwner() == players.get(turnOwner - 1)) {
                baseCountry = allCountries.get(countryIndex - 1);
                enableMap();
                setHackerNumMenu(Math.min(baseCountry.getHackerNumber() - 1, 3), false, false);
            } else {
                targetCountry = allCountries.get(countryIndex - 1);
            }
        } else {
            if (click % 2 == 0) {
                baseCountry = allCountries.get(countryIndex - 1);
            } else
                targetCountry = allCountries.get(countryIndex - 1);
            click++;
            if (baseCountry != null)
                setHackerNumMenu(baseCountry.getHackerNumber() - 1, false, false);
            else
                hackerNum_menu.setText("0");
        }
    }

    @FXML
    public void hireClicked() {
        if (baseCountry == null) {
            infoGame_lbl.setText("You must select one of your own countries!");
        } else {
            startTurn(turnType);
            updateScene(baseCountry, null);
            updatePlayerProperties();
            if (players.get(turnOwner - 1).getNumOfBonusHackers() == 0) {
                infoGame_lbl.setText("No hackers left to hire. Go to hack phase!");
                setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false, false);
            }
        }
    }

    private void updateScene(Country baseCountry, Country targetCountry) {
        if (baseCountry != null) {
            int cId = baseCountry.getId();
            if (turnType == 1) {
                for (int i = TOTAL_NUM_OF_COUNTRIES; i < 2 * TOTAL_NUM_OF_COUNTRIES - 1; i++) {
                    Label label = (Label) map_pane.getChildren().get(i);
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId))) {
                        label.setText(baseCountry.getHackerNumber() + "");
                        break;
                    }
                }
            } else {
                int cId2 = targetCountry.getId();
                for (int i = TOTAL_NUM_OF_COUNTRIES; i < 2 * TOTAL_NUM_OF_COUNTRIES - 1; i++) {
                    Label label = (Label) map_pane.getChildren().get(i);
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId))) {
                        label.setText(baseCountry.getHackerNumber() + "");
                    }
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId2))) {
                        label.setText(targetCountry.getHackerNumber() + "");
                    }
                }
            }
        }
    }

    private void updatePlayerProperties() {
        int index = 0;
        for (Player p : players) {
            HBox hBox =
                    (HBox) nicknames_vbox.getChildren().get(nicknames_vbox.getChildren().size() - players.size() + index);
            GridPane gridPane = (GridPane) hBox.getChildren().get(1);
            Label hackerLabel = (Label) gridPane.getChildren().get(2);
            Label countryLabel = (Label) gridPane.getChildren().get(3);
            hackerLabel.setText(p.getNumOfHackers() + "");
            countryLabel.setText(p.getCountries().size() + "");
            index++;
        }
    }

    @FXML
    public void hackClicked() {
        if (hack_btn.getText().equals("HACK")) {
            if (baseCountry == null || targetCountry == null) {
                infoGame_lbl.setText("Choose countries for hack!");
            } else if (baseCountry.getHackerNumber() <= 1) {
                infoGame_lbl.setText("Choose a country with more hackers!");
            } else {
                disableMap();
                startTurn(turnType);
            }
        } else if (hack_btn.getText().equals("MOVE")) {
            baseCountry.setHackerNumber(baseCountry.getHackerNumber() - Integer.parseInt(hackerNum_menu.getText()));
            targetCountry.setHackerNumber(targetCountry.getHackerNumber() + Integer.parseInt(hackerNum_menu.getText()));
            hack_btn.setText("HACK");
            nextPhase_btn.setDisable(false);
            setHackerNumMenu(0, true, true);
            enableMap();
            updateScene(baseCountry, targetCountry);
            baseCountry = null;
            targetCountry = null;
        }
    }

    @FXML
    public void fortifyClicked() {
        if (baseCountry == null)
            infoGame_lbl.setText("You must select a country to send hackers!");
        else if (targetCountry == null)
            infoGame_lbl.setText("You must select a country to get hackers!");
        else if (baseCountry == targetCountry)
            infoGame_lbl.setText("Base country and target country can't be same. Please select new ones!");
        else if (baseCountry.getHackerNumber() <= 1) {
            infoGame_lbl.setText("There should be at least 1 hacker in base country!");
        } else {
            startTurn(turnType);
            updateScene(baseCountry, targetCountry);
        }
    }

    public void showDice(Hack hack){
        for(Node n: dice_pane.getChildren()){
            ImageView imageView = (ImageView) n;
            imageView.setImage(null);
        }
        for(int i = 0; i < hack.attack.roll.attackersScores.size(); i++)
        {
            ImageView image = (ImageView) dice_pane.getChildren().get(i);
            image.setImage(new Image("/Pictures/Dice-" + hack.attack.roll.attackersScores.get(i) + ".png"));
            System.out.println("attacker dice " + hack.attack.roll.attackersScores.get(i));
        }
        for(int i = 0; i < hack.attack.roll.defendersScores.size(); i++)
        {
            ImageView image = (ImageView) dice_pane.getChildren().get(i + 3);
            image.setImage(new Image("/Pictures/Dice-" + hack.attack.roll.defendersScores.get(i) + ".png"));
            System.out.println("defender dice " + hack.attack.roll.defendersScores.get(i));
        }
    }

    @FXML
    public void howToPlayClicked() {
        howToPlay_pane.setVisible(!howToPlay_pane.isVisible());
    }

    @FXML
    public void settingsClicked() {
        settings_pane.setVisible(!settings_pane.isVisible());
    }

    @FXML
    public void cardsClicked() {
        cards_pane.setVisible(!cards_pane.isVisible());
    }

    private void updateCardsScene() {
        for (Node n : cards_hbox.getChildren()) {
            ImageView imageView = (ImageView) ((Pane) n).getChildren().get(0);
            imageView.setImage(null);
        }
        Player currentPlayer = players.get(turnOwner - 1);
        for (int i = 0; i < currentPlayer.getCards().size(); i++) {
            Pane pane = (Pane) cards_hbox.getChildren().get(i);
            ImageView imageView = (ImageView) pane.getChildren().get(0);
            PointStrategy strategy = currentPlayer.getCards().get(i).getPointStrategy();
            if (strategy.getClass() == LamerPoint.class)
                imageView.setImage(new Image("/Pictures/lCard.jpg"));
            else if (strategy.getClass() == WhitePoint.class)
                imageView.setImage(new Image("/Pictures/wCard.jpg"));
            else if (strategy.getClass() == GrayPoint.class)
                imageView.setImage(new Image("/Pictures/gCard.jpg"));
            else if (strategy.getClass() == BlackPoint.class)
                imageView.setImage(new Image("/Pictures/bCard.jpg"));
        }
        chosenCard1.setImage(null);
        chosenCard2.setImage(null);
        chosenCard3.setImage(null);
    }

    public void endGame() {
        infoGame_lbl.setText("WINNER IS: " + players.get(0).getNickname() + " !!!!");
    }

    @FXML
    public void selectedClicked(MouseEvent e) {
        ImageView imageView = (ImageView) e.getSource();
        if (imageView.getImage() != null)
            imageView.setImage(null);
    }

    @SuppressWarnings("deprecation")
    @FXML
    public void cardSelected(MouseEvent e) {
        ImageView clicked = (ImageView) e.getSource();
        put = false;
        Image lCard = new Image("/Pictures/lCard.jpg");
        Image wCard = new Image("/Pictures/wCard.jpg");
        Image gCard = new Image("/Pictures/gCard.jpg");
        Image bCard = new Image("/Pictures/bCard.jpg");
        if (chosenCard1.getImage() == null) {
            chosenCard1.setImage(clicked.getImage());
            put = true;
        } else if (chosenCard2.getImage() == null && !put) {
            if (chosenCard1.getImage().impl_getUrl().equals(clicked.getImage().impl_getUrl()) || chosenCard1.getImage().impl_getUrl().equals(lCard.impl_getUrl()) ||
                    (chosenCard1.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && !clicked.getImage().impl_getUrl().equals(lCard.impl_getUrl())) ||
                    (chosenCard1.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && clicked.getImage().impl_getUrl().equals(bCard.impl_getUrl())))
                chosenCard2.setImage(clicked.getImage());
            else {
                chosenCard2.setImage(chosenCard1.getImage());
                chosenCard1.setImage(clicked.getImage());
            }
            put = true;
        } else if (chosenCard3.getImage() == null && !put) {
            if (clicked.getImage().impl_getUrl().equals(bCard.impl_getUrl()) ||
                    (clicked.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && !chosenCard2.getImage().impl_getUrl().equals(bCard.impl_getUrl())) ||
                    (clicked.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && (chosenCard2.getImage().impl_getUrl().equals(wCard.impl_getUrl()) || chosenCard2.getImage().impl_getUrl().equals(lCard.impl_getUrl()))))
                chosenCard3.setImage(clicked.getImage());
            else if ((clicked.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && !(chosenCard1.getImage().impl_getUrl().equals(gCard.impl_getUrl()) || chosenCard1.getImage().impl_getUrl().equals(bCard.impl_getUrl())) ||
                    (clicked.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && !chosenCard1.getImage().impl_getUrl().equals(bCard.impl_getUrl())))) {
                chosenCard3.setImage(chosenCard2.getImage());
                chosenCard2.setImage(clicked.getImage());
            } else {
                chosenCard3.setImage(chosenCard2.getImage());
                chosenCard2.setImage(chosenCard1.getImage());
                chosenCard1.setImage(clicked.getImage());
            }
            put = true;
        }
    }

    @FXML
    public void combineCardsClicked() {
        Image lCard = new Image("/Pictures/lCard.jpg");
        Image wCard = new Image("/Pictures/wCard.jpg");
        Image gCard = new Image("/Pictures/gCard.jpg");
        Image bCard = new Image("/Pictures/bCard.jpg");
        System.out.println("BEFORE: " + players.get(turnOwner - 1).getNumOfBonusHackers());
        if (chosenCard1.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(bCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 4);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(lCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 4);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(bCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 6);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(wCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 6);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(bCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 8);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(gCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 8);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(bCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 10);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(bCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 10);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(gCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(bCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 10);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else if (chosenCard1.getImage().impl_getUrl().equals(lCard.impl_getUrl()) && chosenCard2.getImage().impl_getUrl().equals(wCard.impl_getUrl()) && chosenCard3.getImage().impl_getUrl().equals(gCard.impl_getUrl())) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 10);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "LamerPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "WhitePoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "GrayPoint"));
        } else if (chosenCard1.getImage() == bCard && chosenCard2.getImage() == bCard && chosenCard3.getImage() == bCard) {
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 12);
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
            players.get(turnOwner - 1).getCards().removeIf(c -> c.getPointStrategy().getClass().toString().equals(
                    "BlackPoint"));
        } else {
            info_lbl.setText("Please enter cards according to the guide.");
            return;
        }
        updateCardsScene();
        info_lbl.setText("Bonus hackers successfully added! Returning to the game");
        setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false, false);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cards_pane.setVisible(false);
        System.out.println("LATER: " + players.get(turnOwner - 1).getNumOfBonusHackers());
    }
}