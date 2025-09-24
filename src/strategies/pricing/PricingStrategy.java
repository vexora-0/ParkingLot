package src.strategies.pricing;

import src.models.parking.Ticket;

public interface PricingStrategy {
    double calculatePrice(Ticket ticket);

    String getStrategyName();
}
