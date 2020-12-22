package Managers;

import StorageRelatedClasses.*;

public class Hire
{
    int numOfHackers;
    Country country;
    int counter = 30;
    Player turnOwner;

    public Hire(Player turnOwner, Country country, int numOfHackers){
        this.country = country;
        this.turnOwner = turnOwner;
        this.numOfHackers = numOfHackers;
        addNumOfHackers();
        hireHackers();
    }

    /*public void countDown(){
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println(counter--);
                if (counter <= 0) {
                    timer.cancel();
                    endPart();
                }
            }
        }, 0, 1000);
    }*/

    public void setPartNameLabelText(String text){

    }

    public void endPart(){
        System.out.println("Go Hack");
    }

    void addNumOfHackers()
    {
        if ( turnOwner.getNumOfBonusHackers() != 0)
        {
            turnOwner.setNumOfHackers(turnOwner.getNumOfHackers() + numOfHackers);
            turnOwner.setNumOfBonusHackers(turnOwner.getNumOfBonusHackers() - numOfHackers);
        }

//        numOfHackers = numOfHackers + 3;
//        if(p.getNumOfBonusCards().isEmpty() != false) {
//            int hackerCount = 0;
//            for (Card i : p.numOfBonusCards) {
//                hackerCount = i.id;
//                p.numOfBonusHackers = p.numOfBonusHackers + hackerCount;
//            }
//        }
    }

    void hireHackers(){
        country.setHackerNumber(country.getHackerNumber() + numOfHackers);
    }

    public int chooseHackerNum(){
        return numOfHackers;
    }
}