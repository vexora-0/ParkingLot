package src.strategies.pricing;

import src.models.parking.Ticket;
import src.models.vehicles.Vehicle;

public class HourlyPricingStrategy implements PricingStrategy {
    private static final double BASE_HOURLY_RATE = 2.0;
    private static final double CHARGING_RATE_PER_HOUR = 1.5;
    private static final double MINIMUM_CHARGE = 1.0;

    @Override
    public double calculatePrice(Ticket ticket) {
        long minutes = ticket.getParkingDurationMinutes();
        double hours = Math.max(1, Math.ceil(minutes / 60.0));

        double baseRate = BASE_HOURLY_RATE * hours;
        double spotMultiplier = ticket.getAssignedSpot().getSpotType().getPriceMultiplier();
        double spotPrice = baseRate * spotMultiplier;

        double chargingFee = 0.0;
        Vehicle vehicle = ticket.getVehicle();
        if (vehicle.getVehicleType().isElectric() &&
                ticket.getAssignedSpot().getSpotType().hasCharging()) {
            chargingFee = CHARGING_RATE_PER_HOUR * hours;
        }

        return Math.max(MINIMUM_CHARGE, spotPrice + chargingFee);
    }

    @Override
    public String getStrategyName() {
        return "Hourly Pricing Strategy";
    }
}
