package Managers;

import StorageRelatedClasses.Country;
import StorageRelatedClasses.Database;
import StorageRelatedClasses.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class GameManager implements Initializable
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
    int playerNumber;
    private final int TOTAL_NUM_OF_COUNTRIES = 36;
    private final int TOTAL_NUM_OF_HACKERS = 120;
    String[] allColors = {"BLUE", "RED", "GREEN", "ORANGE"};
    int hackerNumBeginning;
    int countryNumBeginning;
    ArrayList<Integer> givenCountries = new ArrayList<>();
    ArrayList<Country> allCountries = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerNumber = players.size();
        hackerNumBeginning = TOTAL_NUM_OF_HACKERS / playerNumber;
        countryNumBeginning = TOTAL_NUM_OF_COUNTRIES / playerNumber;
        startGame();
    }

    public void getCountriesFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/StorageRelatedClasses/Countries.txt"));
        String line = reader.readLine();
        while (line != null)
        {
            System.out.println("Line: " + line);
            line = line.substring(1);
            String countryId = line.substring(0, line.indexOf("|"));  //1
            line = line.substring(line.indexOf("|") + 1);
            String countryName = line.substring(0, line.indexOf("|")); //1
            line = line.substring(line.indexOf("|") + 1);
            String location = line;
            Country country = new Country(Integer.parseInt(countryId), countryName, location);
            allCountries.add(country);
        }
    }

    public void startGame() {
        assignColorsToPlayers();
        assignHackerNumsToPlayers();
        assignCountriesToPlayers();
        assignHackerNumsToCountries();
        int counter = 0;
        String nicknames = "";
        for (Player p : ServerController.players)
        {
            if (lobby.getPlayerIds().contains(String.valueOf(p.getId()))) {
                nicknames += p.getNickname() + ",";
                counter++;
                System.out.println("PLAYER: " + counter);
                System.out.println("ID: " + p.getId());
                System.out.println("NICK: " + p.getNickname());
                System.out.println("COLOR: " + p.getColor());
                for (int i = 0; i < p.getCountries().size(); i++) {
                    System.out.println("COUNTRY " + i + ": " + p.getCountries().get(i) + " -> hacker num: " + allCountries.get(p.getCountries().get(i) - 1).hackerNumber);
                }
                System.out.println("NUM OF COUNTRIES: " + p.getNumOfCountries());
                System.out.println("NUM OF HACKERS: " + p.getNumOfHackers());
            }
        }
        nicknames = nicknames.substring(0, nicknames.length() - 1);
        out.writeUTF(String.valueOf(lobby.getNumOfPlayers()));
        out.writeUTF(lobby.getPlayerIds());
        //send colors !!!!!!!!
        out.writeUTF(nicknames);
        out.writeUTF(String.valueOf(TOTAL_NUM_OF_COUNTRIES));
        for (int i = 0; i < TOTAL_NUM_OF_COUNTRIES; i++) {
            out.writeUTF(String.valueOf(allCountries.get(i).owner.getId()));
        }
        for (int i = 0; i < TOTAL_NUM_OF_COUNTRIES; i++) {
            out.writeUTF(String.valueOf(allCountries.get(i).hackerNumber));
        }
    }

    public void assignColorsToPlayers()
    {
        int i = 0;
        for (Player p : players)
        {
            p.setColor(allColors[i]);
            i++;
        }
    }

    public void assignHackerNumsToPlayers() {
        for (Player p : players) {
            p.setNumOfHackers(hackerNumBeginning);
            System.out.println(p.getNickname() + " - Hacker updated!");
        }
    }

    public void assignCountriesToPlayers() {
        for (Player p : players) {
            ArrayList<Country> countries = new ArrayList<>();
            ArrayList<Integer> countryNumbers = generateRandomCountryNumbers(p);
            for (int i : countryNumbers)
            {
                countries.add(allCountries.get(i - 1));
            }
            p.setCountries(countries);
            p.setNumOfCountries(countryNumBeginning);
        }
    }

    public ArrayList<Integer> generateRandomCountryNumbers(Player player)
    {
        int[] countryNumbers = new int[countryNumBeginning];
        int counter = 0;
        while ( counter != countryNumbers.length )
        {
            int randomNumber = new Random().nextInt(TOTAL_NUM_OF_COUNTRIES) + 1;
            boolean isThere = false;
            for ( int i = 0; i < countryNumbers.length; i++ )
            {
                if ( countryNumbers[i] == randomNumber )
                    isThere = true;
            }
            for (int i = 0; i < givenCountries.size(); i++)
            {
                if (givenCountries.get(i) == randomNumber)
                    isThere = true;
            }
            if ( !isThere )
            {
                countryNumbers[counter] = randomNumber;
                givenCountries.add(randomNumber);
                counter++;
            }
        }
        ArrayList<Integer> countryNums = new ArrayList<>();
        for (int a : countryNumbers)
        {
            countryNums.add(a);
        }
        return countryNums;
    }

    public void assignHackerNumsToCountries() {
        int minHackerForACountry = hackerNumBeginning / countryNumBeginning;
        System.out.println("MIN HACKER FOR A COUNTRY: " + minHackerForACountry);
        String checker = "";
        for (Country country : allCountries)
        {
            if (checker.equals("..")) {
                country.getHackerNumber() = minHackerForACountry + 1;
                System.out.println(country.id + " -> hacker num ===> " + country.hackerNumber);
                checker = "";
            }
            else if (checker.equals("") || checker.equals(".")) {
                country.hackerNumber = minHackerForACountry;
                System.out.println(country.id + " -> hacker num ===> " + country.hackerNumber);
                checker += ".";
            }
        }
        for (int i = 0; i < playerIds.size(); i++) {
            for (Player p : ServerController.players) {
                if (p.getId() == Integer.parseInt(playerIds.get(i))) {
                    for (Integer countryId : p.getCountries())
                    {
                        for (Country country : allCountries)
                        {
                            if (countryId == country.id)
                            {
                                country.owner = p;
                                break;
                            }
                        }
                    }
                }
            }
        }
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

    public void startTurn(int turnOwner) {
        this.turnOwner = turnOwner;
        TurnManager turnManager = new TurnManager(turnOwner, getPlayerSockets());
    }







    public void endGame()
    {
        if ( checkIfFinished() )
        {
            //Finish the game
            uploadDatabase();
        }
        else
        {
            //Start next turn
        }

    }



    public void uploadDatabase()
    {
        //When the game finished, upload all game info to database
    }

    /*@FXML
    public void howToPlayClicked()
    {}

    @FXML
    public void settingsClicked()
    {}*/

    public void assignColorsToCountries()
    {}

    public boolean checkIfFinished()
    {
        return false;
    }

    public void setTurnOwner(int turnOwner)
    {
        this.turnOwner = turnOwner;
    }

    //Oyunculara renk ata
    //Oyunculara hacker sayısı ata
    //Ülkeleri oyunculara ata
    //Ülkelere renk ata
    //Ülkere hacker sayısı ata
    //Eğer tüm ülkeler tek bir kişinin değilse
    //Turn sahibini belirle
    //Turn başlat
    //Eğer tüm ülkeler tek bir kişininse
    //Oyun bilgilerini (kazanan, puanlar vs) güncelle (database)
    //Oyunu bitir ve oyuncuları lobby sayfasına döndür
}