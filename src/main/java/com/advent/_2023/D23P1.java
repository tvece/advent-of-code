package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// https://adventofcode.com/2023/day/23
public class D23P1 {
    static int HEIGHT;
    static int WIDTH;
    static char[][] map;
    static int maxSteps = -1;
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void main(String[] args) throws InterruptedException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("../advent-of-code-input/2023/D23.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        HEIGHT = lines.size();
        WIDTH = lines.getFirst().length();
        map = new char[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            map[i] = lines.get(i).toCharArray();
        }

        // find start
        int startRow = 0, startColumn = -1;
        for (int j = 0; j < WIDTH; j++) {
            if (map[0][j] == '.') {
                startColumn = j;
                break;
            }
        }
        if (startColumn == -1) {
            System.out.println("Starting point not found");
            return;
        }

        // find end
        int endRow = HEIGHT - 1;
        int endColumn = -1;
        for (int j = 0; j < WIDTH; j++) {
            if (map[HEIGHT - 1][j] == '.') {
                endColumn = j;
                break;
            }
        }
        if (endColumn == -1) {
            System.out.println("No finishing path found in bottom row.");
            return;
        }

        boolean[][] visited = new boolean[HEIGHT][WIDTH];
        visited[startRow][startColumn] = true;
        dfs(startRow, startColumn, 0, endRow, endColumn, visited);

        System.out.println(maxSteps);
    }

    static void dfs(int row, int column, int steps, int endRow, int endColumn, boolean[][] visited) {
        if (row == endRow && column == endColumn) {
            maxSteps = Math.max(maxSteps, steps);
            return;
        }

        // calculate maximum number of next steps
        int remaining = floodFillCount(row, column, visited);
        if (steps + remaining <= maxSteps) {
            // nothing better than maxSteps is possible
            return;
        }

        if (isSlope(map[row][column])) {
            int[] move = forcedMove(map[row][column]);
            int newRow = row + move[0];
            int newColumn = column + move[1];
            if (isInMap(newRow, newColumn) && !visited[newRow][newColumn] && isWalkable(newRow, newColumn)) {
                visited[newRow][newColumn] = true;
                dfs(newRow, newColumn, steps + 1, endRow, endColumn, visited);
                visited[newRow][newColumn] = false;
            }
        } else {
            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newColumn = column + direction[1];
                if (isInMap(newRow, newColumn) && !visited[newRow][newColumn] && isWalkable(newRow, newColumn)) {
                    visited[newRow][newColumn] = true;
                    dfs(newRow, newColumn, steps + 1, endRow, endColumn, visited);
                    visited[newRow][newColumn] = false;
                }
            }
        }
    }

    /**
     * Flood fill to count the number of unvisited, reachable walkable cells.
     */
    static int floodFillCount(int startRow, int startColumn, boolean[][] visited) {
        boolean[][] seen = new boolean[HEIGHT][WIDTH];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startColumn});
        seen[startRow][startColumn] = true;
        int count = 0;
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int column = current[1];
            // Count this cell if it's unvisited and walkable.
            if (!visited[row][column] && isWalkable(row, column)) {
                count++;
            }
            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newColumn = column + direction[1];
                if (isInMap(newRow, newColumn) && !seen[newRow][newColumn] && isWalkable(newRow, newColumn) && !visited[newRow][newColumn]) {
                    seen[newRow][newColumn] = true;
                    queue.add(new int[]{newRow, newColumn});
                }
            }
        }
        return count;
    }

    static boolean isInMap(int row, int column) {
        return row >= 0 && row < HEIGHT && column >= 0 && column < WIDTH;
    }

    static boolean isWalkable(int row, int column) {
        char character = map[row][column];
        return character == '.' || isSlope(character);
    }

    static boolean isSlope(char character) {
        return character == '^' || character == 'v' || character == '<' || character == '>';
    }

    static int[] forcedMove(char ch) {
        switch (ch) {
            case '^':
                return new int[]{-1, 0};
            case 'v':
                return new int[]{1, 0};
            case '<':
                return new int[]{0, -1};
            case '>':
                return new int[]{0, 1};
            default:
                throw new RuntimeException("Unsupported map character '" + ch + "'");
        }
    }
}
