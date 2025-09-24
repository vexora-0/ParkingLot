package src.models.vehicles;

import src.enums.VehicleType;
import src.enums.SpotType;

public class Truck extends Vehicle {

    public Truck(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.TRUCK, ownerName);
    }

    @Override
    public boolean canFitInSpot(SpotType spotType) {
        return spotType == SpotType.LARGE || spotType == SpotType.CHARGING_LARGE;
    }
}
