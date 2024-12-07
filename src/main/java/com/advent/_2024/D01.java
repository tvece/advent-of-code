package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D01 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D01.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        Set<Integer> firstColumn = new HashSet<>();
        List<Integer> secondColumn = new ArrayList<>();
        lines.forEach(line -> {
            String[] split = line.split("\\s+");
            firstColumn.add(Integer.valueOf(split[0]));
            secondColumn.add(Integer.valueOf(split[1]));
        });

        Integer[] values = new Integer[firstColumn.size()];
        firstColumn.toArray(values);

        long result = 0;
        for (Integer value : values) {
            result += ((long) value * Collections.frequency(secondColumn, value));
        }

        System.out.printf("%d", result);
    }
}
