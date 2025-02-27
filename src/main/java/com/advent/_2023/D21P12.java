package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// https://www.youtube.com/watch?v=9UOMZSL0JTg
public class D21P12 {

    static int TARGET_STEPS_1 = 64;
    static int TARGET_STEPS_2 = 26501365;

    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("../advent-of-code-input/2023/D21.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        char[][] map = new char[rows.size()][];
        int[] start = new int[2];
        for (int i = 0; i < rows.size(); i++) {
            String row = rows.get(i);
            map[i] = row.toCharArray();
            int indexOfS = row.indexOf("S");
            if (indexOfS != -1) {
                start = new int[]{i, indexOfS};
            }
        }

        System.out.println("Part1: " + getPlots(start, TARGET_STEPS_1, map));

        int gridWidth = (TARGET_STEPS_2 / map.length) - 1;

        double odd = Math.pow((gridWidth / 2) * 2 + 1, 2);
        double even = Math.pow(((gridWidth + 1) / 2) * 2, 2);


        int oddPoints = getPlots(start, (map[0].length * 2) + 1, map);
        int evenPoints = getPlots(start, map[0].length * 2, map);

        int cornerT = getPlots(new int[]{map.length - 1, start[1]}, map.length - 1, map);
        int cornerR = getPlots(new int[]{start[0], 0}, map.length - 1, map);
        int cornerB = getPlots(new int[]{0, start[1]}, map.length - 1, map);
        int cornerL = getPlots(new int[]{start[0], map.length - 1}, map.length - 1, map);

        int smallTopRight = getPlots(new int[]{map.length - 1, 0}, map.length / 2 - 1, map);
        int smallTopLeft = getPlots(new int[]{map.length - 1, map.length - 1}, map.length / 2 - 1, map);
        int smallBottomRight = getPlots(new int[]{0, 0}, map.length / 2 - 1, map);
        int smallBottomLeft = getPlots(new int[]{0, map.length - 1}, map.length / 2 - 1, map);

        int largeTopRight = getPlots(new int[]{map.length - 1, 0}, map.length * 3 / 2 - 1, map);
        int largeTopLeft = getPlots(new int[]{map.length - 1, map.length - 1}, map.length * 3 / 2 - 1, map);
        int largeBottomRight = getPlots(new int[]{0, 0}, map.length * 3 / 2 - 1, map);
        int largeBottomLeft = getPlots(new int[]{0, map.length - 1}, map.length * 3 / 2 - 1, map);


        long result = (long) (odd * oddPoints +
                even * evenPoints) +
                (cornerT + cornerR + cornerB + cornerL) +
                (long) (gridWidth + 1) * (smallTopRight + smallTopLeft + smallBottomRight + smallBottomLeft) +
                (long) gridWidth * (largeTopRight + largeTopLeft + largeBottomRight + largeBottomLeft);

        System.out.println("Part 2: " + result);

    }

    private static int getPlots(int[] startIndex, int targetSteps, char[][] map) {
        Set<String> result = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Step startStep = new Step(startIndex, targetSteps);
        visited.add(startStep.index[0] + ";" + startStep.index[1]);
        Deque<Step> queue = new ArrayDeque<>();
        queue.add(startStep);
        while (!queue.isEmpty()) {
            Step step = queue.poll();

            if (step.stepsRemaining % 2 == 0) {
                result.add(step.index[0] + ";" + step.index[1]);
            }
            if (step.stepsRemaining == 0) {
                continue;
            }

            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

            for (int[] direction : directions) {
                int nr = step.index[0] + direction[0];
                int nc = step.index[1] + direction[1];
                if (nr < 0 || nr >= map.length || nc < 0 || nc >= map[0].length || map[nr][nc] == '#' || visited.contains(nr + ";" + nc)) {
                    continue;
                }
                visited.add(nr + ";" + nc);
                queue.add(new Step(new int[]{nr, nc}, step.stepsRemaining - 1));
            }
        }
        return result.size();
    }

    private record Step(int[] index, int stepsRemaining) {
    }
}