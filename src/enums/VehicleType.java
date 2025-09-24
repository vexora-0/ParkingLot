package src.enums;

public enum VehicleType {
    CAR(1, "Car"),
    BIKE(2, "Bike"),
    ELECTRIC_BIKE(3, "Electric Bike"),
    TRUCK(4, "Truck"),
    ELECTRIC_CAR(5, "Electric Car");

    private final int code;
    private final String displayName;

    VehicleType(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isElectric() {
        return this == ELECTRIC_BIKE || this == ELECTRIC_CAR;
    }
}
