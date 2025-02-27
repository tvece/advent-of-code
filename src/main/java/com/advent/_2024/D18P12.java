package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

// https://adventofcode.com/2024/day/18
public class D18P12 {
    final static int ROWS = 71;
    final static int COLUMNS = 71;
    final static int BYTES = 2994;

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D18.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int[][] map = new int[COLUMNS][ROWS];
        for (int[] column : map) {
            Arrays.fill(column, 0);
        }
        for (int i = 0; i < BYTES; i++) {
            // "5,4" -> [5, 4]
            String[] cords = input.get(i).split(",");
            map[Integer.parseInt(cords[0])][Integer.parseInt(cords[1])] = 1;
        }

        int[][] distances = new int[map.length][map[0].length];
        for (int[] column : distances) {
            Arrays.fill(column, Integer.MAX_VALUE);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 0, 0));
        distances[0][0] = 0;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            if (currentNode.row == ROWS - 1 && currentNode.column == COLUMNS - 1) {
                System.out.println("Shortest path: " + currentNode.distance);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int nextRow = currentNode.row + dr[i];
                int nextColumn = currentNode.column + dc[i];

                if (nextColumn < 0 || nextColumn == COLUMNS || nextRow < 0 || nextRow == ROWS || map[nextColumn][nextRow] == 1) {
                    continue; // Skip walls
                }
                int newDistance = currentNode.distance + 1;

                if (newDistance < distances[nextColumn][nextRow]) {
                    distances[nextColumn][nextRow] = newDistance;
                    pq.add(new D18P12.Node(nextColumn, nextRow, newDistance));
                }
            }
        }
        System.out.println("No path found.\nBytes:" + BYTES + "\nLast byte coordinates: " + input.get(BYTES - 1));
    }

    private static class Node implements Comparable<D18P12.Node> {
        int row;
        int column;
        int distance;

        Node(int column, int row, int distance) {
            this.row = row;
            this.column = column;
            this.distance = distance;
        }

        @Override
        public int compareTo(D18P12.Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}
