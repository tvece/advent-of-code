package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class D14 {
    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D14.txt");
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
                robots.add(new Robot(Integer.parseInt(stringPosition[0]), Integer.parseInt(stringPosition[1]),
                        Integer.parseInt(stringVelocity[0]), Integer.parseInt(stringVelocity[1])));
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        for (int i = 0; i < 100; i++) {
            for (Robot robot : robots) {
                robot.move();
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
        System.out.println(result);

    }

    private static class Robot {
        private static final int edgeX = 101;
        private static final int edgeY = 103;

        int positionX;
        int positionY;
        int velocityX;
        int velocityY;

        Robot(int positionX, int positionY, int velocityX, int velocityY) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        public void move() {
            positionX += velocityX;
            if (positionX < 0) {
                positionX = edgeX + positionX;
            } else if (positionX >= edgeX) {
                positionX = positionX - edgeX;
            }

            positionY += velocityY;
            if (positionY < 0) {
                positionY = edgeY + positionY;
            } else if (positionY >= edgeY) {
                positionY = positionY - edgeY;
            }
        }

        public int getQuadrant() {
            if (positionX < edgeX / 2) {
                if (positionY < edgeY / 2) {
                    return 1;
                } else if (positionY > edgeY / 2) {
                    return 2;
                }
            } else if (positionX > edgeX / 2) {
                if (positionY < edgeY / 2) {
                    return 3;
                } else if (positionY > edgeY / 2) {
                    return 4;
                }
            }
            return 0;
        }
    }
}
