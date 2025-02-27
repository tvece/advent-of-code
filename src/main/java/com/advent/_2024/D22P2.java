package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D22P2 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("../advent-of-code-input/2024/D22.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        HashMap<String, Integer> sequences = new HashMap<>();
        for (String line : input) {
            long secretNumber = Integer.parseInt(line);
            Queue<Long> diffQueue = new LinkedList<>();
            long previousValue = secretNumber % 10;
            Set<String> processedSequences = new HashSet<>();
            for (int i = 0; i < 2000; i++) {
                secretNumber = evolve(secretNumber);
                diffQueue.add(secretNumber % 10 - previousValue);

                if (i > 2) {
                    StringBuilder sequenceBuilder = new StringBuilder();
                    for (Long diff : diffQueue) {
                        sequenceBuilder.append(diff);
                        sequenceBuilder.append(";");
                    }
                    String sequence = sequenceBuilder.toString();
                    if (!processedSequences.contains(sequence)) {
                        processedSequences.add(sequence);
                        if (sequences.containsKey(sequence)) {
                            sequences.put(sequence, (int) (sequences.get(sequence) + secretNumber % 10));
                        } else {
                            sequences.put(sequence, (int) (secretNumber % 10));
                        }
                    }
                    diffQueue.poll();
                }
                previousValue = secretNumber % 10;
            }
        }

        int best = 0;
        for (String key : sequences.keySet()) {
            int value = sequences.get(key);
            if (value > best) {
                best = value;
            }
        }

        System.out.println("result: " + best);
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static long evolve(long number) {
        long result = prune(mix(number, number * 64));
        result = prune(mix(result, (result / 32)));
        return prune(mix(result, (result * 2048)));
    }

    private static long mix(long secret, long number) {
        return number ^ secret;
    }

    private static long prune(long secret) {
        return secret % 16777216;
    }
}