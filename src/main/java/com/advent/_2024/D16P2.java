package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D16P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D16.txt");
        List<String> map;
        try {
            map = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int startRow = 0;
        int startColumn = 0;
        startSearch:
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().length(); j++) {
                if (map.get(i).charAt(j) == 'S') {
                    startRow = i;
                    startColumn = j;
                    break startSearch;
                }
            }
        }
        List<Node> paths = getShortestPaths(map, startRow, startColumn);
        Set<PathElement> uniquePathElements = new HashSet<>();
        paths.forEach(path -> uniquePathElements.addAll(path.path));
        System.out.println(uniquePathElements.size());
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().length(); j++) {
                if (uniquePathElements.contains(new PathElement(i, j))) {
                    System.out.print("O");
                } else {
                    System.out.print(map.get(i).charAt(j));
                }
            }
            System.out.println();
        }

    }

    private static List<Node> getShortestPaths(List<String> map, int startRow, int startColumn) {

        int[][][] distances = new int[map.size()][map.getFirst().length()][4];
        for (int[][] directionCost : distances) {
            for (int[] row : directionCost) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startRow, startColumn, 0, 3, new ArrayList<>()));
        distances[startRow][startColumn][3] = 0;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        List<Node> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            if (map.get(currentNode.row).charAt(currentNode.column) == 'E') {
                result.add(currentNode);
            }

            for (int i = 0; i < 4; i++) {
                int nextRow = currentNode.row + dr[i];
                int nextColumn = currentNode.column + dc[i];

                if (map.get(nextRow).charAt(nextColumn) == '#') continue; // Skip walls

                int turnCost = (i == currentNode.direction) ? 1 : 1001; // Cost for turning or keeping direction
                int newDistance = currentNode.distance + turnCost;

                if (newDistance <= distances[nextRow][nextColumn][i]) {
                    distances[nextRow][nextColumn][i] = newDistance;
                    pq.add(new Node(nextRow, nextColumn, newDistance, i, currentNode.path));
                }
            }
        }
        return result;
    }

    private static class Node implements Comparable<Node> {
        int row;
        int column;
        int distance;
        int direction;
        List<PathElement> path;

        Node(int row, int column, int distance, int direction, List<PathElement> currentPath) {
            this.row = row;
            this.column = column;
            this.distance = distance;
            this.direction = direction;
            path = new ArrayList<>(currentPath);
            path.add(new PathElement(row, column));
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    record PathElement(int row, int column) {
    }
}
