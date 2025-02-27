package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class D15P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D15.txt");
        List<char[]> map = new ArrayList<>();
        AtomicBoolean readingMap = new AtomicBoolean(true);
        StringBuilder instructionsBuilder = new StringBuilder();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                if (line.isEmpty()) {
                    readingMap.set(false);
                } else if (readingMap.get()) {

                    line = line.replaceAll("#", "##")
                            .replaceAll("O", "[]")
                            .replaceAll("\\.", "..")
                            .replaceAll("@", "@.");


                    map.add(line.toCharArray());
                } else {
                    instructionsBuilder.append(line);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        String instructions = instructionsBuilder.toString();
        int[] submarine = new int[]{};
        find:
        for (int row = 0; row < map.size(); row++) {
            for (int column = 0; column < map.getFirst().length; column++) {
                if (map.get(row)[column] == '@') {
                    submarine = new int[]{row, column};
                    break find;
                }
            }
        }
        if (submarine.length == 0) {
            throw new RuntimeException("Submarine not found");
        }

        int lastInstruction = '?';
        boolean wasLastInstructionValid = false;
        for (int i = 0; i < instructions.length(); i++) {
            char instruction = instructions.charAt(i);
            if (lastInstruction == instruction && !wasLastInstructionValid) {
                continue;
            }

            int rowDirection = 0;
            int columnDirection = 0;
            switch (instruction) {
                case '^':
                    rowDirection = -1;
                    break;
                case '>':
                    columnDirection = 1;
                    break;
                case 'v':
                    rowDirection = 1;
                    break;
                case '<':
                    columnDirection = -1;
                    break;
                default:
                    throw new RuntimeException("Unsupported instruction '" + instructions + "'");
            }

            boolean isValid = isInstructionValid(map, instruction, submarine, rowDirection, columnDirection);
            if (isValid) {
                submarine = moveCharacter(map, '@', submarine, rowDirection, columnDirection, true);
            }
            lastInstruction = instruction;
            wasLastInstructionValid = isValid;
        }

        int gps = 0;
        for (int row = 0; row < map.size(); row++) {
            for (int column = 0; column < map.getFirst().length; column++) {
                if (map.get(row)[column] == '[') {
                    gps += ((100 * row) + column);
                }
            }
        }
        System.out.println(gps);
    }

    private static boolean isInstructionValid(List<char[]> map, char instruction, int[] scout, int rowDirection, int columnDirection) {
        scout = scout.clone();
        while (true) {
            scout[0] += rowDirection;
            scout[1] += columnDirection;
            char currentChar = map.get(scout[0])[scout[1]];
            if ((instruction == '^' || instruction == 'v') && (currentChar == '[' || currentChar == ']')) {
                int[] nextScout = scout.clone();
                if (currentChar == '[') {
                    nextScout[1]++;
                } else {
                    nextScout[1]--;
                }

                if (!isInstructionValid(map, instruction, nextScout, rowDirection, columnDirection)) {
                    return false;
                }
            }
            if (currentChar == '.') {
                return true;
            } else if (currentChar == '#') {
                return false;
            }
        }
    }

    private static int[] moveCharacter(List<char[]> map, char character, int[] location, int rowDirection, int columnDirection, boolean isFirst) {
        if (character == '.' || character == '#') {
            return location;
        }
        if (isFirst) {
            map.get(location[0])[location[1]] = '.';
        }

        location[0] = location[0] + rowDirection;
        location[1] = location[1] + columnDirection;
        int[] result = location.clone();
        char nextCharacter = map.get(location[0])[location[1]];
        map.get(location[0])[location[1]] = character;
        moveCharacter(map, nextCharacter, location, rowDirection, columnDirection, false);

        if (rowDirection != 0) {
            if (nextCharacter == '[') {
                int[] nextSideLocation = result.clone();
                nextSideLocation[1]++;
                moveCharacter(map, ']', nextSideLocation, rowDirection, columnDirection, true);
            } else if (nextCharacter == ']') {
                int[] nextSideLocation = result.clone();
                nextSideLocation[1]--;
                moveCharacter(map, '[', nextSideLocation, rowDirection, columnDirection, true);
            }
        }

        return result;
    }
}
