package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2023/day/15
public class D15P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D15.txt");
        String row;
        try {
            row = Files.readAllLines(filePath, StandardCharsets.UTF_8).getFirst();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        List<List<String>> boxes = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            boxes.add(new ArrayList<>());
        }

        String[] steps = row.split(",");
        for (String step : steps) {
            if (step.contains("-")) {
                String lensId = step.substring(0, step.length() - 1);
                List<String> box = boxes.get(getHash(lensId));
                box.stream().filter(lens -> lens.startsWith(lensId)).findFirst().ifPresent(box::remove);
            } else {
                String lensId = step.substring(0, step.indexOf("="));
                List<String> box = boxes.get(getHash(lensId));
                String existingLens = box.stream().filter(lens -> lens.startsWith(lensId)).findFirst().orElse(null);
                if (existingLens == null) {
                    box.add(step);
                } else {
                    box.set(box.indexOf(existingLens), step);
                }
            }
        }

        int result = 0;
        for (int i = 0; i < 256; i++) {
            List<String> box = boxes.get(i);
            for (int j = 0; j < box.size(); j++) {
                String lens = box.get(j);
                int focalLength = Integer.parseInt(lens.substring(lens.indexOf("=") + 1));
                result += (i + 1) * (j + 1) * focalLength;
            }
        }
        System.out.println(result);
    }

    private static int getHash(String step) {
        int stepResult = 0;
        for (char character : step.toCharArray()) {
            stepResult += character;
            stepResult = stepResult * 17;
            stepResult = stepResult % 256;
        }
        return stepResult;
    }
}