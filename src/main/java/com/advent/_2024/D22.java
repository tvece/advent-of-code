package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D22 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D22.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        long result = 0;

        for (String line : input) {
            long secretNumber = Integer.parseInt(line);
            for (int i = 0; i < 2000; i++) {
                secretNumber = evolve(secretNumber);
            }
            System.out.println(line + " : " + secretNumber);
            result += secretNumber;
        }

        System.out.println(result);
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
