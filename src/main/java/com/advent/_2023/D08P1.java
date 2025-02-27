package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class D08P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D08.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        int instructionsLength = lines.getFirst().length();
        int[] instructions = lines.getFirst().chars().map(character -> character == 'L' ? 0 : 1).toArray();

        int zPath = getStringToIntPath("ZZZ");

        //ZZZ + L + R
        List<Integer> map = new ArrayList<>(Collections.nCopies(zPath + 2, 0));
        int currentStep = -1;
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            String brackets = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
            String[] stringDestinations = brackets.split(", ");
            String indexPath = line.substring(0, line.indexOf(' '));
            int mapIndex = getStringToIntPath(indexPath);
            if (indexPath.equals("AAA")) {
                currentStep = mapIndex;
            }
            map.set(mapIndex, getStringToIntPath(stringDestinations[0]));
            map.set(mapIndex + 1, getStringToIntPath(stringDestinations[1]));
        }

        if (currentStep == -1) {
            throw new Error("Initial AAA step not found!");
        }


        int index = -1;

        int stepCounter = 0;
        while (true) {
            if (currentStep >= zPath) {
                System.out.println(stepCounter);
                return;
            }
            stepCounter++;
            index++;
            if (index == instructionsLength) {
                index = 0;
            }
            if (instructions[index] == 0) {
                // L
                currentStep = map.get(currentStep);
            } else {
                // R
                currentStep = map.get(currentStep + 1);
            }
        }
    }

    private static int getStringToIntPath(String stringPath) {
        return ((int) stringPath.charAt(0) * 100000 + (int) stringPath.charAt(1) * 1000
                + (int) stringPath.charAt(2) * 10);
    }
}