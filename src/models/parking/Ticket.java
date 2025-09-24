package src.models.parking;

import src.models.vehicles.Vehicle;
import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot assignedSpot;
    private final String entryGate;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double totalAmount;
    private boolean isPaid;

    public Ticket(Vehicle vehicle, ParkingSpot assignedSpot, String entryGate) {
        this.ticketId = UUID.randomUUID().toString().substring(0, 8);
        this.vehicle = vehicle;
        this.assignedSpot = assignedSpot;
        this.entryGate = entryGate;
        this.entryTime = LocalDateTime.now();
        this.isPaid = false;
        this.totalAmount = 0.0;
    }

    public void completeExit(double amount) {
        this.exitTime = LocalDateTime.now();
        this.totalAmount = amount;
        this.isPaid = true;
    }

    public long getParkingDurationMinutes() {
        LocalDateTime endTime = exitTime != null ? exitTime : LocalDateTime.now();
        return java.time.Duration.between(entryTime, endTime).toMinutes();
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getAssignedSpot() {
        return assignedSpot;
    }

    public String getEntryGate() {
        return entryGate;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public String toString() {
        return String.format("Ticket[%s] Vehicle:%s Spot:%s Entry:%s Duration:%dmin Amount:$%.2f",
                ticketId, vehicle.getLicensePlate(), assignedSpot.getSpotId(),
                entryGate, getParkingDurationMinutes(), totalAmount);
    }
}
