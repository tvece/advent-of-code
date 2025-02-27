package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class D14P12 {
    private static final int width = 101;
    private static final int height = 103;

    // Part 1
    //private static final boolean isPart1 = true;
    // Part 2 - increase the seconds and print position after each second;
    private static final boolean isPart1 = false;

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D14.txt");
        List<Robot> robots = new ArrayList<>();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                //"p=80,58 v=-80,-45"     ->      ["p=80,58","v=-80,-45"]
                String[] parts = line.split(" ");
                //"p=80,58"               ->      "80,58"
                parts[0] = parts[0].substring(2);
                //"v=-80,-45"             ->      "-80,-45"
                parts[1] = parts[1].substring(2);
                //"80,58"                 ->      ["80","58"]
                String[] stringPosition = parts[0].split(",");
                //"-80,-45"                 ->      ["-80","-45"]
                String[] stringVelocity = parts[1].split(",");
                robots.add(new Robot(Integer.parseInt(stringPosition[1]), Integer.parseInt(stringPosition[0]),
                        Integer.parseInt(stringVelocity[1]), Integer.parseInt(stringVelocity[0])));
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int seconds;
        if (isPart1) {
            seconds = 100;
        } else {
            seconds = 10000;
        }
        for (int i = 0; i < seconds; i++) {
            Set<Position> robotPositions = new HashSet<>();
            for (Robot robot : robots) {
                robot.move();
                robotPositions.add(robot.getPosition());
            }
            if (!isPart1 && i > 500) {
                System.out.println("Run: " + (i + 1));
                for (int column = 0; column < width; column++) {
                    for (int row = 0; row < height; row++) {
                        if (robotPositions.contains(new Position(column, row))) {
                            System.out.print("X");
                        } else {
                            System.out.print(" ");
                        }
                    }
                    System.out.println();
                }
                System.out.println("\n\n\n");
            }
        }
        int[] quadrants = new int[]{0, 0, 0, 0, 0,};
        for (Robot robot : robots) {
            quadrants[robot.getQuadrant()] += 1;
        }
        long result = 1;
        for (int i = 1; i < quadrants.length; i++) {
            result = result * quadrants[i];
        }
        System.out.println("safety factor: " + result);

    }

    private static class Robot {

        int row;
        int column;
        int rowVelocity;
        int columnVelocity;

        Robot(int column, int row, int columnVelocity, int rowVelocity) {
            this.column = column;
            this.row = row;
            this.columnVelocity = columnVelocity;
            this.rowVelocity = rowVelocity;
        }

        public void move() {
            row += rowVelocity;
            if (row < 0) {
                row = width + row;
            } else if (row >= width) {
                row = row - width;
            }

            column += columnVelocity;
            if (column < 0) {
                column = height + column;
            } else if (column >= height) {
                column = column - height;
            }
        }

        public int getQuadrant() {
            if (row < width / 2) {
                if (column < height / 2) {
                    return 1;
                } else if (column > height / 2) {
                    return 2;
                }
            } else if (row > width / 2) {
                if (column < height / 2) {
                    return 3;
                } else if (column > height / 2) {
                    return 4;
                }
            }
            return 0;
        }

        public Position getPosition() {
            return new Position(column, row);
        }
    }

    record Position(int x, int y) {
    }
}
