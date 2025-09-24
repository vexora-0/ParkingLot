package src.strategies.allocation;

import src.models.vehicles.Vehicle;
import src.models.parking.ParkingSpot;
import src.models.gates.EntryExitGate;
import java.util.List;
import java.util.Optional;

public interface SlotAllocationStrategy {
    Optional<ParkingSpot> findSpot(Vehicle vehicle, List<ParkingSpot> availableSpots, EntryExitGate entryGate);

    String getStrategyName();
}
