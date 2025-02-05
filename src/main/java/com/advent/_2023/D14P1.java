package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D14P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2023/D14.txt");
        List<String> stringRows;
        try {
            stringRows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        char[][] rows = new char[stringRows.size()][];

        for (int i = 0; i < stringRows.size(); i++) {
            rows[i] = stringRows.get(i).toCharArray();
        }

        north(rows);
        int weight = 0;
        for (int j = 0; j < rows.length; j++) {
            for (int k = 0; k < rows[j].length; k++) {
                if (rows[j][k] == 'O') {
                    weight += rows.length - j;
                }
            }
        }
        System.out.println(weight);
    }

    private static void north(char[][] rows) {
        for (int currentRow = 0; currentRow < rows.length; currentRow++) {
            char[] row = rows[currentRow];
            for (int currentColumn = 0; currentColumn < row.length; currentColumn++) {
                if (row[currentColumn] == 'O') {
                    Integer freeSpot = null;
                    int rowFinder = currentRow - 1;
                    while (rowFinder >= 0) {
                        char finderChar = rows[rowFinder][currentColumn];
                        if (finderChar == '#' || finderChar == 'O') {
                            break;
                        }
                        freeSpot = rowFinder;
                        rowFinder--;
                    }
                    if (freeSpot != null) {
                        rows[currentRow][currentColumn] = '.';
                        rows[freeSpot][currentColumn] = 'O';
                    }
                }
            }
        }
    }
}