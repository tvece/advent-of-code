package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2024/day/13
public class D13P12 {

    // Part1
    private static final long CORRECTION = 0;
    // Part 2
    //private static final long CORRECTION = 10000000000000L;

    private static final String BUTTON_A_PREFIX = "Button A: X";
    private static final String BUTTON_B_PREFIX = "Button B: X";

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D13.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        List<Machine> machines = new ArrayList<>();
        Machine readingMachine = new Machine();
        for (String line : input) {
            if (line.startsWith(BUTTON_A_PREFIX)) {
                readingMachine = new Machine();
                line = line.substring(BUTTON_A_PREFIX.length());
                readingMachine.xIncrementA = Integer.parseInt(line.substring(0, line.indexOf(",")));
                readingMachine.YIncrementA = Integer.parseInt(line.substring(line.indexOf("Y") + 1));
            } else if (line.startsWith(BUTTON_B_PREFIX)) {
                line = line.substring(BUTTON_B_PREFIX.length());
                readingMachine.xIncrementB = Integer.parseInt(line.substring(0, line.indexOf(",")));
                readingMachine.YIncrementB = Integer.parseInt(line.substring(line.indexOf("Y") + 1));
            } else if (line.startsWith("Prize: ")) {
                readingMachine.prizeX = Long.parseLong(line.substring(line.indexOf("X") + 2, line.indexOf(","))) + CORRECTION;
                readingMachine.prizeY = Long.parseLong(line.substring(line.indexOf("Y=") + 2)) + CORRECTION;
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
