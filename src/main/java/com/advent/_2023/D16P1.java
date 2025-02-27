package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// https://adventofcode.com/2023/day/16
public class D16P1 {

    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("../advent-of-code-input/2023/D16.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        char[][] map = new char[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            map[i] = rows.get(i).toCharArray();
        }

        Set<String> energized = new HashSet<>();
        Queue<Step> steps = new LinkedList<>();
        steps.add(new Step(0, 0, 'E'));
        while (!steps.isEmpty()) {
            Step step = steps.poll();
            if (step.row < 0 || step.column < 0 || step.row > map.length - 1 || step.column > map[0].length - 1) {
                continue;
            }
            String key = step.row + "," + step.column + ";" + step.direction;
            if (energized.contains(key)) {
                continue;
            }

            energized.add(key);

            char[] nextDirections = new char[2];
            char currentChar = map[step.row][step.column];

            if (currentChar == '.' || (currentChar == '-' && (step.direction == 'W' || step.direction == 'E'))
                    || (currentChar == '|' && (step.direction == 'N' || step.direction == 'S'))) {
                switch (step.direction) {
                    case 'N':
                        nextDirections[0] = 'N';
                        break;
                    case 'E':
                        nextDirections[0] = 'E';
                        break;
                    case 'S':
                        nextDirections[0] = 'S';
                        break;
                    case 'W':
                        nextDirections[0] = 'W';
                        break;
                }
            } else if (currentChar == '/') {
                switch (step.direction) {
                    case 'N':
                        nextDirections[0] = 'E';
                        break;
                    case 'E':
                        nextDirections[0] = 'N';
                        break;
                    case 'S':
                        nextDirections[0] = 'W';
                        break;
                    case 'W':
                        nextDirections[0] = 'S';
                        break;
                }
            } else if (currentChar == '\\') {
                switch (step.direction) {
                    case 'N':
                        nextDirections[0] = 'W';
                        break;
                    case 'E':
                        nextDirections[0] = 'S';
                        break;
                    case 'S':
                        nextDirections[0] = 'E';
                        break;
                    case 'W':
                        nextDirections[0] = 'N';
                        break;
                }
            } else if (currentChar == '|') {
                switch (step.direction) {
                    case 'W':
                        nextDirections[0] = 'N';
                        nextDirections[1] = 'S';
                        break;
                    case 'E':
                        nextDirections[0] = 'N';
                        nextDirections[1] = 'S';
                        break;
                }
            } else if (currentChar == '-') {
                switch (step.direction) {
                    case 'N':
                        nextDirections[0] = 'W';
                        nextDirections[1] = 'E';
                        break;
                    case 'S':
                        nextDirections[0] = 'W';
                        nextDirections[1] = 'E';
                        break;
                }
            }

            for (char nextDirection : nextDirections) {
                switch (nextDirection) {
                    case 'N':
                        steps.add(new Step(step.row - 1, step.column, 'N'));
                        break;
                    case 'E':
                        steps.add(new Step(step.row, step.column + 1, 'E'));
                        break;
                    case 'S':
                        steps.add(new Step(step.row + 1, step.column, 'S'));
                        break;
                    case 'W':
                        steps.add(new Step(step.row, step.column - 1, 'W'));
                        break;
                }
            }
        }

        System.out.println(energized.stream().map(element -> element.substring(0, element.indexOf(';'))).distinct().count());

    }

    private record Step(int row, int column, char direction) {
    }
}