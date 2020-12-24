package Managers;

import StorageRelatedClasses.Country;
import StorageRelatedClasses.Player;

public class Attack extends PingManager {
    Player attacker;
    Player defender;
    Country attackerCountry;
    Country defenderCountry;
    Player winner;
    Player loser;
    int numOfDefenderHackers;
    PingManager pingManager;
    DiceManager roll;

    Attack(Player attacker, Player defender, Country attackersCountry, Country defendersCountry,
           int numOfAttackerHackers) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackerCountry = attackersCountry;
        this.defenderCountry = defendersCountry;
        pingManager = new PingManager();
        pingManager.setPing();
        numOfDefenderHackers = defendersCountry.getHackerNumber();

        roll = new DiceManager(Math.min(numOfAttackerHackers, 3), Math.min(numOfAttackerHackers, 2),
                pingManager.pingLevel);
        roll.compareBiggest(numOfAttackerHackers, numOfDefenderHackers);

        attackersCountry.setHackerNumber(numOfAttackerHackers - roll.defenderWins);
        defendersCountry.setHackerNumber(numOfDefenderHackers - roll.attackerWins);
    }
}

