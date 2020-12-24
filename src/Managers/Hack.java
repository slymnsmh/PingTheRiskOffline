package Managers;

import StorageRelatedClasses.Card;
import StorageRelatedClasses.Country;

import java.util.ArrayList;

public class Hack {

    static int distance;
    Country baseCountry;
    Country targetCountry;
    int numOfAttacker;

    public Hack(Country baseCountry, Country targetCountry, int numOfAttacker) {
        this.baseCountry = baseCountry;
        this.targetCountry = targetCountry;
        this.numOfAttacker = numOfAttacker;
        calculateDistance();
        startAttack();
    }

    void calculateDistance() {
        int temp1 =
                Integer.parseInt(baseCountry.getLocation().substring(0, 1)) - Integer.parseInt(targetCountry.getLocation().substring(0, 1));
        int temp2 =
                Integer.parseInt(baseCountry.getLocation().substring(2, 3)) - Integer.parseInt(targetCountry.getLocation().substring(2, 3));
        distance = (int) Math.sqrt(Math.pow(temp1, 2) + Math.pow(temp2, 2));
    }

    void startAttack() {
        Attack attack = new Attack(baseCountry.getOwner(), targetCountry.getOwner(), baseCountry, targetCountry,
                numOfAttacker);
        if (targetCountry.getHackerNumber() == 0) {
            targetCountry.getOwner().getCountries().remove(targetCountry);
            baseCountry.getOwner().getCountries().add(targetCountry);
            targetCountry.setOwner(baseCountry.getOwner());
            targetCountry.setHackerNumber(1);
            baseCountry.setHackerNumber(baseCountry.getHackerNumber() - 1);
            baseCountry.getOwner().setNumOfWins(baseCountry.getOwner().getNumOfWins() + 1);
            giveCard();
        }
    }

    void giveCard() {
        ArrayList<Card> cards = baseCountry.getOwner().getCards();
        cards.add(new Card(baseCountry.getOwner()));
        baseCountry.getOwner().setCards(cards);
    }

}
