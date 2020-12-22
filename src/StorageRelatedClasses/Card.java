package StorageRelatedClasses;

public class Card {
    Player owner;
    int type;

    public Card(Player owner){
        this.owner = owner;
        type = generateRandomCard();
    }

    public int generateRandomCard()
    {
        return (int) ((4.0 * Math.random() + 1));
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
