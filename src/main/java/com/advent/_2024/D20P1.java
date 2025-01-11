package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Stream;

public class D20P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D20.txt");
        char[][] input;
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            input = lines.map(String::toCharArray).toArray(char[][]::new);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int[] start = new int[2];
        int[] end = new int[2];

        for (int row = 0; row < input.length; row++) {
            for (int column = 0; column < input[row].length; column++) {
                if (input[row][column] == 'S') {
                    start[0] = row;
                    start[1] = column;
                }
                if (input[row][column] == 'E') {
                    end[0] = row;
                    end[1] = column;
                }
            }
        }

        // first run without cheating to evaluate the limit
        Node shortestPathNode = getAllPaths(start, end, Integer.MAX_VALUE, false, input).getFirst();
        System.out.println("shortest path: " + shortestPathNode.distance + "\n");

        List<Node> cheatPathNodes = getAllPaths(start, end, shortestPathNode.distance - 99, true, input);
        System.out.println("shorter paths with cheat: " + cheatPathNodes.size() + "\n");

        int result = 0;
        for (Node path : cheatPathNodes) {
            System.out.println(path.cheatPosition.row + " " + path.cheatPosition.column);
            result++;
        }
        System.out.println(result);
    }

    private static List<Node> getAllPaths(int[] start, int[] end, int distanceLimit, boolean canCheat, char[][] input) {
        List<Node> paths = new ArrayList<>();
        Queue<Node> pq = new ArrayDeque<>();
        pq.add(new Node(start[0], start[1], 0, canCheat ? null : new CheatMarker(start[0], start[1]), null));

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        while (!pq.isEmpty()) {
            Node currentPosition = pq.poll();

            if (currentPosition.row == end[0] && currentPosition.column == end[1]) {
                paths.add(currentPosition);
            }

            for (int i = 0; i < 4; i++) {
                int nextRow = currentPosition.row + dr[i];
                int nextColumn = currentPosition.column + dc[i];

                if (currentPosition.previousNode != null &&
                        currentPosition.previousNode.row == nextRow &&
                        currentPosition.previousNode.column == nextColumn) {
                    // do not go backwards
                    continue;
                }

                if (nextColumn < 0 || nextColumn == input[0].length ||
                        nextRow < 0 || nextRow == input.length) {
                    continue; // skip out of range cords
                }

                CheatMarker nextCheated = currentPosition.cheatPosition;
                boolean isWall = input[nextRow][nextColumn] == '#';
                if (isWall && nextCheated != null) {
                    //already cheated -> wall is not accepted
                    continue;
                } else if (isWall) {
                    // no cheat yet -> wall is accepted
                    nextCheated = new CheatMarker(nextRow, nextColumn);
                }


                int newDistance = currentPosition.distance + 1;
                if (newDistance < distanceLimit) {
                    pq.add(new Node(nextRow, nextColumn, newDistance, nextCheated, currentPosition));
                }

            }
        }
        return paths;
    }

    private static class Node {
        int row;
        int column;
        int distance;
        CheatMarker cheatPosition;
        Node previousNode;

        Node(int row, int column, int distance, CheatMarker cheatPosition, Node previousNode) {
            this.row = row;
            this.column = column;
            this.distance = distance;
            this.cheatPosition = cheatPosition;
            this.previousNode = previousNode;
        }
    }

    private static class CheatMarker {
        int row;
        int column;

        CheatMarker(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
