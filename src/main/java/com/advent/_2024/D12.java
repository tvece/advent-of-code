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

public class D12 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D12.txt");
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
                                group.addMember();
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
        System.out.println(result);
    }

    private static class Group {
        final char character;
        int borders;
        int area;

        Group(char character) {
            this.character = character;
            this.borders = 0;
            this.area = 0;
        }

        void addMember() {
            this.area++;
        }

        void addBorder() {
            this.borders++;
        }
    }
}
