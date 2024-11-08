package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

public final class Bicycle extends Vehicle{

    private double pricePerDay;
    private double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour){
        super(id,model);
        this.pricePerDay=pricePerDay;
        this.pricePerHour=pricePerHour;
    }
    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {

        Duration duration=Duration.between(startOfRent,endOfRent);

        if(duration.toDays()>=7){
            throw new InvalidRentingPeriodException("Duration is more than 7 days");
        }

        long days=duration.toDays();
        double price=days*pricePerDay;
        System.out.println(days);

        duration=duration.minusDays(days);
        price+=duration.toHours()*pricePerHour;
        System.out.println(price);

        return  price;
    }

}
