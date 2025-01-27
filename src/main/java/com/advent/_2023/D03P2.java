package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D03P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2023/D03.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        Map<String, List<List<Integer>>> resultNumbers = new HashMap<>();
        for (int lineIterator = 0; lineIterator < lines.size(); lineIterator++) {
            String line = lines.get(lineIterator);
            int charIterator = 0;
            List<Integer> currentNumber = new ArrayList<>();
            String currentStarCords = "";
            boolean wasNextToSymbol = false;
            while (charIterator < line.length()) {
                char charString = line.charAt(charIterator);
                if (Character.isDigit(charString)) {
                    if (!wasNextToSymbol) {
                        String nextToCords = getNextToCords(charIterator, lineIterator, lines);
                        if (nextToCords.equals(NEXT_TO_OTHER)) {
                            wasNextToSymbol = true;
                        } else if (!nextToCords.equals(NEXT_TO_NONE) && !currentStarCords.contains(nextToCords)) {
                            currentStarCords += nextToCords;
                        }
                    }
                    currentNumber.add(Character.getNumericValue(charString));
                } else {
                    if (!currentNumber.isEmpty() && !currentStarCords.isEmpty()) {
                        String[] cords = currentStarCords.split(";");
                        for (String cord : cords) {
                            List<Integer> copy = new ArrayList<>(currentNumber);
                            if (!resultNumbers.containsKey(cord)) {
                                resultNumbers.put(cord, new ArrayList<>());
                            }
                            resultNumbers.get(cord).add(copy);
                        }
                    }
                    wasNextToSymbol = false;
                    if (!currentNumber.isEmpty()) {
                        currentNumber.clear();
                    }
                    if (!currentStarCords.isEmpty()) {
                        currentStarCords = "";
                    }
                }
                charIterator++;
            }
            if (!currentNumber.isEmpty() && !currentStarCords.isEmpty()) {
                String[] cords = currentStarCords.split(";");
                for (String cord : cords) {
                    List<Integer> copy = new ArrayList<>(currentNumber);
                    if (!resultNumbers.containsKey(cord)) {
                        resultNumbers.put(cord, new ArrayList<>());
                    }
                    resultNumbers.get(cord).add(copy);
                }
            }
            if (!currentNumber.isEmpty()) {
                currentNumber.clear();
            }
        }
        List<Integer> resultArray = new ArrayList<>();
        resultNumbers.keySet().forEach(key -> {
            if (resultNumbers.get(key).size() == 2) {
                resultArray.add(getNumberFromStringParts(resultNumbers.get(key).get(0))
                        * getNumberFromStringParts(resultNumbers.get(key).get(1)));
            }
        });
        Integer result = 0;
        for (Integer integer : resultArray) {
            result += integer;
        }
        System.out.println(result);
    }

    private static int getNumberFromStringParts(List<Integer> parts) {
        int result = 0;
        for (int j = 0; j < parts.size(); j++) {
            result += (((int) Math.pow(10, parts.size() - j - 1) * parts.get(j)));
        }
        return result;
    }

    private static final String NEXT_TO_OTHER = "other";
    private static final String NEXT_TO_NONE = "none";

    private static String getNextToCords(int x, int y, List<String> lines) {
        StringBuilder cords = new StringBuilder();
        boolean isNextToSymbol = false;
        for (int xrange = x - 1; xrange <= x + 1; xrange++) {
            if (xrange >= 0 && lines.getFirst().length() > xrange) {
                for (int yrange = y - 1; yrange <= y + 1; yrange++) {
                    if (yrange >= 0 && lines.size() > yrange) {
                        if (!(xrange == x && yrange == y)) {
                            char charCheck = lines.get(yrange).charAt(xrange);
                            if (!Character.isDigit(charCheck) && charCheck != '.') {
                                if (charCheck == '*') {
                                    cords.append(xrange).append(",").append(yrange).append(";");
                                }
                                isNextToSymbol = true;
                            }
                        }
                    }
                }
            }
        }
        if (cords.isEmpty()) {
            if (isNextToSymbol) {
                return NEXT_TO_OTHER;
            } else {
                return NEXT_TO_NONE;
            }
        }
        return cords.toString();
    }
}
