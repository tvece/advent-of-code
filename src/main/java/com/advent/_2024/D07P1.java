package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D07P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D07.txt");
        List<Long> correctResults = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                String[] split = line.split(": ");
                long expectedValue = Long.parseLong(split[0]);
                String[] inputStrings = split[1].split(" ");
                List<Long> inputValues = Arrays.stream(inputStrings).map(Long::parseLong).collect(Collectors.toCollection((ArrayList::new)));
                if (isValid(expectedValue, inputValues, inputValues.getFirst(), 1)) {
                    correctResults.add(expectedValue);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        long sum = 0;
        for (long correctResult : correctResults) {
            sum += correctResult;
        }
        System.out.println(sum);
    }

    private static boolean isValid(long expectedValue, List<Long> values, long currentValue, int nextValueIndex) {
        if (nextValueIndex == values.size() && expectedValue == currentValue) {
            return true;
        }
        if (expectedValue < currentValue) {
            return false;
        }
        if (nextValueIndex == values.size()) {
            return false;
        }
        long nextValue = values.get(nextValueIndex);
        nextValueIndex++;

        return isValid(expectedValue, values, currentValue + nextValue, nextValueIndex)
                || isValid(expectedValue, values, currentValue * nextValue, nextValueIndex);
    }
}
