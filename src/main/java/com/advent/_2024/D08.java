package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class D08 {
    //TODO: fix part 1
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

        Set<Location> result = new HashSet<>();
        for (Character c : toBeProcessed.keySet()) {
            List<Location> locations = toBeProcessed.get(c);
            for (int firstLocationIndex = 0; firstLocationIndex < locations.size(); firstLocationIndex++) {
                for (int secondLocationIndex = firstLocationIndex + 1; secondLocationIndex < locations.size(); secondLocationIndex++) {
                    Location locationA = locations.get(firstLocationIndex);
                    Location locationB = locations.get(secondLocationIndex);
                    System.out.println(locationA + " | " + locationB);

                    int diffX = locationA.x - locationB.x;
                    int diffY = locationA.y - locationB.y;

                    if (locationB.x > locationA.x) {
                        diffX = -diffX;
                    }

                    if (locationB.y > locationA.y) {
                        diffY = -diffY;
                    }

                    int currentX = locationA.x - diffX;
                    int currentY = locationA.y - diffY;
                    if (currentX >= 0 && currentY >= 0 &&
                            currentX < rows.getFirst().length() && currentY < rows.size()) {
                        result.add(new Location(currentX, currentY));
                        System.out.println(new Location(currentX, currentY));
                    }
                    currentX = locationB.x + diffX;
                    currentY = locationB.y + diffY;
                    if (currentX >= 0 && currentY >= 0 &&
                            currentX < rows.getFirst().length() && currentY < rows.size()) {
                        result.add(new Location(currentX, currentY));
                        System.out.println(new Location(currentX, currentY));
                    }
                    System.out.println();
                }
            }
        }

        System.out.println(result);
        System.out.println();

        for (int x = 0; x < rows.size(); x++) {
            for (int y = 0; y < rows.getFirst().length(); y++) {
                if (result.contains(new Location(x, y))) {
                    System.out.print('A');
                } else {
                    System.out.print(rows.get(x).charAt(y));
                }
            }
            System.out.print('\n');
        }
    }

    record Location(int x, int y) {
    }
}
