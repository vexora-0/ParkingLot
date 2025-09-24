# Multi-Floor Parking Lot System

A comprehensive object-oriented parking lot management system built with Java, demonstrating SOLID principles and multiple design patterns.

## 🏗️ Architecture & Design Patterns

### Design Patterns Implemented

1. **Singleton Pattern** - `ParkingLotService` ensures single instance of parking lot
2. **Strategy Pattern** - Pluggable pricing and slot allocation strategies
3. **Factory Pattern** - Vehicle, ParkingSpot, and Gate creation
4. **Observer Pattern** - Spot status updates (implicit through service methods)

### SOLID Principles Applied

- **S**RP: Each class has a single, well-defined responsibility
- **O**CP: System is open for extension (new vehicle types, strategies) but closed for modification
- **L**SP: All vehicle subtypes are substitutable for the base Vehicle class
- **I**SP: Interfaces are focused and client-specific
- **D**IP: High-level modules depend on abstractions, not concretions

## 📊 UML Class Diagram

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
```

## 🚀 Features

### Core Functionality
- ✅ Multi-floor parking support
- ✅ Multiple vehicle types (Car, Bike, Truck, Electric variants)
- ✅ Different spot types (Standard, Charging, Large)
- ✅ Multiple entry/exit gates
- ✅ Real-time spot availability tracking
- ✅ Automatic ticket generation and management

### Smart Features
- 🧠 **Intelligent Slot Allocation**: Finds optimal spots based on vehicle type and proximity
- 💰 **Dynamic Pricing**: Time-based pricing with spot type and charging multipliers
- ⚡ **Electric Vehicle Support**: Dedicated charging spots with additional fees
- 📊 **Real-time Monitoring**: Live status of floors, spots, and occupancy


## 📁 Project Structure

```
src/
├── enums/
│   ├── VehicleType.java       # Vehicle type enumeration
│   ├── SpotType.java          # Parking spot types
│   └── SpotStatus.java        # Spot availability status
├── models/
│   ├── vehicles/
│   │   ├── Vehicle.java       # Abstract base vehicle
│   │   ├── Car.java           # Standard car
│   │   ├── ElectricCar.java   # Electric car with charging needs
│   │   ├── Bike.java          # Motorcycle/bike
│   │   ├── ElectricBike.java  # Electric bike
│   │   └── Truck.java         # Large vehicle
│   ├── parking/
│   │   ├── ParkingSpot.java   # Individual parking spot
│   │   └── Ticket.java        # Entry/exit ticket
│   └── gates/
│       └── EntryExitGate.java # Gate management
├── strategies/
│   ├── pricing/
│   │   ├── PricingStrategy.java      # Pricing interface
│   │   └── HourlyPricingStrategy.java # Time-based pricing
│   └── allocation/
│       ├── SlotAllocationStrategy.java        # Allocation interface
│       ├── NearestAvailableStrategy.java      # Proximity-based
│       └── PreferredSpotTypeStrategy.java     # Type-preference
├── factories/
│   ├── VehicleFactory.java    # Vehicle creation
│   ├── ParkingSpotFactory.java # Spot creation
│   └── GateFactory.java       # Gate creation
├── services/
│   └── ParkingLotService.java # Main orchestration service
└── ParkingLotDemo.java        # Demo application
```

## 💡 Key Design Decisions

### 1. Strategy Pattern for Pricing
- Allows dynamic pricing models without modifying core logic
- Easy to add seasonal pricing, membership discounts, etc.
- Supports complex pricing rules (time-based, spot-type, charging fees)

### 2. Factory Pattern for Object Creation
- Centralizes object creation logic
- Ensures consistent initialization
- Simplifies adding new vehicle types

### 3. Singleton for Parking Lot Service
- Ensures single source of truth for parking state
- Thread-safe implementation for concurrent access
- Centralized management of all parking operations

### 4. Composition over Inheritance
- Vehicle types are composed of behaviors rather than deep inheritance
- Flexible spot type system without rigid hierarchies
- Easy to extend with new features
