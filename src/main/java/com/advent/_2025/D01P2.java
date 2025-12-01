package com.advent._2025;

import com.sun.source.tree.ContinueTree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// https://adventofcode.com/2025/day/1
public class D01P2 {
    final static int INIT_POSITION = 50;

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2025/D01.txt");
        AtomicInteger position = new AtomicInteger(INIT_POSITION);
        AtomicInteger result = new AtomicInteger(0);
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                char direction = line.charAt(0);
                int clicks = Integer.parseInt(line.substring(1));
                if (clicks == 0) {
                    return;
                }
                int newPosition = switch (direction) {
                    case ('L') -> (position.get() - clicks);
                    case ('R') -> (position.get() + clicks);
                    default -> throw new RuntimeException("Invalid direction: " + direction);
                };

                boolean differentSign = (position.get() * newPosition) < 0;
                if (differentSign || newPosition == 0) {
                    result.incrementAndGet();
                }

                result.set(result.get() + Math.abs(newPosition / 100));
                position.set(newPosition % 100);
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        System.out.println(result.get());
    }
}