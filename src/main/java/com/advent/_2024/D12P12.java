package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D12P12 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D12.txt");
        List<String> map;
        try {
            map = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        boolean[][] processed = new boolean[map.size()][map.getFirst().length()];
        List<Group> groups = new ArrayList<>();
        for (int row = 0; row < map.size(); row++) {
            for (int column = 0; column < map.getFirst().length(); column++) {
                if (!processed[row][column]) {
                    char character = map.get(row).charAt(column);
                    Group group = new Group(character);
                    Queue<int[]> queue = new ArrayDeque<>();
                    queue.add(new int[]{row, column});
                    while (!queue.isEmpty()) {
                        int[] currentElement = queue.poll();
                        if (currentElement[0] >= 0 && currentElement[0] < map.size() &&
                                currentElement[1] >= 0 && currentElement[1] < map.getFirst().length() && // in range
                                map.get(currentElement[0]).charAt(currentElement[1]) == group.character) {
                            if (!processed[currentElement[0]][currentElement[1]]) {
                                group.addMember(currentElement[0], currentElement[1]);
                                processed[currentElement[0]][currentElement[1]] = true;
                                queue.add(new int[]{currentElement[0] + 1, currentElement[1]});
                                queue.add(new int[]{currentElement[0] - 1, currentElement[1]});
                                queue.add(new int[]{currentElement[0], currentElement[1] + 1});
                                queue.add(new int[]{currentElement[0], currentElement[1] - 1});
                            }
                        } else {
                            group.addBorder();
                        }
                    }
                    groups.add(group);
                }
            }
        }
        long result = 0;
        for (Group group : groups) {
            result += (long) group.area * group.borders;
        }
        System.out.println("Part 1: " + result);
        long result2 = 0;
        for (Group group : groups) {
            result2 += (long) group.area * group.getSides();
        }
        System.out.println("Part 2: " + result2);
    }

    private static class Group {
        final char character;
        int borders;
        List<Point> points = new ArrayList<>();
        private int area;

        Group(char character) {
            this.character = character;
            this.borders = 0;
            this.area = 0;
        }

        void addMember(int row, int column) {
            this.area++;
            points.add(new Point(row, column));
        }

        void addBorder() {
            this.borders++;
        }

        int getSides() {
            int sides = 0;
            Map<Integer, List<Point>> rows = new HashMap<>();
            Map<Integer, List<Point>> columns = new HashMap<>();
            this.points.forEach(point -> {
                if (rows.containsKey(point.row)) {
                    rows.get(point.row).add(point);
                } else {
                    rows.put(point.row, new ArrayList<>(Arrays.asList(new Point[]{point})));
                }
                if (columns.containsKey(point.column)) {
                    columns.get(point.column).add(point);
                } else {
                    columns.put(point.column, new ArrayList<>(Arrays.asList(new Point[]{point})));
                }
            });
            rows.forEach((id, row) -> Collections.sort(row));
            columns.forEach((id, column) -> Collections.sort(column));

            //top
            for (Integer key : rows.keySet()) {
                List<Point> row = rows.get(key);
                Set<Point> sideIndicators = new HashSet<>();
                for (Point point : row) {
                    if (!points.contains(new Point(point.row - 1, point.column))) {
                        if (!sideIndicators.contains(new Point(point.row, point.column - 1))) {
                            sides++;
                        }
                        sideIndicators.add(point);
                    }
                }
            }

            //bottom
            for (Integer key : rows.keySet()) {
                List<Point> row = rows.get(key);
                Set<Point> sideIndicators = new HashSet<>();
                for (Point point : row) {
                    if (!points.contains(new Point(point.row + 1, point.column))) {
                        if (!sideIndicators.contains(new Point(point.row, point.column - 1))) {
                            sides++;
                        }
                        sideIndicators.add(point);
                    }
                }
            }

            //left
            for (Integer key : columns.keySet()) {
                List<Point> column = columns.get(key);
                Set<Point> sideIndicators = new HashSet<>();
                for (Point point : column) {
                    if (!points.contains(new Point(point.row, point.column - 1))) {
                        if (!sideIndicators.contains(new Point(point.row - 1, point.column))) {
                            sides++;
                        }
                        sideIndicators.add(point);
                    }
                }
            }

            //right
            for (Integer key : columns.keySet()) {
                List<Point> column = columns.get(key);
                Set<Point> sideIndicators = new HashSet<>();
                for (Point point : column) {
                    if (!points.contains(new Point(point.row, point.column + 1))) {
                        if (!sideIndicators.contains(new Point(point.row - 1, point.column))) {
                            sides++;
                        }
                        sideIndicators.add(point);
                    }
                }
            }
            return sides;
        }
    }

    private static class Point implements Comparable<Point> {
        int row, column;

        Point(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int compareTo(Point p) {
            return (this.column != p.column) ? this.column - p.column : this.row - p.row;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + column + ")";
        }

        @Override
        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = prime * result + row;
            result = prime * result + column;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true; // Check if the same reference
            if (obj == null || getClass() != obj.getClass()) return false; // Null or different class
            Point other = (Point) obj; // Cast to Point
            return this.column == other.column && this.row == other.row; // Compare fields
        }
    }
}
