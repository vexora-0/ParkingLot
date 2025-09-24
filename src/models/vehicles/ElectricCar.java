package src.models.vehicles;

import src.enums.VehicleType;
import src.enums.SpotType;

public class ElectricCar extends Vehicle {
    private boolean needsCharging;

    public ElectricCar(String licensePlate, String ownerName, boolean needsCharging) {
        super(licensePlate, VehicleType.ELECTRIC_CAR, ownerName);
        this.needsCharging = needsCharging;
    }

    public boolean needsCharging() {
        return needsCharging;
    }

    @Override
    public boolean canFitInSpot(SpotType spotType) {
        if (needsCharging) {
            return spotType == SpotType.CHARGING || spotType == SpotType.CHARGING_LARGE;
        }
        return spotType == SpotType.STANDARD || spotType == SpotType.CHARGING ||
                spotType == SpotType.LARGE || spotType == SpotType.CHARGING_LARGE;
    }
}
