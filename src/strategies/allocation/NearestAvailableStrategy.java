package src.strategies.allocation;

import src.models.vehicles.Vehicle;
import src.models.parking.ParkingSpot;
import src.models.gates.EntryExitGate;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class NearestAvailableStrategy implements SlotAllocationStrategy {

    @Override
    public Optional<ParkingSpot> findSpot(Vehicle vehicle, List<ParkingSpot> availableSpots, EntryExitGate entryGate) {
        return availableSpots.stream()
                .filter(spot -> spot.canAccommodate(vehicle))
                .sorted(Comparator
                        .comparingInt((ParkingSpot spot) -> Math.abs(spot.getFloor() - entryGate.getFloor()))
                        .thenComparing(ParkingSpot::getSpotId))
                .findFirst();
    }

    @Override
    public String getStrategyName() {
        return "Nearest Available Strategy";
    }
}
