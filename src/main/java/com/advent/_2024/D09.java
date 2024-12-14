package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D09 {
    //TODO: part 2
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D09.txt");
        String input;
        try {
            input = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        System.out.println(input.length());
        List<Integer> disk = new ArrayList<>();
        boolean isSpace = false;
        int fileCounter = 0;
        for (char character : input.toCharArray()) {
            int number = Character.getNumericValue(character);
            if (isSpace) {
                fileCounter++;
            }
            for (int i = 0; i < number; i++) {
                if (isSpace) {
                    disk.add(null);
                } else {
                    disk.add(fileCounter);
                }
            }
            isSpace = !isSpace;
        }

        int currentIndex = 0;
        int stopIndex = disk.size() - 1;
        while (true) {
            if (disk.get(currentIndex) == null) {
                boolean foundReplacement = false;
                for (int replacementIndex = stopIndex; replacementIndex > currentIndex; replacementIndex--) {
                    if (disk.get(replacementIndex) != null) {
                        Integer temp = disk.get(currentIndex);
                        disk.set(currentIndex, disk.get(replacementIndex));
                        disk.set(replacementIndex, temp);
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
        while (disk.get(index) != null) {
            long hashPart = ((long) index * disk.get(index));
            hash += hashPart;
            index++;
        }
        System.out.printf("%d", hash);


    }

}
