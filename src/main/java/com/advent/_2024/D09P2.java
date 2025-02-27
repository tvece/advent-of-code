package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2024/day/9
public class D09P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D09.txt");
        String input;
        try {
            input = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        List<File> disk = new ArrayList<>();
        boolean isSpace = false;
        int fileCounter = 0;
        for (char character : input.toCharArray()) {
            int size = Character.getNumericValue(character);
            if (isSpace) {
                fileCounter++;
            }
            disk.add(new File(size, fileCounter, isSpace));
            isSpace = !isSpace;
        }

        int currentIndex = disk.size() - 1;
        while (currentIndex >= 0) {
            File currentFile = disk.get(currentIndex);
            if (!currentFile.isSpace) {
                int spaceFinder = 0;
                while (spaceFinder < currentIndex) {
                    File finderFile = disk.get(spaceFinder);
                    if (finderFile.isSpace) {
                        if (finderFile.size >= currentFile.size) {
                            finderFile.size = finderFile.size - currentFile.size;
                            disk.remove(currentIndex);
                            disk.add(spaceFinder, currentFile);
                            File newSpace = new File(currentFile.size, 0, true);
                            disk.add(currentIndex, newSpace);
                            break;
                        }
                    }
                    spaceFinder++;
                }
            }
            currentIndex--;
        }

        long hash = 0;
        int index = 0;
        for (File file : disk) {
            if (!file.isSpace) {
                for (int part = 0; part < file.size; part++) {
                    hash += ((long) (index + part) * file.fileIndex);
                }
            }
            index += file.size;
        }

        System.out.printf("%d", hash);
    }

    private static class File {
        int size;
        int fileIndex;
        boolean isSpace;

        File(int size, int fileIndex, boolean isSpace) {
            this.size = size;
            this.fileIndex = fileIndex;
            this.isSpace = isSpace;
        }
    }

}
