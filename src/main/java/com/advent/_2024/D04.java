package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D04 {
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
            //PART 2
            for (int columnIndex = 1; columnIndex < (row.length() - 1); columnIndex++) {
                char character = row.charAt(columnIndex);


                if (character == 'A') {
                    //+
                    if (     //vertical
                            ((rows.get(rowIndex - 1).charAt(columnIndex) == 'M' && (rows.get(rowIndex + 1).charAt(columnIndex) == 'S')) ||
                                    (rows.get(rowIndex - 1).charAt(columnIndex) == 'S' && (rows.get(rowIndex + 1).charAt(columnIndex) == 'M'))) &&
                                    // horizontal
                                    ((rows.get(rowIndex).charAt(columnIndex - 1) == 'M' && (rows.get(rowIndex).charAt(columnIndex - 1) == 'S')) ||
                                            (rows.get(rowIndex).charAt(columnIndex - 1) == 'S' && (rows.get(rowIndex).charAt(columnIndex - 1) == 'M')))
                    ) {
                        result++;
                    }
                    if (    //upper start
                            ((rows.get(rowIndex - 1).charAt(columnIndex - 1) == 'M' && (rows.get(rowIndex + 1).charAt(columnIndex + 1) == 'S')) ||
                                    (rows.get(rowIndex - 1).charAt(columnIndex - 1) == 'S' && (rows.get(rowIndex + 1).charAt(columnIndex + 1) == 'M'))) &&
                                    // lower start
                                    ((rows.get(rowIndex + 1).charAt(columnIndex - 1) == 'M' && (rows.get(rowIndex - 1).charAt(columnIndex + 1) == 'S')) ||
                                            (rows.get(rowIndex + 1).charAt(columnIndex - 1) == 'S' && (rows.get(rowIndex - 1).charAt(columnIndex + 1) == 'M')))
                    ) {
                        result++;
                    }

                    //X
                }
            }
            /*
            //PART 1
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
        */
        }
        System.out.println(result);
    }
}
