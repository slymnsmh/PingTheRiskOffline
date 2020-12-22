package StorageRelatedClasses;

interface PointStrategy {
    public int givePoint();
}

class LamerPoint implements PointStrategy{
    public int givePoint(){
        return 1;
    }
}

class BlackPoint implements PointStrategy{
    public int givePoint(){
        return 3;
    }
}

class GrayPoint implements PointStrategy{
    public int givePoint(){
        return 2;
    }
}

class JokerPoint implements PointStrategy{
    public int givePoint(){
        return 4;
    }
}