package Managers;

import StorageRelatedClasses.Country;

public class Fortify {
    Country baseCountry, targetCountry;
    int hackerNum;

    public Fortify(Country baseCountry, Country targetCountry, int hackerNum) {
        this.baseCountry = baseCountry;
        this.targetCountry = targetCountry;
        this.hackerNum = hackerNum;

        fortifyHackers();
    }

    void fortifyHackers() {
        baseCountry.setHackerNumber(baseCountry.getHackerNumber() - hackerNum);
        targetCountry.setHackerNumber(targetCountry.getHackerNumber() + hackerNum);
    }
}