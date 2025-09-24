package src.models.vehicles;

import src.enums.VehicleType;
import src.enums.SpotType;

public class ElectricBike extends Vehicle {
    private boolean needsCharging;

    public ElectricBike(String licensePlate, String ownerName, boolean needsCharging) {
        super(licensePlate, VehicleType.ELECTRIC_BIKE, ownerName);
        this.needsCharging = needsCharging;
    }

    public boolean needsCharging() {
        return needsCharging;
    }

    @Override
    public boolean canFitInSpot(SpotType spotType) {
        if (needsCharging) {
            return spotType.hasCharging();
        }
        return true;
    }
}
