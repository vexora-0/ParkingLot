package src.models.vehicles;

import src.enums.VehicleType;
import src.enums.SpotType;

public class Car extends Vehicle {

    public Car(String licensePlate, String ownerName) {
        super(licensePlate, VehicleType.CAR, ownerName);
    }

    @Override
    public boolean canFitInSpot(SpotType spotType) {
        return spotType == SpotType.STANDARD || spotType == SpotType.CHARGING ||
                spotType == SpotType.LARGE || spotType == SpotType.CHARGING_LARGE;
    }
}
