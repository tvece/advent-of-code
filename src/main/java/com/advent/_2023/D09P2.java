package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class D09P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D09.txt");
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            int result = 0;
            for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
                String stringLine = it.next();
                List<Integer> line = Stream.of(stringLine.split(" ")).map(Integer::valueOf).toList();
                List<List<Integer>> diffLines = new ArrayList<>(Collections.singletonList(line));
                List<Integer> previousLine = new ArrayList<>(line);
                while (true) {
                    List<Integer> diffLine = new ArrayList<>();
                    for (int i = 0; i < previousLine.size() - 1; i++) {
                        diffLine.add(previousLine.get(i + 1) - previousLine.get(i));
                    }
                    diffLines.add(diffLine);
                    if (diffLine.stream().allMatch(diff -> diff.equals(0))) {
                        Integer value = 0;
                        for (int i = diffLines.size() - 1; i >= 0; i--) {
                            value = diffLines.get(i).getFirst() - value;
                        }
                        result += value;
                        break;
                    }
                    previousLine = diffLine;
                }
            }
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
    }
}