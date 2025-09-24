package src.models.gates;

public class EntryExitGate {
    private final String gateId;
    private final String location;
    private final int floor;
    private final GateType gateType;
    private boolean isOperational;

    public EntryExitGate(String gateId, String location, int floor, GateType gateType) {
        this.gateId = gateId;
        this.location = location;
        this.floor = floor;
        this.gateType = gateType;
        this.isOperational = true;
    }

    public String getGateId() {
        return gateId;
    }

    public String getLocation() {
        return location;
    }

    public int getFloor() {
        return floor;
    }

    public GateType getGateType() {
        return gateType;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setOperational(boolean operational) {
        this.isOperational = operational;
    }

    public boolean canProcessEntry() {
        return isOperational && (gateType == GateType.ENTRY || gateType == GateType.BOTH);
    }

    public boolean canProcessExit() {
        return isOperational && (gateType == GateType.EXIT || gateType == GateType.BOTH);
    }

    @Override
    public String toString() {
        return String.format("Gate[%s] %s Floor:%d Type:%s Status:%s",
                gateId, location, floor, gateType,
                isOperational ? "Operational" : "Closed");
    }

    public enum GateType {
        ENTRY("Entry Only"),
        EXIT("Exit Only"),
        BOTH("Entry & Exit");

        private final String description;

        GateType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
