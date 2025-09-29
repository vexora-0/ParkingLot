package src.services;

import src.models.parking.ParkingSpot;
import src.models.parking.Ticket;
import src.models.vehicles.Vehicle;
import src.models.gates.EntryExitGate;
import src.strategies.pricing.PricingStrategy;
import src.strategies.allocation.SlotAllocationStrategy;
import src.enums.SpotStatus;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotService {
    private static ParkingLotService instance;
    private final String parkingLotName;
    private final Map<String, ParkingSpot> allSpots;
    private final Map<String, EntryExitGate> gates;
    private final Map<String, Ticket> activeTickets;
    private PricingStrategy pricingStrategy;
    private SlotAllocationStrategy allocationStrategy;

    private ParkingLotService(String parkingLotName) {
        this.parkingLotName = parkingLotName;
        this.allSpots = new ConcurrentHashMap<>();
        this.gates = new ConcurrentHashMap<>();
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public static synchronized ParkingLotService getInstance(String parkingLotName) {
        if (instance == null) {
            instance = new ParkingLotService(parkingLotName);
        }
        return instance;
    }

    public static ParkingLotService getInstance() {
        if (instance == null) {
            return getInstance("Multi-Floor Parking Lot");
        }
        return instance;
    }

    public void addParkingSpots(List<ParkingSpot> spots) {
        spots.forEach(spot -> allSpots.put(spot.getSpotId(), spot));
    }

    public void addGates(List<EntryExitGate> gateList) {
        gateList.forEach(gate -> gates.put(gate.getGateId(), gate));
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void setAllocationStrategy(SlotAllocationStrategy allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
    }

    public Optional<Ticket> parkVehicle(Vehicle vehicle, String entryGateId) {
        EntryExitGate gate = gates.get(entryGateId);
        if (gate == null || !gate.canProcessEntry()) {
            return Optional.empty();
        }

        List<ParkingSpot> availableSpots = getAvailableSpots();
        Optional<ParkingSpot> spotOpt = allocationStrategy.findSpot(vehicle, availableSpots, gate);

        if (!spotOpt.isPresent()) {
            return Optional.empty();
        }

        ParkingSpot spot = spotOpt.get();
        spot.occupy(vehicle);

        Ticket ticket = new Ticket(vehicle, spot, entryGateId);
        activeTickets.put(ticket.getTicketId(), ticket);

        return Optional.of(ticket);
    }

    public Optional<Ticket> exitVehicle(String ticketId, String exitGateId) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            return Optional.empty();
        }

        EntryExitGate gate = gates.get(exitGateId);
        if (gate == null || !gate.canProcessExit()) {
            return Optional.empty();
        }

        double amount = pricingStrategy.calculatePrice(ticket);
        ticket.completeExit(amount);

        ticket.getAssignedSpot().free();
        activeTickets.remove(ticketId);

        return Optional.of(ticket);
    }

    public List<ParkingSpot> getAvailableSpots() {
        return allSpots.values().stream()
                .filter(ParkingSpot::isAvailable)
                .collect(ArrayList::new, (list, spot) -> list.add(spot), ArrayList::addAll);
    }

    public List<ParkingSpot> getAvailableSpotsForFloor(int floor) {
        return allSpots.values().stream()
                .filter(spot -> spot.getFloor() == floor && spot.isAvailable())
                .collect(ArrayList::new, (list, spot) -> list.add(spot), ArrayList::addAll);
    }

    public ParkingLotStatus getStatus() {
        Map<Integer, FloorStatus> floorStatusMap = new HashMap<>();

        allSpots.values().forEach(spot -> {
            int floor = spot.getFloor();
            FloorStatus status = floorStatusMap.computeIfAbsent(floor,
                    f -> new FloorStatus(f));
            status.addSpot(spot);
        });

        return new ParkingLotStatus(parkingLotName, floorStatusMap, activeTickets.size());
    }

    public Optional<ParkingSpot> findSpotById(String spotId) {
        return Optional.ofNullable(allSpots.get(spotId));
    }

    public Optional<Ticket> findActiveTicket(String ticketId) {
        return Optional.ofNullable(activeTickets.get(ticketId));
    }

    public List<EntryExitGate> getOperationalGates() {
        return gates.values().stream()
                .filter(EntryExitGate::isOperational)
                .collect(ArrayList::new, (list, gate) -> list.add(gate), ArrayList::addAll);
    }

    public static class ParkingLotStatus {
        private final String name;
        private final Map<Integer, FloorStatus> floors;
        private final int activeVehicles;

        public ParkingLotStatus(String name, Map<Integer, FloorStatus> floors, int activeVehicles) {
            this.name = name;
            this.floors = floors;
            this.activeVehicles = activeVehicles;
        }

        public String getName() {
            return name;
        }

        public Map<Integer, FloorStatus> getFloors() {
            return floors;
        }

        public int getActiveVehicles() {
            return activeVehicles;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("=== %s Status ===\n", name));
            sb.append(String.format("Active Vehicles: %d\n\n", activeVehicles));

            floors.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        sb.append(String.format("Floor %d: %s\n",
                                entry.getKey(), entry.getValue()));
                    });

            return sb.toString();
        }
    }

    public static class FloorStatus {
        private final int floor;
        private int totalSpots;
        private int availableSpots;
        private int occupiedSpots;
        private int maintenanceSpots;

        public FloorStatus(int floor) {
            this.floor = floor;
        }

        public void addSpot(ParkingSpot spot) {
            totalSpots++;
            switch (spot.getStatus()) {
                case AVAILABLE:
                    availableSpots++;
                    break;
                case OCCUPIED:
                    occupiedSpots++;
                    break;
                case MAINTENANCE:
                    maintenanceSpots++;
                    break;
            }
        }

        public int getFloor() {
            return floor;
        }

        public int getTotalSpots() {
            return totalSpots;
        }

        public int getAvailableSpots() {
            return availableSpots;
        }

        public int getOccupiedSpots() {
            return occupiedSpots;
        }

        public int getMaintenanceSpots() {
            return maintenanceSpots;
        }

        @Override
        public String toString() {
            return String.format("Total:%d Available:%d Occupied:%d Maintenance:%d",
                    totalSpots, availableSpots, occupiedSpots, maintenanceSpots);
        }
    }
}

