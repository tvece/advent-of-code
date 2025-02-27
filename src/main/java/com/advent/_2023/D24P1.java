package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// https://adventofcode.com/2023/day/24
public class D24P1 {

    public static void main(String[] args) {
        List<Hailstone> stones = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get("../advent-of-code-input/2023/D24.txt"), StandardCharsets.UTF_8)) {
            lines.forEach(line -> stones.add(new Hailstone(line)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        long result = 0;
        double min = 200000000000000.0;
        double max = 400000000000000.0;
        for (int i = 0; i < stones.size(); i++) {
            for (int j = i + 1; j < stones.size(); j++) {
                Hailstone first = stones.get(i);
                Hailstone second = stones.get(j);
                int denominator = (first.xVelocity * second.yVelocity) - (first.yVelocity * second.xVelocity);
                long number1 = ((second.x - first.x) * second.yVelocity) - ((second.y - first.y) * second.xVelocity);
                long number2 = ((first.x - second.x) * first.yVelocity) - ((first.y - second.y) * first.xVelocity);
                if (denominator != 0 && (number1 / denominator) > 0 && (number2 / denominator) < 0) {
                    double intersectionX = (number1 / (double) denominator) * first.xVelocity + first.x;
                    double intersectionY = (number1 / (double) denominator) * first.yVelocity + first.y;
                    if (intersectionX >= min && intersectionX <= max && intersectionY >= min && intersectionY <= max) {
                        result++;
                    }
                }
            }
        }
        System.out.println(result);
    }

    private static class Hailstone {
        long x;
        long y;
        long z;
        int xVelocity;
        int yVelocity;
        int zVelocity;

        public Hailstone(String line) {
            String[] ends = line.split("@");
            String[] part1 = ends[0].split(",");
            x = Long.parseLong(part1[0].trim());
            y = Long.parseLong(part1[1].trim());
            z = Long.parseLong(part1[2].trim());
            String[] part2 = ends[1].split(",");
            xVelocity = Integer.parseInt(part2[0].trim());
            yVelocity = Integer.parseInt(part2[1].trim());
            zVelocity = Integer.parseInt(part2[2].trim());
        }
    }
}