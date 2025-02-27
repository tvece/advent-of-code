package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D09P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D09.txt");
        String input;
        try {
            input = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        List<Integer> disk = new ArrayList<>();
        boolean isSpace = false;
        int fileCounter = 0;
        for (char character : input.toCharArray()) {
            int size = Character.getNumericValue(character);
            for (int i = 0; i < size; i++) {
                if (isSpace) {
                    disk.add(-1);
                } else {
                    disk.add(fileCounter);
                }
            }
            if (isSpace) {
                fileCounter++;
            }
            isSpace = !isSpace;
        }

        int spaceIndex = 0;
        int currentIndex = disk.size() - 1;

        while (true) {
            int currentPart = disk.get(currentIndex);
            if (currentPart == -1) {
                currentIndex--;
                continue;
            }
            while (disk.get(currentIndex) == -1) {
                currentIndex--;
            }

            while (disk.get(spaceIndex) != -1) {
                spaceIndex++;
            }

            if (spaceIndex > currentIndex) {
                break;
            }

            disk.set(spaceIndex, currentPart);
            disk.set(currentIndex, -1);
        }

        long hash = 0;
        for (int i = 0; i < disk.size(); i++) {
            int part = disk.get(i);
            if (part != -1) {
                hash += (long) i * part;
            }
        }
        System.out.printf("%d", hash);
    }
}
