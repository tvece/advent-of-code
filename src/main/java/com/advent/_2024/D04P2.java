package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D04P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D04.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int result = 0;
        for (int rowIndex = 1; rowIndex < (rows.size() - 1); rowIndex++) {
            String row = rows.get(rowIndex);
            for (int columnIndex = 1; columnIndex < (row.length() - 1); columnIndex++) {
                char character = row.charAt(columnIndex);
                if (character == 'A') {
                    if (     //+
                            ((rows.get(rowIndex - 1).charAt(columnIndex) == 'M' && (rows.get(rowIndex + 1).charAt(columnIndex) == 'S')) ||
                                    (rows.get(rowIndex - 1).charAt(columnIndex) == 'S' && (rows.get(rowIndex + 1).charAt(columnIndex) == 'M'))) &&
                                    // horizontal
                                    ((rows.get(rowIndex).charAt(columnIndex - 1) == 'M' && (rows.get(rowIndex).charAt(columnIndex - 1) == 'S')) ||
                                            (rows.get(rowIndex).charAt(columnIndex - 1) == 'S' && (rows.get(rowIndex).charAt(columnIndex - 1) == 'M')))
                    ) {
                        result++;
                    }
                    if (    //X
                            ((rows.get(rowIndex - 1).charAt(columnIndex - 1) == 'M' && (rows.get(rowIndex + 1).charAt(columnIndex + 1) == 'S')) ||
                                    (rows.get(rowIndex - 1).charAt(columnIndex - 1) == 'S' && (rows.get(rowIndex + 1).charAt(columnIndex + 1) == 'M'))) &&
                                    // lower start
                                    ((rows.get(rowIndex + 1).charAt(columnIndex - 1) == 'M' && (rows.get(rowIndex - 1).charAt(columnIndex + 1) == 'S')) ||
                                            (rows.get(rowIndex + 1).charAt(columnIndex - 1) == 'S' && (rows.get(rowIndex - 1).charAt(columnIndex + 1) == 'M')))
                    ) {
                        result++;
                    }
                }
            }
        }
        System.out.println(result);
    }
}
