package src.strategies.allocation;

import src.models.vehicles.Vehicle;
import src.models.vehicles.ElectricCar;
import src.models.vehicles.ElectricBike;
import src.models.vehicles.Truck;
import src.models.parking.ParkingSpot;
import src.models.gates.EntryExitGate;
import src.enums.SpotType;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class PreferredSpotTypeStrategy implements SlotAllocationStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(Vehicle vehicle, List<ParkingSpot> availableSpots, EntryExitGate entryGate) {
        return availableSpots.stream()
                .filter(spot -> spot.canAccommodate(vehicle))
                .sorted(Comparator
                        .comparing((ParkingSpot spot) -> getPreferenceScore(vehicle, spot))
                        .thenComparingInt(spot -> Math.abs(spot.getFloor() - entryGate.getFloor()))
                        .thenComparing(ParkingSpot::getSpotId))
                .findFirst();
    }

    private int getPreferenceScore(Vehicle vehicle, ParkingSpot spot) {
        SpotType spotType = spot.getSpotType();

        if (vehicle instanceof ElectricCar && ((ElectricCar) vehicle).needsCharging()) {
            return spotType.hasCharging() ? 1 : 5;
        }

        if (vehicle instanceof ElectricBike && ((ElectricBike) vehicle).needsCharging()) {
            return spotType.hasCharging() ? 1 : 5;
        }

        if (vehicle instanceof Truck) {
            return spotType.isLarge() ? 1 : 10;
        }

        if (vehicle.getVehicleType().isElectric() && spotType.hasCharging()) {
            return 2;
        }

        return spotType == SpotType.STANDARD ? 1 : 3;
    }

    @Override
    public String getStrategyName() {
        return "Preferred Spot Type Strategy";
    }
}
