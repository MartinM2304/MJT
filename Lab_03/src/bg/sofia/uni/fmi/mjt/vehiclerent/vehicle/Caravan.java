package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

import java.time.Duration;
import java.time.LocalDateTime;

public final class Caravan extends Vehicle {

    final int pricePerSeat=5;
    final int pricePerBed=10;
    FuelType fuelType;
    int numberOfSeats;
    int numberOfBeds;
    double pricePerWeek;
    double pricePerDay;
    double pricePerHour;

    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id,model);
        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;
        this.pricePerWeek=pricePerWeek;
        this.numberOfBeds=numberOfBeds;
        this.numberOfSeats=numberOfSeats;
        this.fuelType=fuelType;
    }
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException{


        Duration duration=Duration.between(startOfRent,endOfRent);

        if(duration.toDays()<1){
            throw  new InvalidRentingPeriodException("Renting for less than 24h");
        }

        double result=numberOfBeds*pricePerBed+numberOfSeats*pricePerSeat;
        long days=duration.toDays();
        long weeks=days/7;
        result+= weeks*pricePerWeek;

        duration=duration.minusDays(days);
        days-=weeks*7;
        result+=days*pricePerDay;
        result+=duration.toHours();

        result+=super.driver.age().getTaxDriver();

        return result;
    }
}
