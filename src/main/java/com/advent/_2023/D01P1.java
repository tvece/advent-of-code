package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

// https://adventofcode.com/2023/day/1
public class D01P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D01.txt");
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            int result = 0;
            for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
                String line = it.next();
                // parse the line to find first and last values
                int firstValue = 0;
                int lastValue = 0;
                for (int j = 0; j < line.length(); j++) {
                    char character = line.charAt(j);
                    if (Character.isDigit(character)) {
                        if (firstValue == 0) {
                            firstValue = Character.getNumericValue(character);
                        } else {
                            lastValue = Character.getNumericValue(character);
                        }
                    }
                }
                System.out.println(line + " " + firstValue + " " + lastValue);
                // evaluate the line and add its value to result
                if (firstValue != 0) {
                    result += (firstValue * 10);
                    if (lastValue != 0) {
                        result += lastValue;
                    } else {
                        result += firstValue;
                    }
                }
            }
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
    }
}