package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D06P12 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D06.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int[] startPosition = new int[2];
        start:
        for (int i = 0; i < rows.size(); i++) {
            for (int j = 0; j < rows.getFirst().length(); j++) {
                if (rows.get(i).charAt(j) == '^') {
                    startPosition = new int[]{i, j};
                    break start;
                }
            }
        }

        RunResult firstRun = run(rows, startPosition);
        int result = 0;
        System.out.println("Part1: " + firstRun.uniquePositions().size());
        int numberOfRuns = 0;
        for (String uniquePosition : firstRun.uniquePositions()) {
            numberOfRuns += 1;
            System.out.println(numberOfRuns);
            String[] stringCords = uniquePosition.split(" ");
            int rowIndex = Integer.parseInt(stringCords[0]);
            String row = rows.get(rowIndex);
            int replaceIndex = Integer.parseInt(stringCords[1]);
            row = row.substring(0, replaceIndex) + "#" + row.substring(replaceIndex + 1);
            rows.set(rowIndex, row);
            RunResult runResult = run(rows, startPosition);
            if (runResult.isLoop()) {
                result++;
            }

            row = row.substring(0, replaceIndex) + "." + row.substring(replaceIndex + 1);
            rows.set(rowIndex, row);
        }
        System.out.println("Part 2: " + result);
    }

    private static RunResult run(List<String> rows, int[] startPosition) {
        int direction = 1;
        int i = startPosition[0];
        int j = startPosition[1];
        Set<String> uniquePositions = new HashSet<>();
        List<String> history = new ArrayList<>();
        boolean isLoop = false;
        while (i >= 0 && j >= 0 && i < rows.size() && j < rows.getFirst().length()) {
            if (rows.get(i).charAt(j) == '#') {
                if (direction == 1) {
                    i += 1;
                } else if (direction == 2) {
                    j -= 1;
                } else if (direction == 3) {
                    i -= 1;
                } else {
                    j += 1;
                }
                direction = (direction % 4) + 1;
            } else {
                uniquePositions.add(i + " " + j);
                String historyEntry = i + ", " + j + " - " + direction;
                if (history.contains(historyEntry)) {
                    isLoop = true;
                    break;
                }
                history.add(historyEntry);
                if (direction == 1) {
                    i -= 1;
                } else if (direction == 2) {
                    j += 1;
                } else if (direction == 3) {
                    i += 1;
                } else {
                    j -= 1;
                }
            }
        }
        return new RunResult(history, uniquePositions, isLoop);
    }

    private record RunResult(List<String> history, Set<String> uniquePositions, boolean isLoop) {
    }
}
