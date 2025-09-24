package src.factories;

import src.models.vehicles.*;
import src.enums.VehicleType;

public class VehicleFactory {

    public static Vehicle createVehicle(VehicleType type, String licensePlate, String ownerName) {
        return createVehicle(type, licensePlate, ownerName, false);
    }

    public static Vehicle createVehicle(VehicleType type, String licensePlate, String ownerName,
            boolean needsCharging) {
        switch (type) {
            case CAR:
                return new Car(licensePlate, ownerName);
            case BIKE:
                return new Bike(licensePlate, ownerName);
            case ELECTRIC_BIKE:
                return new ElectricBike(licensePlate, ownerName, needsCharging);
            case TRUCK:
                return new Truck(licensePlate, ownerName);
            case ELECTRIC_CAR:
                return new ElectricCar(licensePlate, ownerName, needsCharging);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type: " + type);
        }
    }
}
