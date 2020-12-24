package StorageRelatedClasses;

import java.sql.SQLException;
import java.util.ArrayList;

public class Player {
    ArrayList<Card> cards = new ArrayList<>();
    private String nickname;
    private String color;
    private int numOfHackers;
    private int numOfCountries;
    private ArrayList<Country> countries;
    private int numOfWins;
    private int numOfBonusCards;
    private int numOfBonusHackers;

    public Player(String nickname) throws SQLException {
        this.nickname = nickname;
        this.color = "";
        this.numOfHackers = 0;
        this.numOfCountries = 0;
        this.numOfWins = 0;
        this.numOfBonusCards = 0;
        this.numOfBonusHackers = 0;
        this.countries = null;
    }

    public Player(int id, String ip, String port, int gameId, String nickname, String color, int numOfHackers,
                  int numOfCountries, ArrayList<Country> countries, int numOfWins, int numOfLosses,
                  int numOfBonusCards, int numOfBonusHackers, boolean isOnline) {
        this.nickname = nickname;
        this.color = color;
        this.numOfHackers = numOfHackers;
        this.numOfCountries = numOfCountries;
        this.countries = countries;
        this.numOfWins = numOfWins;
        this.numOfBonusCards = numOfBonusCards;
        this.numOfBonusHackers = numOfBonusHackers;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumOfHackers() {
        return numOfHackers;
    }

    public void setNumOfHackers(int numOfHackers) {
        this.numOfHackers = numOfHackers;
    }

    public int getNumOfCountries() {
        return numOfCountries;
    }

    public void setNumOfCountries(int numOfRegions) {
        this.numOfCountries = numOfRegions;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public int getNumOfWins() {
        return numOfWins;
    }

    public void setNumOfWins(int numOfWins) {
        this.numOfWins = numOfWins;
    }

    public int getNumOfBonusCards() {
        return numOfBonusCards;
    }

    public void setNumOfBonusCards(int numOfBonusCards) {
        this.numOfBonusCards = numOfBonusCards;
    }

    public int getNumOfBonusHackers() {
        return numOfBonusHackers;
    }

    public void setNumOfBonusHackers(int numOfBonusHackers) {
        this.numOfBonusHackers = numOfBonusHackers;
    }
}
