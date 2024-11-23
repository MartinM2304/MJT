package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup{
    JUNIOR(10),
    EXPERIENCED(0),
    SENIOR(15);
    private final int taxDriver;

    AgeGroup(int taxDriver){this.taxDriver=taxDriver;}

    public int getTaxDriver(){return taxDriver;}

}