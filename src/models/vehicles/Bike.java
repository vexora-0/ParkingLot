package src.models.vehicles;

import src.enums.VehicleType;
import src.enums.SpotType;

public class Bike extends Vehicle {

    public Bike(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.BIKE, ownerName);
    }

    @Override
    public boolean canFitInSpot(SpotType spotType) {
        return true;
    }
}
