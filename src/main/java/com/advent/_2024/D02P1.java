package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class D02P1 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D02.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int result = 0;
        for (String line : lines) {
            boolean isValidReport = isValidReport(Arrays.stream(line.split(" ")).mapToInt(Integer::valueOf).toArray());
            result += isValidReport ? 1 : 0;
            System.out.println(line + " " + isValidReport);
        }

        System.out.print(result);
    }

    private static boolean isValidReport(int[] report) {
        if (report.length < 3) {
            throw new RuntimeException("report " + Arrays.toString(report) + " is invalid! only three or more numbers are supported.");
        }
        int dif = report[0] - report[1];

        if (dif == 0 || dif > 3 || dif < -3) {
            return false;
        }

        boolean isDescending = dif > 0;

        for (int i = 1; i < report.length - 1; i++) {
            int dif2 = report[i] - report[i + 1];
            if (dif2 == 0) {
                return false;
            }

            if (isDescending && (dif2 > 3 || dif2 < 1)) {
                return false;
            }

            if (!isDescending && (dif2 < -3 || dif2 > -1)) {
                return false;
            }
        }
        return true;
    }
}
