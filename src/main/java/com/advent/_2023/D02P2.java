package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

public class D02P2 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2023/D02.txt");

        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            int result = 0;
            int i = 0;
            for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
                Game game = new Game(it.next(), ++i);
                result += (game.red * game.green * game.blue);
            }
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
    }


    private static class Game {
        public int red = 0;
        public int blue = 0;
        public int green = 0;

        public int id;

        public Game(String line, int id) {
            this.id = id;
            // remove "Game XY: "
            line = line.substring(line.indexOf(": ") + 2);
            for (String grab : line.split("; ")) {
                for (String groupString : grab.split(", ")) {
                    String[] group = groupString.split(" ");
                    int count = Integer.parseInt(group[0]);
                    switch (group[1]) {
                        case "red":
                            if (red < count) {
                                red = count;
                            }
                            break;
                        case "blue":
                            if (blue < count) {
                                blue = count;
                            }
                            break;
                        case "green":
                            if (green < count) {
                                green = count;
                            }
                            break;
                        default:
                            throw new RuntimeException("Unexpected dice color: " + group[1]);
                    }
                }
            }
        }
    }
}
