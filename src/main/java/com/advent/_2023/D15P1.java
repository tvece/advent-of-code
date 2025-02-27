package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// https://adventofcode.com/2023/day/15
public class D15P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D15.txt");
        String row;
        try {
            row = Files.readAllLines(filePath, StandardCharsets.UTF_8).getFirst();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        int result = 0;
        for (String step : row.split(",")) {
            result += getHash(step);
        }

        System.out.println(result);
    }

    private static int getHash(String step) {
        int stepResult = 0;
        for (char character : step.toCharArray()) {
            stepResult += character;
            stepResult = stepResult * 17;
            stepResult = stepResult % 256;
        }
        return stepResult;
    }
}