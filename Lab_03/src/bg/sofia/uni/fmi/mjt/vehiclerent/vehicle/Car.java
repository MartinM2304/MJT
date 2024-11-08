package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;




public final class Car extends Vehicle{

    private final int priceForSeat=5;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;
    private int numberOfSeats;
    private  FuelType fuelType;

    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id,model);
        this.fuelType=fuelType;
        this.pricePerWeek=pricePerWeek;
        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;
        this.numberOfSeats=numberOfSeats;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException{
        double result=numberOfSeats*priceForSeat;
        Duration duration=Duration.between(startOfRent,endOfRent);


        long days=duration.toDays();
        //System.out.println(days);
        long weeks= days/7;
        //System.out.println(weeks);

        duration=duration.minusDays(days);
        result+=weeks*pricePerWeek;
        result+=days*fuelType.getPricePerType();

        days-=weeks*7;
        result+=days*pricePerDay;

        //System.out.println(duration.toHours());
        result+= duration.toHours();

        result+=super.driver.age().getTaxDriver();

        return result;
    }
}
