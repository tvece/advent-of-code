package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D13 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D13.txt");
        List<String> input;
        List<Machine> machines = new ArrayList<>();
        Machine readingMachine = new Machine();
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        for (String line : input) {
            if (line.startsWith("Button A: ")) {
                readingMachine = new Machine();
                line = line.substring("Button A: X".length());
                readingMachine.xIncrementA = Integer.parseInt(line.substring(0, line.indexOf(",")));
                readingMachine.YIncrementA = Integer.parseInt(line.substring(line.indexOf("Y") + 1));
            } else if (line.startsWith("Button B: ")) {
                line = line.substring("Button B: X".length());
                readingMachine.xIncrementB = Integer.parseInt(line.substring(0, line.indexOf(",")));
                readingMachine.YIncrementB = Integer.parseInt(line.substring(line.indexOf("Y") + 1));
            } else if (line.startsWith("Prize: ")) {
                readingMachine.prizeX = Long.parseLong(line.substring(line.indexOf("X") + 2, line.indexOf(","))) + 10000000000000L;
                readingMachine.prizeY = Long.parseLong(line.substring(line.indexOf("Y=") + 2)) + 10000000000000L;
                machines.add(readingMachine);
            }
        }

        long result = 0;
        for (Machine machine : machines) {
            long determinant = ((long) machine.xIncrementA * machine.YIncrementB) - ((long) machine.YIncrementA * machine.xIncrementB);
            long determinantX = (machine.prizeX * machine.YIncrementB) - (machine.prizeY * machine.xIncrementB);
            long determinantY = (machine.xIncrementA * machine.prizeY) - (machine.YIncrementA * machine.prizeX);
            if (determinantX % determinant == 0 && determinantY % determinant == 0) {
                long resultA = determinantX / determinant;
                long resultB = determinantY / determinant;
                result += (3 * resultA) + resultB;
            }
        }
        System.out.println(result);
    }

    private static class Machine {
        int xIncrementA;
        int YIncrementA;
        int xIncrementB;
        int YIncrementB;
        long prizeX;
        long prizeY;
    }


}
