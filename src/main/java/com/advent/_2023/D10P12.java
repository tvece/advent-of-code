package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D10P12 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D10.txt");
        List<String> stringLines;
        try {
            stringLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        char[][] map = new char[stringLines.size()][stringLines.getFirst().length()];
        int[] currentPosition = null;
        for (int i = 0; i < stringLines.size(); i++) {
            String stringLine = stringLines.get(i);
            for (int j = 0; j < stringLine.length(); j++) {

                char currentChar = stringLine.charAt(j);
                if (currentChar == 'S') {
                    currentPosition = new int[]{i, j};
                }
                map[i][j] = currentChar;
            }
        }
        if (currentPosition == null) {
            throw new RuntimeException("Start position not found!");
        }
        int[] previousPosition;
        List<int[]> path = new ArrayList<>();
        path.add(currentPosition);
        path.add(currentPosition);
        boolean hasNorth = false;
        boolean hasWest = false;
        boolean hasSouth = false;
        boolean hasEast = false;
        while (true) {
            previousPosition = path.get(path.size() - 2);
            char currentChar = map[currentPosition[0]][currentPosition[1]];
            switch (currentChar) {
                case 'S':
                    boolean foundInitialDirection = false;
                    // north
                    if (!(previousPosition[0] == currentPosition[0] - 1
                            && previousPosition[1] == currentPosition[1])
                            && isInRange(currentPosition[0] - 1, currentPosition[1], map)) {
                        char testChar = map[currentPosition[0] - 1][currentPosition[1]];
                        if (testChar == '|' || testChar == '7' || testChar == 'F') {
                            currentPosition[0] -= 1;
                            foundInitialDirection = true;
                            hasNorth = true;
                        }
                    }
                    // east
                    if (!(previousPosition[0] == currentPosition[0]
                            && previousPosition[1] == currentPosition[1] + 1)
                            && isInRange(currentPosition[0], currentPosition[1] + 1, map)) {
                        char testChar = map[currentPosition[0]][currentPosition[1] + 1];
                        if (testChar == '-' || testChar == 'J' || testChar == '7') {
                            if (!foundInitialDirection) {
                                currentPosition[1] += 1;
                                foundInitialDirection = true;
                            }
                            hasEast = true;
                        }
                    }
                    // south
                    if (!(previousPosition[0] == currentPosition[0] + 1
                            && previousPosition[1] == currentPosition[1])
                            && isInRange(currentPosition[0] + 1, currentPosition[1], map)) {
                        char testChar = map[currentPosition[0]][currentPosition[1] + 1];
                        if (testChar == '|' || testChar == 'L' || testChar == 'J') {
                            if (!foundInitialDirection) {
                                currentPosition[0] += 1;
                                foundInitialDirection = true;
                            }
                            hasSouth = true;
                        }
                    }
                    // west
                    if (!(previousPosition[0] == currentPosition[0]
                            && previousPosition[1] == currentPosition[1] - 1)
                            && isInRange(currentPosition[0], currentPosition[1] - 1, map)) {
                        char testChar = map[currentPosition[0]][currentPosition[1] - 1];
                        if (testChar == '-' || testChar == 'L' || testChar == 'F') {
                            if (!foundInitialDirection) {
                                currentPosition[1] -= 1;
                            }
                            hasWest = true;

                        }
                    }
                    break;
                case ('|'):
                    if (!(previousPosition[0] == currentPosition[0] - 1
                            && previousPosition[1] == currentPosition[1])) {
                        // north
                        currentPosition[0] -= 1;
                    } else {
                        // south
                        currentPosition[0] += 1;
                    }
                    break;
                case ('-'):
                    if (!(previousPosition[0] == currentPosition[0]
                            && previousPosition[1] == currentPosition[1] + 1)) {
                        // east
                        currentPosition[1] += 1;
                    } else {
                        // west
                        currentPosition[1] -= 1;
                    }
                    break;
                case ('L'):
                    if (!(previousPosition[0] == currentPosition[0] - 1
                            && previousPosition[1] == currentPosition[1])) {
                        // north
                        currentPosition[0] -= 1;
                    } else {
                        // east
                        currentPosition[1] += 1;
                    }
                    break;
                case ('J'):
                    if (!(previousPosition[0] == currentPosition[0] - 1
                            && previousPosition[1] == currentPosition[1])) {
                        // north
                        currentPosition[0] -= 1;
                    } else {
                        // west
                        currentPosition[1] -= 1;
                    }
                    break;
                case ('7'):
                    if (!(previousPosition[0] == currentPosition[0]
                            && previousPosition[1] == currentPosition[1] - 1)) {
                        // west
                        currentPosition[1] -= 1;
                    } else {
                        // south
                        currentPosition[0] += 1;
                    }
                    break;
                case ('F'):
                    if (!(previousPosition[0] == currentPosition[0]
                            && previousPosition[1] == currentPosition[1] + 1)) {
                        // east
                        currentPosition[1] += 1;
                    } else {
                        // south
                        currentPosition[0] += 1;
                    }
                    break;
                default:
                    break;
            }
            if (map[currentPosition[0]][currentPosition[1]] == 'S') {
                break;
            }
            path.add(new int[]{currentPosition[0], currentPosition[1]});
        }
        System.out.println((path.size() - 1) / 2);

        int enclosed = 0;
        for (int row = 0; row < map.length; row++) {
            boolean inside = false;
            int tubeCount = 0;
            char previousChar = 'X';
            for (int column = 0; column < map[0].length; column++) {
                char currentChar = map[row][column];
                boolean isCurrentCharPath = isPath(row, column, path);
                if (isCurrentCharPath && currentChar != '-') {
                    if (currentChar == 'S') {
                        if (hasNorth && hasSouth) {
                            currentChar = '|';
                        }
                        if (hasWest && hasEast) {
                            currentChar = '-';
                        }
                        if (hasNorth && hasEast) {
                            currentChar = 'L';
                        }
                        if (hasNorth && hasWest) {
                            currentChar = 'J';
                        }
                        if (hasSouth && hasEast) {
                            currentChar = 'F';
                        }
                        if (hasSouth && hasWest) {
                            currentChar = '7';
                        }
                    }
                    tubeCount++;
                    if ((currentChar == 'J' && previousChar == 'F')
                            || (currentChar == '7' && previousChar == 'L')) {
                        tubeCount--;
                    }
                    previousChar = currentChar;
                    if (tubeCount % 2 == 1) {
                        inside = true;
                    } else {
                        inside = false;
                    }
                }
                if (inside && !isCurrentCharPath) {
                    enclosed++;
                }
            }
        }
        System.out.println(enclosed);
    }

    private static boolean isPath(int row, int column, List<int[]> path) {
        return path.stream().anyMatch(element -> element[0] == row && element[1] == column);
    }

    private static boolean isInRange(int xPosition, int yPosition, char[][] map) {
        return xPosition >= 0 && xPosition < map.length && yPosition >= 0 && map[0].length > yPosition;
    }
}