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

public class D04P12 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D04.txt");
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            List<Integer> matches = new ArrayList<>(List.of(0));
            for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
                String line = it.next();
                line = line.substring(line.indexOf(":") + 1);
                List<Integer> winningNumbers = new ArrayList<>();
                List<Integer> actualNumbers = new ArrayList<>();
                boolean isBeforePipe = true;
                int index = 0;
                while (index < line.length() - 2) {
                    int currentNumber = 0;
                    char currentChar = line.charAt(index + 1);
                    char nextChar = line.charAt(index + 2);
                    if (currentChar == '|') {
                        isBeforePipe = false;
                        index += 2;
                        continue;
                    }
                    if (currentChar != ' ') {
                        currentNumber = Character.getNumericValue(currentChar) * 10;
                    }

                    currentNumber += Character.getNumericValue(nextChar);
                    if (isBeforePipe) {
                        winningNumbers.add(currentNumber);
                    } else {
                        actualNumbers.add(currentNumber);
                    }
                    index += 3;
                }
                int count = actualNumbers.stream().filter(winningNumbers::contains)
                        .toList().size();
                matches.add(count);
            }

            System.out.println(matches.stream().mapToInt(a -> (int) Math.pow(2, a - 1)).sum());

            List<Integer> resolvedMatches = new ArrayList<>(Collections.nCopies(matches.size(), 0));
            for (int i = matches.size() - 1; i > 0; i--) {
                int result = 1;
                for (int j = 1; j <= matches.get(i); j++) {
                    result += resolvedMatches.get(j + i);
                }
                resolvedMatches.set(i, result);
            }

            System.out.println(resolvedMatches.stream().mapToInt(a -> a).sum());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
    }
}