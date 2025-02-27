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

// https://adventofcode.com/2024/day/5
public class D05P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D05.txt");
        HashMap<String, List<String>> instructions = new HashMap<>();
        AtomicBoolean readingInstructions = new AtomicBoolean(true);
        List<String[]> correctManuals = new ArrayList<>();
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
                    for (int i = 0; i < manual.length - 1; i++) {
                        if (instructions.containsKey(manual[i])) {
                            List<String> instruction = instructions.get(manual[i]);

                            for (int j = i + 1; j < manual.length; j++) {
                                if (instruction.contains(manual[j])) {
                                    return;
                                }
                            }
                        }
                    }
                    correctManuals.add(manual);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int result = 0;
        for (String[] correctManual : correctManuals) {
            if (correctManual.length % 2 == 0) {
                throw new RuntimeException("Correct manual has even pages! " + Arrays.toString(correctManual));
            }
            result += Integer.parseInt(correctManual[correctManual.length / 2]);
        }
        System.out.println(result);
    }
}
