package src;

import src.services.ParkingLotService;
import src.models.vehicles.Vehicle;
import src.models.parking.Ticket;
import src.models.parking.ParkingSpot;
import src.models.gates.EntryExitGate;
import src.factories.*;
import src.strategies.pricing.HourlyPricingStrategy;
import src.strategies.allocation.PreferredSpotTypeStrategy;
import src.enums.VehicleType;
import src.enums.SpotType;

import java.util.List;
import java.util.Optional;

public class ParkingLotDemo {

    public static void main(String[] args) {
        System.out.println("=== Parking Lot Demo ===\n");

        ParkingLotService parkingLot = setupParkingLot();
        runDemo(parkingLot);
    }

    private static ParkingLotService setupParkingLot() {
        ParkingLotService parkingLot = ParkingLotService.getInstance("City Mall Parking");

        parkingLot.setPricingStrategy(new HourlyPricingStrategy());
        parkingLot.setAllocationStrategy(new PreferredSpotTypeStrategy());

        setupParkingSpots(parkingLot);
        setupGates(parkingLot);

        return parkingLot;
    }

    private static void setupParkingSpots(ParkingLotService parkingLot) {
        List<ParkingSpot> floorSpots = ParkingSpotFactory.createSpotsForFloor(1, 10, 3, 2, 1);
        parkingLot.addParkingSpots(floorSpots);
        System.out.println("✓ Created parking spots");
    }

    private static void setupGates(ParkingLotService parkingLot) {
        List<EntryExitGate> gates = GateFactory.createDefaultGates();
        parkingLot.addGates(gates);
        System.out.println("✓ Setup gates");
    }

    private static void runDemo(ParkingLotService parkingLot) {
        System.out.println("\n--- Parking Vehicles ---");

        Vehicle[] vehicles = {
                VehicleFactory.createVehicle(VehicleType.CAR, "MH01AB1234", "Rajesh Kumar"),
                VehicleFactory.createVehicle(VehicleType.ELECTRIC_CAR, "DL02CD5678", "Priya Sharma", true),
                VehicleFactory.createVehicle(VehicleType.BIKE, "UP03EF9012", "Amit Singh")
        };

        for (Vehicle vehicle : vehicles) {
            Optional<Ticket> ticketOpt = parkingLot.parkVehicle(vehicle, "G01");
            if (ticketOpt.isPresent()) {
                Ticket ticket = ticketOpt.get();
                System.out.printf("✓ %s parked at %s\n",
                        vehicle.getOwnerName(), ticket.getAssignedSpot().getSpotId());
            } else {
                System.out.printf("✗ Failed to park %s's vehicle\n", vehicle.getOwnerName());
            }
        }

        System.out.println("\n--- Status ---");
        System.out.println(parkingLot.getStatus());

        System.out.println("\n✓ Demo completed!");
    }
}
