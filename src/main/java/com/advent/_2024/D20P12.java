package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class D20P12 {
    // PART 2
    // maximum ps for cheat
    final static int RANGE = 20;
    // minimal improvement compared to distance without cheat
    final static int MINIMAL_IMPROVEMENT = 100;

    // PART 1
    //final static int RANGE = 2;
    //final static int MINIMAL_IMPROVEMENT = 100;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D20.txt");
        char[][] input;
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            input = lines.map(String::toCharArray).toArray(char[][]::new);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        Position start = null;
        Position end = null;

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                if (input[row][column] == 'S') {
                    start = new Position(row, column);
                }
                if (input[row][column] == 'E') {
                    end = new Position(row, column);
                }
            }
        }
        if (start == null) {
            throw new RuntimeException("Failed to find start position");
        }

        if (end == null) {
            throw new RuntimeException("Failed to find end position");
        }

        // evaluate racetrack

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        List<Position> path = new ArrayList<>();
        Position previousPosition = start;
        Position currentPosition = start;
        path.add(currentPosition);
        int distance = 0;
        int[][] distances = new int[input.length][input[0].length];
        for (int i = 0; i < input.length; i++) {
            Arrays.fill(distances[i], -1);
        }
        distances[start.row][start.column] = 0;
        while (true) {
            if (currentPosition.row == end.row && currentPosition.column == end.column) {
                path.add(currentPosition);
                break;
            }

            for (int i = 0; i < 4; i++) {
                int nextRow = currentPosition.row + dr[i];
                int nextColumn = currentPosition.column + dc[i];

                if (previousPosition.row == nextRow && previousPosition.column == nextColumn) {
                    // do not go backwards
                    continue;
                }

                if (nextColumn < 0 || nextColumn == input[0].length ||
                        nextRow < 0 || nextRow == input.length ||
                        input[nextRow][nextColumn] == '#'
                ) {
                    continue; // skip walls
                }

                previousPosition = currentPosition;
                distance++;
                distances[nextRow][nextColumn] = distance;
                currentPosition = new Position(nextRow, nextColumn);
                path.add(currentPosition);
                break;
            }
        }

        System.out.println("Shortest path size: " + path.size());

        int result = 0;
        for (int i = 0; i < path.size() - 1; i++) { // do not search for cheat when end is reached
            currentPosition = path.get(i);
            for (int jumpByRow = -RANGE; jumpByRow <= RANGE; jumpByRow++) {
                for (int jumpByColumn = -RANGE; jumpByColumn <= RANGE; jumpByColumn++) {
                    if (jumpByRow == 0 && jumpByColumn == 0) {
                        continue;   // jump to same position is not a cheat
                    }
                    int manhattanDistance = Math.abs(jumpByRow) + Math.abs(jumpByColumn);
                    if (manhattanDistance <= RANGE) {    // must be in range
                        if (currentPosition.row + jumpByRow < 0 || currentPosition.row + jumpByRow >= distances.length ||
                                currentPosition.column + jumpByColumn < 0 || currentPosition.column + jumpByColumn >= distances[0].length ||
                                distances[currentPosition.row + jumpByRow][currentPosition.column + jumpByColumn] == -1) {
                            continue; //jump to wall or out of range
                        }
                        int distanceDiff = distances[currentPosition.row + jumpByRow][currentPosition.column + jumpByColumn] - (distances[currentPosition.row][currentPosition.column] + manhattanDistance);

                        if (distanceDiff >= MINIMAL_IMPROVEMENT) { // jump must improve the distance by at least minimalImprovement
                            result++;
                        }
                    }
                }
            }
        }
        System.out.println(result);
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private record Position(int row, int column) {
    }
}
