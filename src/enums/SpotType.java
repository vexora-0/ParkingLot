package src.enums;

public enum SpotType {
    STANDARD(1.0, "Standard"),
    CHARGING(2.0, "Electric Vehicle Charging"),
    LARGE(1.5, "Large Vehicle"),
    CHARGING_LARGE(2.5, "Large Vehicle with Charging");

    private final double priceMultiplier;
    private final String description;

    SpotType(double priceMultiplier, String description) {
        this.priceMultiplier = priceMultiplier;
        this.description = description;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasCharging() {
        return this == CHARGING || this == CHARGING_LARGE;
    }

    public boolean isLarge() {
        return this == LARGE || this == CHARGING_LARGE;
    }
}
