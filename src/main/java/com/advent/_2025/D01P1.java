package com.advent._2025;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// https://adventofcode.com/2025/day/1
public class D01P1 {
    final static int INIT_POSITION = 50;

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2025/D01.txt");
        AtomicInteger position = new AtomicInteger(INIT_POSITION);
        AtomicInteger result = new AtomicInteger(0);
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                char direction = line.charAt(0);
                int clicks = Integer.parseInt(line.substring(1));
                position.set(switch (direction) {
                    case ('L') -> (position.get() - clicks) % 100;
                    case ('R') -> (position.get() + clicks) % 100;
                    default -> throw new RuntimeException("Invalid direction: " + direction);
                });
                if (position.get() == 0) {
                    result.incrementAndGet();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        System.out.println(result.get());
    }
}