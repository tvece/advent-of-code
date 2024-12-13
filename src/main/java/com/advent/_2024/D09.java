package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class D09 {
    //TODO: fix part 1
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D09.txt");
        String input;
        try {
            input = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        StringBuilder diskBuilder = new StringBuilder();
        boolean isSpace = false;
        int fileCounter = 0;
        for (char character : input.toCharArray()) {
            int number = Character.getNumericValue(character);
            if (isSpace) {
                fileCounter++;
            }
            for (int i = 0; i < number; i++) {
                if (isSpace) {
                    diskBuilder.append(".");
                } else {
                    diskBuilder.append(fileCounter);
                }
            }
            isSpace = !isSpace;
        }
        char[] disk = diskBuilder.toString().toCharArray();

        int currentIndex = 0;
        int stopIndex = disk.length - 1;
        while (true) {
            if (disk[currentIndex] == '.') {
                boolean foundReplacement = false;
                for (int replacementIndex = stopIndex; replacementIndex > currentIndex; replacementIndex--) {
                    if (disk[replacementIndex] != '.') {
                        char temp = disk[currentIndex];
                        disk[currentIndex] = disk[replacementIndex];
                        disk[replacementIndex] = temp;
                        foundReplacement = true;
                        break;
                    }
                }
                if (!foundReplacement) {
                    break;
                } else {
                    stopIndex--;
                }
            }
            currentIndex++;
        }


        long hash = 0;
        int index = 0;
        while (disk[index] != '.') {
            hash += ((long) index * Character.getNumericValue(disk[index]));
            index++;
        }
        System.out.println(disk);
        System.out.printf("%d", hash);


    }

}
