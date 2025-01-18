package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class D24P1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D24.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        HashMap<String, Boolean> wires = new HashMap<>();
        HashMap<String, Gate> gates = new HashMap<>();
        List<Gate> zGates = new ArrayList<>();
        boolean readingFirstPart = true;
        for (String line : input) {
            if (line.isEmpty()) {
                readingFirstPart = false;
            } else {
                if (readingFirstPart) {
                    String[] split = line.split(": ");
                    wires.put(split[0], split[1].equals("1"));
                } else {
                    Gate gate = new Gate(line);
                    gates.put(gate.id, gate);
                    if (gate.id.startsWith("z")) {
                        zGates.add(gate);
                    }
                }
            }
        }
        zGates.sort(Comparator.comparingInt(gate -> Integer.parseInt(gate.id.substring(1))));

        HashMap<String, Boolean> cache = new HashMap<>();
        long result = 0;
        for (int i = 0; i < zGates.size(); i++) {
            Gate gate = zGates.get(i);
            System.out.println(gate.id);
            result += (long) ((getGateValue(gate, gates, wires, cache) ? 1 : 0) * Math.pow(2, i));
        }

        System.out.println("result: " + result);
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static boolean getGateValue(Gate gate, HashMap<String, Gate> gates, HashMap<String, Boolean> wires, HashMap<String, Boolean> cache) {
        boolean result;

        boolean isWire = gate.characterA.startsWith("x") || gate.characterA.startsWith("y");
        boolean valueA;
        if (isWire) {
            valueA = wires.get(gate.characterA);
        } else {
            if (cache.containsKey(gate.characterA)) {
                valueA = cache.get(gate.characterA);
            } else {
                valueA = getGateValue(gates.get(gate.characterA), gates, wires, cache);
            }
        }

        isWire = gate.characterB.startsWith("x") || gate.characterB.startsWith("y");
        boolean valueB;
        if (isWire) {
            valueB = wires.get(gate.characterB);
        } else {
            if (cache.containsKey(gate.characterB)) {
                valueB = cache.get(gate.characterB);
            } else {
                valueB = getGateValue(gates.get(gate.characterB), gates, wires, cache);
            }
        }

        switch (gate.operation) {
            case Operation.AND:
                result = valueA && valueB;
                break;
            case Operation.OR:
                result = valueA || valueB;
                break;
            case Operation.XOR:
                result = valueA != valueB;
                break;
            default:
                throw new RuntimeException("Invalid operation: " + gate.operation);
        }

        cache.put(gate.id, result);
        return result;
    }

    private enum Operation {
        AND,
        OR,
        XOR
    }

    private static class Gate {
        String characterA;
        String characterB;
        Operation operation;
        String id;

        //ntg XOR fgs -> mjb
        public Gate(String line) {
            String[] split = line.split(" ");
            characterA = split[0];
            characterB = split[2];
            id = split[4];
            switch (split[1]) {
                case "AND":
                    operation = Operation.AND;
                    break;
                case "OR":
                    operation = Operation.OR;
                    break;
                case "XOR":
                    operation = Operation.XOR;
                    break;
                default:
                    throw new RuntimeException("Invalid operation: " + split[1]);
            }
        }
    }
}