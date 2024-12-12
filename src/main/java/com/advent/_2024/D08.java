package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class D08 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D08.txt");
        List<String> rows;
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        HashMap<Character, List<Location>> toBeProcessed = new HashMap<>();
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            String row = rows.get(rowIndex);
            for (int columnIndex = 0; columnIndex < row.length(); columnIndex++) {
                char character = row.charAt(columnIndex);
                if (character != '.') {
                    Location location = new Location(rowIndex, columnIndex);
                    if (toBeProcessed.containsKey(character)) {
                        toBeProcessed.get(character).add(location);
                    } else {
                        toBeProcessed.put(character, new ArrayList<>(List.of(location)));
                    }
                }
            }
        }

        for (Character c : toBeProcessed.keySet()) {
            List<Location> locations = toBeProcessed.get(c);
            for (int firstLocationIndex = 0; firstLocationIndex < locations.size(); firstLocationIndex++) {
                for (int secondLocationIndex = firstLocationIndex + 1; secondLocationIndex < locations.size(); secondLocationIndex++) {
                    System.out.println(locations.get(firstLocationIndex) + " " + locations.get(secondLocationIndex));
                }
            }
        }

    }

    record Location(int x, int y) {
    }
}
