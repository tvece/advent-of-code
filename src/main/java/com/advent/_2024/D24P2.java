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

/*

set all x and y to 0 and then set only one pair of x and y at a time to 1 and get result of all gates - some items (adder part) in the sequence of results do not follow the expected sequence of powers of 2
then the wrong adder parts can be compared to something like https://content.instructables.com/F3D/2GZ2/KNVR5S0C/F3D2GZ2KNVR5S0C.png to find the to be swapped gates

for my input it is:
vcf  z10
tnc  z39
fhg  z17
fsq  dvb

 */

public class D24P2 {

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
                    wires.put(split[0], false);
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

        for (int test = 0; test < 45; test++) {
            long result = 0;
            System.out.println("test:" + test);
            String testString = test < 10 ? ("0" + test) : String.valueOf(test);
            wires.put("x" + testString, true);
            wires.put("y" + testString, true);
            for (int i = 0; i < zGates.size(); i++) {
                Gate gate = zGates.get(i);
                int gateValue = getGateValue(gate, gates, wires) ? 1 : 0;
                result += (long) (gateValue * Math.pow(2, i));
            }
            wires.put("x" + testString, false);
            wires.put("y" + testString, false);
            System.out.println("result: " + result);
        }


        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static boolean getGateValue(Gate gate, HashMap<String, Gate> gates, HashMap<String, Boolean> wires) {
        boolean result;
        boolean isWire = gate.characterA.startsWith("x") || gate.characterA.startsWith("y");
        boolean valueA;
        if (isWire) {
            valueA = wires.get(gate.characterA);
        } else {
            valueA = getGateValue(gates.get(gate.characterA), gates, wires);
        }

        isWire = gate.characterB.startsWith("x") || gate.characterB.startsWith("y");
        boolean valueB;
        if (isWire) {
            valueB = wires.get(gate.characterB);
        } else {
            valueB = getGateValue(gates.get(gate.characterB), gates, wires);
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