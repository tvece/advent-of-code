package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D20P2 {

    public static final int LOW = 0;
    public static final int HIGH = 1;

    static Map<String, Device> devices = new HashMap<>();
    static Map<String, Long> mainFeeders = new HashMap<>();
    static List<Pulse> futurePulses = new ArrayList<>();

    static long numberOfButtonPresses = 0;

    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("../advent-of-code-input/2023/D20.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        // process input
        for (String row : rows) {
            String[] parts = row.split(" -> ");
            String token = parts[0].trim();
            List<String> targets = List.of(parts[1].split(", "));

            String name = "broadcaster";
            Type type = Type.BROADCASTER;
            if (token.charAt(0) == '%') {
                type = Type.FLIPFLOP;
                name = token.substring(1);
            } else if (token.charAt(0) == '&') {
                type = Type.CONJUNCTION;
                name = token.substring(1);
            }
            devices.put(name, new Device(name, type, targets, State.OFF));

        }

        // initialize memory of each dive if its conjunction device
        for (Device device : devices.values()) {
            for (String target : device.targets) {
                Device targetDevice = devices.get(target);
                if (targetDevice != null && // target is an output module
                        targetDevice.type == Type.CONJUNCTION) {
                    targetDevice.memory.put(device.name, LOW);
                }
            }
        }

        // find ultimate feeder
        Device ultimateFeenderDevice = devices.values().stream().filter(device -> device.targets.contains("rx")).findFirst().orElse(null);
        if (ultimateFeenderDevice == null) {
            throw new RuntimeException("Ultimate feeder not found!");
        }
        String ultimateFeeder = ultimateFeenderDevice.name;

        // fill main feeders
        for (Device device : devices.values()) {
            if (device.targets.contains(ultimateFeeder)) {
                mainFeeders.put(device.name, -1L); // not yet pulsed high)
            }
        }

        while (true) {
            runOnce();
        }
    }

    public static void runOnce() {
        numberOfButtonPresses++;
        broadcast(LOW);
        while (!futurePulses.isEmpty()) {
            List<Pulse> pulses = new ArrayList<>(futurePulses);
            futurePulses.clear();
            dispatchPulses(pulses);
        }
    }

    static void broadcast(int pulse) {
        Device device = devices.get("broadcaster");
        for (String target : device.targets) {
            schedulePulse("broadcaster", target, pulse);
        }
    }

    static void schedulePulse(String sender, String target, int pulse) {
        futurePulses.add(new Pulse(sender, target, pulse));
    }

    static void dispatchPulses(List<Pulse> pulses) {
        for (Pulse pulse : pulses) {
            receivePulse(pulse.sender, pulse.pulse, pulse.target);
        }
    }

    static void receivePulse(String sender, int pulse, String target) {
        // record button press count when a main feeder receives a LOW pulse for the first time.
        if (pulse == LOW && mainFeeders.containsKey(target) && mainFeeders.get(target) == -1L) {
            mainFeeders.put(target, numberOfButtonPresses);
            tryFinish();
        }

        Device device = devices.get(target);
        if (device == null) return; // target is an output module

        if (device.type == Type.BROADCASTER) {
            broadcast(pulse);
            return;
        }
        if (device.type == Type.FLIPFLOP) {
            flipflopReceivePulse(device, pulse);
            return;
        }
        conjunctionReceivePulse(sender, device, pulse);
    }

    // for flipflop devices toggles state on a LOW pulse and sends new pulse accordingly.
    static void flipflopReceivePulse(Device device, int pulse) {
        if (pulse == HIGH) return;
        int newPulse = (device.state == State.OFF) ? HIGH : LOW;
        device.state = (device.state == State.OFF) ? State.ON : State.OFF;
        for (String target : device.targets) {
            schedulePulse(device.name, target, newPulse);
        }
    }

    // for conjunction devices updates memory and sends a pulse based on the memory values.
    static void conjunctionReceivePulse(String sender, Device device, int pulse) {
        device.memory.put(sender, pulse);
        int lows = 0;
        for (Integer value : device.memory.values()) {
            if (value == LOW) lows++;
        }
        // If there are no LOW values in memory, then all inputs are HIGH,
        // so the new pulse should be LOW. Otherwise, it should be HIGH.
        int newPulse = (lows == 0) ? LOW : HIGH;
        for (String target : device.targets) {
            schedulePulse(device.name, target, newPulse);
        }
    }

    static void tryFinish() {
        Collection<Long> values = mainFeeders.values();
        if (values.contains(-1L)) return;
        long lcmValue = lowestCommonMultiple(new ArrayList<>(values));
        System.out.println(lcmValue);
        System.exit(0);
    }

    static long lowestCommonMultiple(List<Long> list) {
        long multiple = list.getFirst();
        for (int n = 1; n < list.size(); n++) {
            multiple = lcm(multiple, list.get(n));
        }
        return multiple;
    }

    static long lcm(long a, long b) {
        return (a * b) / gcd(a, b);
    }

    static long gcd(long a, long b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    public enum Type {
        BROADCASTER, FLIPFLOP, CONJUNCTION
    }


    public enum State {
        ON, OFF
    }

    record Pulse(String sender, String target, int pulse) {
    }

    private static class Device {
        String name;
        Type type;
        List<String> targets;
        State state;
        Map<String, Integer> memory;

        Device(String name, Type kind, List<String> targets, State state) {
            this.name = name;
            this.type = kind;
            this.targets = targets;
            this.state = state;
            this.memory = new HashMap<>();
        }
    }
}