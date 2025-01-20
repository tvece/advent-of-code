package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class D25P1 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D25.txt");
        List<int[]> keys = new ArrayList<>();
        List<int[]> locks = new ArrayList<>();
        int[] defaultSizes = new int[]{0, 0, 0, 0, 0};
        int[] sizes = defaultSizes.clone();

        boolean isKey = false;
        boolean isReadingFirstLine = true;
        int objectRowIndex = 0;
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
                String line = it.next();
                if (line.isEmpty()) {
                    if (isKey) {
                        keys.add(sizes);
                    } else {
                        locks.add(sizes);
                    }
                    sizes = defaultSizes.clone();
                    isReadingFirstLine = true;
                } else if (isReadingFirstLine) {
                    isKey = line.contains(".");
                    isReadingFirstLine = false;
                } else if (objectRowIndex == 5) {
                    objectRowIndex = 0;
                    isReadingFirstLine = true;
                } else {
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) == '#') {
                            sizes[i]++;
                        }
                    }
                    objectRowIndex++;
                }
            }
            if (isKey) {
                keys.add(sizes);
            } else {
                locks.add(sizes);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        int result = 0;
        for (int[] key : keys) {
            for (int[] lock : locks) {
                if (key[0] + lock[0] <= 5 &&
                        key[1] + lock[1] <= 5 &&
                        key[2] + lock[2] <= 5 &&
                        key[3] + lock[3] <= 5 &&
                        key[4] + lock[4] <= 5) {
                    result++;
                }
            }
        }

        System.out.println("result: " + result);
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
