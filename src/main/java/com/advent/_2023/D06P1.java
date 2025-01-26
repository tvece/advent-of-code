package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class D06P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2023/D06.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        List<Long> times = Arrays.stream(lines.get(0).substring(5).trim().split("\\s+"))
                .map(Long::parseLong).toList();
        List<Long> distances = Arrays.stream(lines.get(1).substring(9).trim().split("\\s+"))
                .map(Long::parseLong).toList();
        Integer results = 1;
        for (int i = 0; i < times.size(); i++) {
            int validTries = 0;
            Long totalTime = times.get(i);
            for (int windupTime = 1; windupTime < totalTime; windupTime++) {
                if (((totalTime - windupTime) * windupTime) > distances.get(i)) {
                    validTries++;
                }
            }
            results = results * validTries;
        }
        System.out.println(results);
    }
}