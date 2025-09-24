package src.models.parking;

import src.enums.SpotType;
import src.enums.SpotStatus;
import src.models.vehicles.Vehicle;

public class ParkingSpot {
    private final String spotId;
    private final int floor;
    private final SpotType spotType;
    private SpotStatus status;
    private Vehicle currentVehicle;
    private final double basePrice;

    public ParkingSpot(String spotId, int floor, SpotType spotType, double basePrice) {
        this.spotId = spotId;
        this.floor = floor;
        this.spotType = spotType;
        this.status = SpotStatus.AVAILABLE;
        this.basePrice = basePrice;
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    public boolean canAccommodate(Vehicle vehicle) {
        return isAvailable() && vehicle.canFitInSpot(spotType);
    }

    public void occupy(Vehicle vehicle) {
        if (!canAccommodate(vehicle)) {
            throw new IllegalStateException("Cannot occupy spot: " + spotId);
        }
        this.currentVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
    }

    public Vehicle free() {
        Vehicle vehicle = currentVehicle;
        currentVehicle = null;
        status = SpotStatus.AVAILABLE;
        return vehicle;
    }

    public String getSpotId() {
        return spotId;
    }

    public int getFloor() {
        return floor;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setStatus(SpotStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Spot[%s] Floor:%d Type:%s Status:%s",
                spotId, floor, spotType, status);
    }
}
