package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D03P2 {
    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D03.txt");
        String input;
        try {
            input = String.join("", Files.readAllLines(filePath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        long result = 0;

        input = "do()" + input;
        input = input + "don't()";

        Pattern blockPattern = Pattern.compile("(?<=do\\(\\))(.+?)((?=don't\\(\\))|(?=do\\(\\)))");
        Matcher blockMatcher = blockPattern.matcher(input);
        while (blockMatcher.find()) {
            String block = blockMatcher.group(1);
            Pattern pattern = Pattern.compile("mul\\(([0-9]*),([0-9]*)\\)");

            Matcher matcher = pattern.matcher(block);
            while (matcher.find()) {
                result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
            }
        }

        System.out.printf("%d", result);
    }
}
