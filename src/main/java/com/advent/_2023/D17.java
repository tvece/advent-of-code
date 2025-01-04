package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class D17 {

    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("src/main/resources/2023/D17.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        int[][] map = new int[rows.size()][];

        for (int i = 0; i < rows.size(); i++) {
            map[i] = new int[rows.getFirst().length()];
            for (int j = 0; j < rows.getFirst().length(); j++) {
                map[i][j] = Integer.parseInt(String.valueOf(rows.get(i).charAt(j)));
            }
        }

        int[][][][] distances = new int[map.length][map[0].length][4][11];
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                for (int d = 0; d < 4; d++) {
                    Arrays.fill(distances[r][c][d], Integer.MAX_VALUE / 2);
                }
            }
        }


        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 0, 0, 0, 'S', "[0,0]"));

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            if (currentNode.row == map.length - 1 && currentNode.column == map[0].length - 1) {
                System.out.println();
                for (int row = 0; row < map.length; row++) {
                    for (int column = 0; column < map[0].length; column++) {
                        System.out.print(map[row][column]);
                    }
                    System.out.println();
                }
                System.out.println();

                System.out.println();
                for (int row = 0; row < map.length; row++) {
                    for (int column = 0; column < map[0].length; column++) {
                        if (currentNode.path.contains("[" + row + "," + column + "]")) {
                            System.out.print('X');
                        } else {
                            System.out.print(map[row][column]);
                        }
                    }
                    System.out.println();
                }
                System.out.println();
                System.out.println("Shortest path: " + currentNode.distance + currentNode.path);
                return;
            }


            if (currentNode.direction != 'S' && currentNode.distance > distances[currentNode.row][currentNode.column][getDirectionInteger(currentNode.direction)][currentNode.streak]) {
                continue;
            }

            String nextDirections;
            switch (currentNode.direction) {
                case 'S':
                    nextDirections = "RD";
                    break;
                case 'R':
                    nextDirections = currentNode.streak < 10 ? "R" : "";
                    nextDirections += currentNode.streak >= 4 ? "UD" : "";
                    break;
                case 'D':
                    nextDirections = currentNode.streak < 10 ? "D" : "";
                    nextDirections += currentNode.streak >= 4 ? "RL" : "";
                    break;
                case 'L':
                    nextDirections = currentNode.streak < 10 ? "L" : "";
                    nextDirections += currentNode.streak >= 4 ? "UD" : "";
                    break;
                case 'U':
                    nextDirections = currentNode.streak < 10 ? "U" : "";
                    nextDirections += currentNode.streak >= 4 ? "LR" : "";
                    break;
                default:
                    throw new RuntimeException("Unsupported direction" + currentNode.direction);
            }

            for (char nextDirection : nextDirections.toCharArray()) {
                int nextRow;
                int nextColumn;
                int nextDirectionInteger = getDirectionInteger(nextDirection);
                switch (nextDirection) {
                    case 'U':
                        nextRow = currentNode.row - 1;
                        nextColumn = currentNode.column;
                        break;
                    case 'R':
                        nextRow = currentNode.row;
                        nextColumn = currentNode.column + 1;
                        break;
                    case 'D':
                        nextRow = currentNode.row + 1;
                        nextColumn = currentNode.column;
                        break;
                    case 'L':
                        nextRow = currentNode.row;
                        nextColumn = currentNode.column - 1;
                        break;
                    default:
                        throw new RuntimeException("Unsupported direction " + nextDirection);
                }

                if (nextRow < 0 || nextRow == map.length || nextColumn < 0 || nextColumn == map[0].length) {
                    continue; // Skip walls
                }

                int nextStreak = currentNode.direction == nextDirection ? currentNode.streak + 1 : 1;

                int newDistance = currentNode.distance + map[nextRow][nextColumn];

                if (newDistance < distances[nextRow][nextColumn][nextDirectionInteger][nextStreak]) {
                    distances[nextRow][nextColumn][nextDirectionInteger][nextStreak] = newDistance;
                    pq.add(new Node(nextRow, nextColumn, newDistance, nextStreak, nextDirection, currentNode.path + "[" + nextRow + "," + nextColumn + "]"));
                }
            }
        }
    }

    private static class Node implements Comparable<Node> {
        int row;
        int column;
        int distance;
        int streak;
        char direction;
        String path;

        Node(int row, int column, int distance, int streak, char direction, String path) {
            this.row = row;
            this.column = column;
            this.distance = distance;
            this.streak = streak;
            this.direction = direction;
            this.path = path;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    private static int getDirectionInteger(char direction) {
        switch (direction) {
            case 'U':
                return 0;
            case 'R':
                return 1;
            case 'D':
                return 2;
            case 'L':
                return 3;
            default:
                throw new RuntimeException("Unsupported direction " + direction);
        }
    }
}
