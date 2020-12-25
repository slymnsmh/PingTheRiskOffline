package StorageRelatedClasses;

public class Card {
    Player owner;
    PointStrategy pointStrategy;

    public Card(Player owner){
        this.owner = owner;
        this.pointStrategy = generateRandomPointStrategy();
    }

    public PointStrategy generateRandomPointStrategy()
    {
        int randomNumber = (int) ((4.0 * Math.random() + 1));
            if (randomNumber == 1)
                return new LamerPoint();
            else if (randomNumber == 2)
                return new WhitePoint();
            else if (randomNumber == 3)
                return new GrayPoint();
            else
                return new BlackPoint();
    }

    public PointStrategy getPointStrategy() {
        return pointStrategy;
    }

    public void setPointStrategy(PointStrategy pointStrategy) {
        this.pointStrategy = pointStrategy;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
