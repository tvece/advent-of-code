package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://adventofcode.com/2023/day/13
public class D13P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D13.txt");
        List<String> stringRows;
        try {
            stringRows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        List<List<String>> maps = new ArrayList<>();
        maps.add(new ArrayList<>());
        int i = 0;
        for (String row : stringRows) {
            if (row.isEmpty()) {
                maps.add(new ArrayList<>());
                i++;
                continue;
            }
            maps.get(i).add(row);
        }
        long result = 0;
        for (List<String> map : maps) {
            char[][] rows = new char[map.size()][];
            for (int rowIndex = 0; rowIndex < map.size(); rowIndex++) {
                rows[rowIndex] = map.get(rowIndex).toCharArray();
            }
            char[][] columns = new char[map.getFirst().length()][];
            for (int columnIndex = 0; columnIndex < map.getFirst().length(); columnIndex++) {
                columns[columnIndex] = new char[map.size()];
                for (int j = 0; j < map.size(); j++) {
                    columns[columnIndex][j] = map.get(j).charAt(columnIndex);
                }
            }
            int foundReflection;
            for (int rowIndex = 0; rowIndex < rows.length - 1; rowIndex++) {
                boolean isReflection = true;
                int leftCheck = rowIndex;
                for (int rightCheck = rowIndex + 1; rightCheck < rows.length
                        && rightCheck < ((rowIndex + 1) * 2); rightCheck++) {
                    if (!Arrays.equals(rows[leftCheck], rows[rightCheck])) {
                        isReflection = false;
                        break;
                    }
                    leftCheck--;
                }
                if (isReflection) {
                    foundReflection = rowIndex;
                    result += 100L * (foundReflection + 1);
                    break;
                }
            }
            for (int columnIndex = 0; columnIndex < columns.length - 1; columnIndex++) {
                boolean isReflection = true;
                int leftCheck = columnIndex;
                for (int rightCheck = columnIndex + 1; rightCheck < columns.length
                        && rightCheck < ((columnIndex + 1) * 2); rightCheck++) {
                    if (!Arrays.equals(columns[leftCheck], columns[rightCheck])) {
                        isReflection = false;
                        break;
                    }
                    leftCheck--;
                }
                if (isReflection) {
                    foundReflection = columnIndex;
                    result += foundReflection + 1;
                    break;
                }
            }
        }
        System.out.println(result);
    }
}