package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;

public abstract sealed class Vehicle permits Bicycle,Car,Caravan{

    private String id;
    private String model;

    protected Driver driver; // intelij cant resolve
    private boolean taken;

    LocalDateTime startRentTime;

    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
    }

    public LocalDateTime getStartRentTime(){
        return startRentTime;
    }
    /**
     * Simulates rental of the vehicle. The vehicle now is considered rented by the provided driver and the start of the rental is the provided date.
     * @param driver the driver that wants to rent the vehicle.
     * @param startRentTime the start time of the rent
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver.
     */
    public void rent(Driver driver, LocalDateTime startRentTime) {

        if(taken){
            throw new VehicleAlreadyRentedException("Vehicle is already rented");
        }

        taken=true;
        this.driver=driver;
        this.startRentTime=startRentTime;

    }

    /**
     * Simulates end of rental for the vehicle - it is no longer rented by a driver.
     * @param rentalEnd time of end of rental
     * @throws IllegalArgumentException in case @rentalEnd is null
     * @throws VehicleNotRentedException in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     * in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     * and the driver tries to return them after an hour.
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {

        if(!taken){
            throw  new VehicleNotRentedException("Vehicle is not rented");
        }

        //System.out.println(startRentTime);
        //System.out.println(startRentTime.compareTo(rentalEnd));
        if(rentalEnd.compareTo(startRentTime)<=0){
            throw new InvalidRentingPeriodException("return time is not valid");
        }
        taken=false;

    }

    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or
     * the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

}
