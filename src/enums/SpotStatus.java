package src.enums;

public enum SpotStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    MAINTENANCE("Under Maintenance"),
    RESERVED("Reserved");

    private final String description;

    SpotStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
