package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

// https://adventofcode.com/2023/day/1
public class D01P2 {

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
                    int value = 0;
                    String subString = line.substring(j);
                    if (Character.isDigit(character)) {
                        value = Character.getNumericValue(character);
                    } else if (subString.startsWith("one")) {
                        value = 1;
                    } else if (subString.startsWith("two")) {
                        value = 2;
                    } else if (subString.startsWith("three")) {
                        value = 3;
                    } else if (subString.startsWith("four")) {
                        value = 4;
                    } else if (subString.startsWith("five")) {
                        value = 5;
                    } else if (subString.startsWith("six")) {
                        value = 6;
                    } else if (subString.startsWith("seven")) {
                        value = 7;
                    } else if (subString.startsWith("eight")) {
                        value = 8;
                    } else if (subString.startsWith("nine")) {
                        value = 9;
                    }
                    if (value != 0) {
                        if (firstValue == 0) {
                            firstValue = value;
                        } else {
                            lastValue = value;
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