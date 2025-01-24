package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class D05P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D05.txt");
        HashMap<String, List<String>> instructions = new HashMap<>();
        AtomicBoolean readingInstructions = new AtomicBoolean(true);
        List<String[]> correctedManuals = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                if (line.isEmpty()) {
                    readingInstructions.set(false);
                } else if (readingInstructions.get()) {
                    String[] stringInstruction = line.split("\\|");
                    if (instructions.containsKey(stringInstruction[1])) {
                        instructions.get(stringInstruction[1]).add(stringInstruction[0]);
                    } else {
                        List<String> instruction = new ArrayList<>();
                        instruction.add(stringInstruction[0]);
                        instructions.put(stringInstruction[1], instruction);
                    }
                } else {
                    String[] manual = line.split(",");
                    boolean foundError = false;
                    for (int i = 0; i < manual.length - 1; i++) {
                        if (instructions.containsKey(manual[i])) {
                            List<String> instruction = instructions.get(manual[i]);
                            int lastWrongIndex = 0;
                            boolean requiresSwap = false;
                            for (int j = i + 1; j < manual.length; j++) {
                                if (instruction.contains(manual[j])) {
                                    foundError = true;
                                    lastWrongIndex = j;
                                    requiresSwap = true;
                                }
                            }
                            if (requiresSwap) {
                                String temp = manual[i];
                                manual[i] = manual[lastWrongIndex];
                                manual[lastWrongIndex] = temp;
                                i--;
                            }
                        }
                    }
                    if (foundError) {
                        correctedManuals.add(manual);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int result = 0;
        for (String[] correctedManual : correctedManuals) {
            if (correctedManual.length % 2 == 0) {
                throw new RuntimeException("Corrected manual has even pages! " + Arrays.toString(correctedManual));
            }
            result += Integer.parseInt(correctedManual[correctedManual.length / 2]);
        }
        System.out.println(result);
    }
}
