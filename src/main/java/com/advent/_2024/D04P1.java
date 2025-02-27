package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D04P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D04.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int result = 0;
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            String row = rows.get(rowIndex);
            for (int columnIndex = 0; columnIndex < row.length(); columnIndex++) {
                char character = row.charAt(columnIndex);

                if (character == 'X') {
                    //NW
                    if (columnIndex > 2 && rowIndex > 2) {
                        if (rows.get(rowIndex - 1).charAt(columnIndex - 1) == 'M') {
                            if (rows.get(rowIndex - 2).charAt(columnIndex - 2) == 'A') {
                                if (rows.get(rowIndex - 3).charAt(columnIndex - 3) == 'S') {
                                    System.out.println("NW " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //W
                    if (columnIndex > 2) {
                        if (rows.get(rowIndex).charAt(columnIndex - 1) == 'M') {
                            if (rows.get(rowIndex).charAt(columnIndex - 2) == 'A') {
                                if (rows.get(rowIndex).charAt(columnIndex - 3) == 'S') {
                                    System.out.println("W " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //SW
                    if (columnIndex > 2 && rowIndex < (rows.size() - 3)) {
                        if (rows.get(rowIndex + 1).charAt(columnIndex - 1) == 'M') {
                            if (rows.get(rowIndex + 2).charAt(columnIndex - 2) == 'A') {
                                if (rows.get(rowIndex + 3).charAt(columnIndex - 3) == 'S') {
                                    System.out.println("SW " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //S
                    if (rowIndex < (rows.size() - 3)) {
                        if (rows.get(rowIndex + 1).charAt(columnIndex) == 'M') {
                            if (rows.get(rowIndex + 2).charAt(columnIndex) == 'A') {
                                if (rows.get(rowIndex + 3).charAt(columnIndex) == 'S') {
                                    System.out.println("S " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //SE
                    if (columnIndex < (row.length() - 3) && rowIndex < (rows.size() - 3)) {
                        if (rows.get(rowIndex + 1).charAt(columnIndex + 1) == 'M') {
                            if (rows.get(rowIndex + 2).charAt(columnIndex + 2) == 'A') {
                                if (rows.get(rowIndex + 3).charAt(columnIndex + 3) == 'S') {
                                    System.out.println("SE " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //E
                    if (columnIndex < (row.length() - 3)) {
                        if (rows.get(rowIndex).charAt(columnIndex + 1) == 'M') {
                            if (rows.get(rowIndex).charAt(columnIndex + 2) == 'A') {
                                if (rows.get(rowIndex).charAt(columnIndex + 3) == 'S') {
                                    System.out.println("E " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //NE
                    if (columnIndex < (row.length() - 3) && rowIndex > 2) {
                        if (rows.get(rowIndex - 1).charAt(columnIndex + 1) == 'M') {
                            if (rows.get(rowIndex - 2).charAt(columnIndex + 2) == 'A') {
                                if (rows.get(rowIndex - 3).charAt(columnIndex + 3) == 'S') {
                                    System.out.println("NE " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                    //N
                    if (rowIndex > 2) {
                        if (rows.get(rowIndex - 1).charAt(columnIndex) == 'M') {
                            if (rows.get(rowIndex - 2).charAt(columnIndex) == 'A') {
                                if (rows.get(rowIndex - 3).charAt(columnIndex) == 'S') {
                                    System.out.println("N " + (columnIndex + 1) + " " + (rowIndex + 1));
                                    result++;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(result);
    }
}
