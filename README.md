# Multi-Floor Parking Lot System using Design Patterns

## UML Class Diagram

```
┌─────────────────────┐    ┌──────────────────────┐    ┌─────────────────────┐
│     VehicleType     │    │      SpotType        │    │     SpotStatus      │
│    <<enumeration>>  │    │   <<enumeration>>    │    │   <<enumeration>>   │
├─────────────────────┤    ├──────────────────────┤    ├─────────────────────┤
│ CAR                 │    │ STANDARD             │    │ AVAILABLE           │
│ BIKE                │    │ CHARGING             │    │ OCCUPIED            │
│ ELECTRIC_BIKE       │    │ LARGE                │    │ MAINTENANCE         │
│ TRUCK               │    │ CHARGING_LARGE       │    │ RESERVED            │
│ ELECTRIC_CAR        │    └──────────────────────┘    └─────────────────────┘
└─────────────────────┘

┌─────────────────────────────────────┐
│             Vehicle                 │
│           <<abstract>>              │
├─────────────────────────────────────┤
│ - licensePlate: String              │
│ - vehicleType: VehicleType          │
│ - ownerName: String                 │
├─────────────────────────────────────┤
│ + canFitInSpot(SpotType): boolean   │
│ + getLicensePlate(): String         │
│ + getVehicleType(): VehicleType     │
└─────────────────────────────────────┘
                  ▲
                  │
    ┌─────────────┼─────────────┐
    │             │             │
┌───▼───┐   ┌────▼────┐   ┌────▼──────┐
│  Car  │   │  Bike   │   │   Truck   │
└───────┘   └─────────┘   └───────────┘
    │
    ▼
┌─────────────┐   ┌─────────────────┐
│ElectricCar  │   │ ElectricBike    │
├─────────────┤   ├─────────────────┤
│needsCharging│   │ needsCharging   │
└─────────────┘   └─────────────────┘

┌─────────────────────────────────────┐
│           ParkingSpot               │
├─────────────────────────────────────┤
│ - spotId: String                    │
│ - floor: int                        │
│ - spotType: SpotType                │
│ - status: SpotStatus                │
│ - currentVehicle: Vehicle           │
│ - basePrice: double                 │
├─────────────────────────────────────┤
│ + occupy(Vehicle): void             │
│ + free(): Vehicle                   │
│ + canAccommodate(Vehicle): boolean  │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│             Ticket                  │
├─────────────────────────────────────┤
│ - ticketId: String                  │
│ - vehicle: Vehicle                  │
│ - assignedSpot: ParkingSpot         │
│ - entryGate: String                 │
│ - entryTime: LocalDateTime          │
│ - exitTime: LocalDateTime           │
│ - totalAmount: double               │
│ - isPaid: boolean                   │
├─────────────────────────────────────┤
│ + completeExit(double): void        │
│ + getParkingDurationMinutes(): long │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│         EntryExitGate               │
├─────────────────────────────────────┤
│ - gateId: String                    │
│ - location: String                  │
│ - floor: int                        │
│ - gateType: GateType                │
│ - isOperational: boolean            │
├─────────────────────────────────────┤
│ + canProcessEntry(): boolean        │
│ + canProcessExit(): boolean         │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│        PricingStrategy              │
│        <<interface>>                │
├─────────────────────────────────────┤
│ + calculatePrice(Ticket): double    │
│ + getStrategyName(): String         │
└─────────────────────────────────────┘
                  ▲
                  │
┌─────────────────▼────────────────────┐
│     HourlyPricingStrategy           │
├─────────────────────────────────────┤
│ - BASE_HOURLY_RATE: double          │
│ - CHARGING_RATE_PER_HOUR: double    │
│ - MINIMUM_CHARGE: double            │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│     SlotAllocationStrategy          │
│        <<interface>>                │
├─────────────────────────────────────┤
│ + findSpot(Vehicle,List<>,Gate):    │
│   Optional<ParkingSpot>             │
│ + getStrategyName(): String         │
└─────────────────────────────────────┘
                  ▲
            ┌─────┼─────┐
┌───────────▼──┐      ┌─▼─────────────────────┐
│NearestAvail  │      │PreferredSpotType      │
│ableStrategy  │      │Strategy               │
└──────────────┘      └───────────────────────┘

┌─────────────────────────────────────┐
│       ParkingLotService             │
│       <<singleton>>                 │
├─────────────────────────────────────┤
│ - instance: ParkingLotService       │
│ - allSpots: Map<String,ParkingSpot> │
│ - gates: Map<String,EntryExitGate>  │
│ - activeTickets: Map<String,Ticket> │
│ - pricingStrategy: PricingStrategy  │
│ - allocationStrategy: SlotAllocation│
├─────────────────────────────────────┤
│ + getInstance(): ParkingLotService  │
│ + parkVehicle(Vehicle,String):      │
│   Optional<Ticket>                  │
│ + exitVehicle(String,String):       │
│   Optional<Ticket>                  │
│ + getAvailableSpots(): List<>       │
│ + getStatus(): ParkingLotStatus     │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│        VehicleFactory               │
│        <<factory>>                  │
├─────────────────────────────────────┤
│ + createVehicle(VehicleType,String, │
│   String): Vehicle                  │
│ + createVehicle(VehicleType,String, │
│   String,boolean): Vehicle          │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      ParkingSpotFactory             │
│        <<factory>>                  │
├─────────────────────────────────────┤
│ + createSpot(String,int,SpotType):  │
│   ParkingSpot                       │
│ + createSpotsForFloor(int,int,int,  │
│   int,int): List<ParkingSpot>       │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│         GateFactory                 │
│        <<factory>>                  │
├─────────────────────────────────────┤
│ + createGate(String,String,int,     │
│   GateType): EntryExitGate          │
│ + createDefaultGates(): List<>      │
└─────────────────────────────────────┘
```

## Design Pattern Implementation

The Multi-Floor Parking Lot System demonstrates several key design patterns that work together to create a flexible, maintainable, and extensible parking management solution.

**Key Design Patterns:**

1. **Singleton Pattern** - `ParkingLotService` ensures a single instance manages the entire parking lot state, providing centralized control and thread-safe operations.

2. **Strategy Pattern** - Pluggable pricing (`PricingStrategy`) and slot allocation (`SlotAllocationStrategy`) strategies allow dynamic behavior changes without modifying core logic.

3. **Factory Pattern** - `VehicleFactory`, `ParkingSpotFactory`, and `GateFactory` centralize object creation, ensuring consistent initialization and easy extension for new types.

4. **Template Method Pattern** - Abstract `Vehicle` class defines the structure while concrete implementations provide specific behavior for different vehicle types.

**Pattern Benefits:**
- **Extensibility**: Easy to add new vehicle types, pricing models, or allocation strategies
- **Maintainability**: Clear separation of concerns with each class having a single responsibility
- **Flexibility**: Runtime strategy selection allows dynamic behavior changes
- **Consistency**: Factory pattern ensures uniform object creation across the system


