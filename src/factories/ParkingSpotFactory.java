package src.factories;

import src.models.parking.ParkingSpot;
import src.enums.SpotType;
import java.util.ArrayList;
import java.util.List;

public class ParkingSpotFactory {
    private static final double BASE_PRICE = 2.0;

    public static ParkingSpot createSpot(String spotId, int floor, SpotType spotType) {
        return new ParkingSpot(spotId, floor, spotType, BASE_PRICE);
    }

    public static List<ParkingSpot> createSpotsForFloor(int floor, int standardSpots,
            int chargingSpots, int largeSpots, int chargingLargeSpots) {
        List<ParkingSpot> spots = new ArrayList<>();

        for (int i = 1; i <= standardSpots; i++) {
            spots.add(createSpot(String.format("F%d-S%03d", floor, i), floor, SpotType.STANDARD));
        }

        for (int i = 1; i <= chargingSpots; i++) {
            spots.add(createSpot(String.format("F%d-C%03d", floor, i), floor, SpotType.CHARGING));
        }

        for (int i = 1; i <= largeSpots; i++) {
            spots.add(createSpot(String.format("F%d-L%03d", floor, i), floor, SpotType.LARGE));
        }

        for (int i = 1; i <= chargingLargeSpots; i++) {
            spots.add(createSpot(String.format("F%d-CL%02d", floor, i), floor, SpotType.CHARGING_LARGE));
        }

        return spots;
    }
}
