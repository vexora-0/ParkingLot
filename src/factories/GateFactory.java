package src.factories;

import src.models.gates.EntryExitGate;
import src.models.gates.EntryExitGate.GateType;
import java.util.ArrayList;
import java.util.List;

public class GateFactory {

    public static EntryExitGate createGate(String gateId, String location, int floor, GateType type) {
        return new EntryExitGate(gateId, location, floor, type);
    }

    public static List<EntryExitGate> createDefaultGates() {
        List<EntryExitGate> gates = new ArrayList<>();

        gates.add(createGate("G01", "North Entrance", 0, GateType.ENTRY));
        gates.add(createGate("G02", "South Entrance", 0, GateType.ENTRY));
        gates.add(createGate("G03", "East Exit", 0, GateType.EXIT));
        gates.add(createGate("G04", "West Exit", 0, GateType.EXIT));
        gates.add(createGate("G05", "Main Gate", 0, GateType.BOTH));

        return gates;
    }
}

