package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D11 {
    //TODO part 2
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D11.txt");
        String input;
        try {
            input = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        List<Long> result = new ArrayList<>();
        String[] parts = input.split(" ");
        for (String part : parts) {
            result.add(Long.parseLong(part));
        }
        int cycles = 25;
        for (int i = 0; i < cycles; i++) {
            System.out.println(i);
            List<Long> cycle = new ArrayList<>();
            for (Long stone : result) {
                if (stone == 0) {
                    cycle.add(1L);
                } else {
                    List<Integer> numbers = new ArrayList<>();
                    long parser = stone;
                    while (true) {
                        numbers.add((int) (parser % 10));
                        if (parser < 10) {
                            break;
                        }
                        parser = parser / 10;
                    }
                    if (numbers.size() % 2 == 0) {
                        numbers = numbers.reversed();
                        long stoneA = 0L;
                        for (int firstNumberIndex = 0; firstNumberIndex < (numbers.size() / 2); firstNumberIndex++) {
                            stoneA += (long) Math.pow(10, ((double) numbers.size() / 2) - firstNumberIndex - 1) * numbers.get(firstNumberIndex);
                        }
                        long stoneB = 0L;
                        for (int secondNumberIndex = numbers.size() / 2; secondNumberIndex < numbers.size(); secondNumberIndex++) {
                            stoneB += (long) Math.pow(10, ((double) numbers.size()) - secondNumberIndex - 1) * numbers.get(secondNumberIndex);
                        }
                        cycle.add(stoneA);
                        cycle.add(stoneB);
                    } else {
                        cycle.add(stone * 2024);
                    }

                }
            }
            result = new ArrayList<>(cycle);
        }
        System.out.println(result.size());
    }
}
