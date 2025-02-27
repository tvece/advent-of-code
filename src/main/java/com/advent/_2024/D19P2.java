package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// https://adventofcode.com/2024/day/19
public class D19P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D19.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        // Read the line containing the towel patterns
        // e.g., "r, wr, b, g, bwu, rb, gb, br"
        String patternsLine = input.getFirst();

        // Split on commas to get individual patterns, trimming whitespace
        String[] patternArray = patternsLine.split(", ");
        Set<String> towelPatterns = new HashSet<>(Arrays.asList(patternArray));

        // Now read each subsequent line as a design
        // We will store them in a list to process
        List<String> designs = new ArrayList<>();
        for (int i = 2; i < input.size(); i++) {
            String design = input.get(i);
            designs.add(design);
        }

        // For each design, check if it can be formed by the available towel patterns
        long result = 0;
        for (String design : designs) {
            result += canFormDesign(design, towelPatterns);
        }

        // Output the count of possible designs
        System.out.println("Result: " + result);
    }

    // Helper method to check if a design can be formed by concatenating patterns in towelPatterns
    private static long canFormDesign(String design, Set<String> towelPatterns) {
        int n = design.length();
        long[] dp = new long[n + 1];
        dp[0] = 1; // Empty prefix is always valid.

        // Go through each position in the design.
        for (int i = 0; i < n; i++) {
            // Only proceed if the prefix up to i is formable.
            if (dp[i] > 0) {
                // Try to place each pattern starting at index i.
                for (String pattern : towelPatterns) {
                    int end = i + pattern.length();
                    // Check boundary and if substring matches the pattern.
                    if (end <= n && design.substring(i, end).equals(pattern)) {
                        dp[end] += dp[i];
                    }
                }
            }
        }

        // If dp[n] is true, we can form the entire design.
        return dp[n];
    }
}
