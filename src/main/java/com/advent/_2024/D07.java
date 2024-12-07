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

//TODO: fix part 2
public class D07 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D07.txt");
        List<String> rows;
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
            //System.out.println(correctResult);
        }
        System.out.println(sum);
    }

    private static boolean isValid(long expectedValue, List<Long> values, long currentValue, int nextValueIndex) {
        if (expectedValue == currentValue) {
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
        int magnitude = 0;
        long magnitudeResolver = nextValue;
        while (magnitudeResolver > 0) {
            magnitude++;
            magnitudeResolver = magnitudeResolver / 10;
        }

        System.out.println("   " + currentValue + " || " + nextValue + " = " + ((currentValue * (long) Math.pow((long) 10, magnitude)) + nextValue) + "_" + magnitude);


        return isValid(expectedValue, values, currentValue + nextValue, nextValueIndex)
                || isValid(expectedValue, values, currentValue * nextValue, nextValueIndex)
                || isValid(expectedValue, values, (currentValue * (long) Math.pow((long) 10, magnitude)) + nextValue, nextValueIndex);
    }
}
