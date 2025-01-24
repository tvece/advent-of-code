package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D01P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D01.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        List<Integer> firstColumn = new ArrayList<>();
        List<Integer> secondColumn = new ArrayList<>();
        lines.forEach(line -> {
            String[] split = line.split("\\s+");
            firstColumn.add(Integer.valueOf(split[0]));
            secondColumn.add(Integer.valueOf(split[1]));
        });

        Collections.sort(firstColumn);
        Collections.sort(secondColumn);

        long result = 0;
        for (int i = 0; i < firstColumn.size(); i++) {
            result += Math.abs(firstColumn.get(i) - secondColumn.get(i));
        }

        System.out.printf("%d", result);
    }
}
