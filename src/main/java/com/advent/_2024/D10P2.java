package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// https://adventofcode.com/2024/day/10
public class D10P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D10.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int[][] map = new int[input.size()][input.getFirst().length()];

        for (int rowIndex = 0; rowIndex < input.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < input.getFirst().length(); columnIndex++) {
                map[rowIndex][columnIndex] = Character.getNumericValue(input.get(rowIndex).charAt(columnIndex));
            }
        }

        int totalCount = 0;
        for (int rowIndex = 0; rowIndex < map.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < map[0].length; columnIndex++) {
                if (map[rowIndex][columnIndex] == 0) {
                    int currentCount = getTrails(map, rowIndex, columnIndex, 0, "");
                    totalCount += currentCount;
                }

            }
        }

        System.out.println(totalCount);
    }

    private static int getTrails(int[][] map, int rowIndex, int columnIndex, int currentValue, String sequence) {
        // out of bounds
        if (rowIndex < 0 || rowIndex == map.length) {
            return 0;
        }
        if (columnIndex < 0 || columnIndex == map[rowIndex].length) {
            return 0;
        }
        // not valid neighbor
        if (currentValue != map[rowIndex][columnIndex]) {
            return 0;
        }
        // top of the trail
        if (map[rowIndex][columnIndex] == 9) {
            return 1;
        }
        // inspect neighbors
        int nextValue = currentValue + 1;
        return getTrails(map, rowIndex - 1, columnIndex, nextValue, sequence + "[" + rowIndex + "," + columnIndex + "]") + // north
                getTrails(map, rowIndex, columnIndex + 1, nextValue, sequence + "[" + rowIndex + "," + columnIndex + "]") + // east
                getTrails(map, rowIndex + 1, columnIndex, nextValue, sequence + "[" + rowIndex + "," + columnIndex + "]") + // south
                getTrails(map, rowIndex, columnIndex - 1, nextValue, sequence + "[" + rowIndex + "," + columnIndex + "]"); //west
    }
}
