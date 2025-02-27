package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// https://adventofcode.com/2023/day/3
public class D03P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D03.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        Integer result = 0;
        for (int lineIterator = 0; lineIterator < lines.size(); lineIterator++) {
            String line = lines.get(lineIterator);
            for (int charIterator = 0; charIterator < line.length(); charIterator++) {
                List<Integer> currentNumber = new ArrayList<>();
                boolean wasNextToSymbol = false;
                while (charIterator < line.length()) {
                    char charString = line.charAt(charIterator);
                    if (Character.isDigit(charString)) {
                        if (!wasNextToSymbol && isNextToSymbol(charIterator, lineIterator, lines)) {
                            wasNextToSymbol = true;
                        }
                        currentNumber.add(Character.getNumericValue(charString));
                    } else {
                        break;
                    }
                    charIterator++;
                }
                if (!currentNumber.isEmpty() && wasNextToSymbol) {
                    for (int numberIterator = 0; numberIterator < currentNumber.size(); numberIterator++) {
                        result += (((int) Math.pow(10, currentNumber.size() - numberIterator - 1) * currentNumber.get(numberIterator)));
                    }
                    currentNumber.clear();
                }
            }
        }
        System.out.println(result);

    }

    private static boolean isNextToSymbol(int x, int y, List<String> lines) {
        for (int xrange = x - 1; xrange <= x + 1; xrange++) {
            if (xrange >= 0 && lines.getFirst().length() > xrange) {
                for (int yrange = y - 1; yrange <= y + 1; yrange++) {
                    if (yrange >= 0 && lines.size() > yrange) {
                        if (xrange != 0 && yrange != 0) {
                            char charCheck = lines.get(yrange).charAt(xrange);
                            if (!Character.isDigit(charCheck) && charCheck != '.') {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
