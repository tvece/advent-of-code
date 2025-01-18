package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class D23P1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D23.txt");
        List<String> tPairs = new ArrayList<>();
        List<String> input = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                input.add(line);
                String[] split = line.split("-");
                if (split[0].startsWith("t") || split[1].startsWith("t")) {
                    tPairs.add(line);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        
        List<String> foundGroups = new ArrayList<>();
        for (String tPair : tPairs) {
            String[] split = tPair.split("-");
            String firstComputer = split[0];
            String secondComputer = split[1];
            for (String line : input) {
                if (line.startsWith(firstComputer) || line.endsWith(firstComputer)) {
                    String thirdComputer = line.startsWith(firstComputer) ? line.split("-")[1] : line.split("-")[0];
                    String[] groupArray = new String[]{firstComputer, secondComputer, thirdComputer};
                    Arrays.sort(groupArray);
                    String group = String.join("", groupArray);
                    if (input.contains(secondComputer + "-" + thirdComputer) || input.contains(thirdComputer + "-" + secondComputer)) {
                        if (!foundGroups.contains(group)) {
                            foundGroups.add(group);
                        }
                    }
                }
            }
        }

        System.out.println("result: " + foundGroups.size());
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
