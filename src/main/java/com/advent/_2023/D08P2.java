package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class D08P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2023/D08.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        int instructionsLength = lines.getFirst().length();
        int[] instructions = lines.getFirst().chars().map(character -> character == 'L' ? 0 : 1).toArray();

        //ZZZ + L + R
        List<Integer> map = new ArrayList<>(Collections.nCopies(getStringToIntPath("ZZZ") + 2, 0));
        List<Integer> currentStepsList = new ArrayList<>();
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            String brackets = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
            String[] stringDestinations = brackets.split(", ");
            String indexPath = line.substring(0, line.indexOf(' '));
            int mapIndex = getStringToIntPath(indexPath);
            if (indexPath.charAt(2) == 'A') {
                currentStepsList.add(mapIndex);
            }
            map.set(mapIndex, getStringToIntPath(stringDestinations[0]));
            map.set(mapIndex + 1, getStringToIntPath(stringDestinations[1]));
        }

        Integer[] currentSteps = currentStepsList.toArray(Integer[]::new);
        long[] solutions = new long[currentSteps.length];
        int solutionCounter = -1;
        int index = -1;
        for (Integer step : currentSteps) {
            Integer currentStep = step;
            int stepCounter = 0;
            solutionCounter++;
            while (true) {
                if ((currentStep % 1000) / 10 == 90) {
                    solutions[solutionCounter] = stepCounter;
                    break;
                }
                stepCounter++;
                index++;
                if (index == instructionsLength) {
                    index = 0;
                }
                if (instructions[index] == 0) {
                    // L
                    currentStep = map.get(currentStep);
                } else {
                    // R
                    currentStep = map.get(currentStep + 1);
                }
            }
        }
        System.out.println(lcm(solutions));
    }

    private static int getStringToIntPath(String stringPath) {
        return ((int) stringPath.charAt(0) * 100000 + (int) stringPath.charAt(1) * 1000
                + (int) stringPath.charAt(2) * 10);
    }

    private static long gcd(long x, long y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static long lcm(long... numbers) {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }
}