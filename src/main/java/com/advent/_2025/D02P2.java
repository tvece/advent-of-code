package com.advent._2025;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

//TODO
public class D02P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2025/D02.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        String input = lines.getFirst();
        List<Range> ranges = new ArrayList<>();
        AtomicLong maxNumber = new AtomicLong(0L);
        Stream.of(input.split(",")).forEach((stringRange) -> {
            String[] rangeSplit = stringRange.split("-");
            Range range = new Range(Long.parseLong(rangeSplit[0]), Long.parseLong(rangeSplit[1]));
            ranges.add(range);
            if (range.end > maxNumber.get()) {
                maxNumber.set(range.end);
            }
        });
    }

    record Range(long start, long end) {
    }
}