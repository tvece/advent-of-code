package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// logic is based on observing how result changes based on input
public class D17P2 {

    long registerA;
    long registerB;
    long registerC;

    int instructionPointer = 0;

    List<Integer> out = new ArrayList<>();

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D17.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        String goal = input.get(4).substring("Program: ".length());
        List<Integer> instructions = Arrays.stream(goal.split(",")).map(Integer::parseInt).toList();

        long result = 1L;
        int s = 1; // suffix to check
        while (true) {
            List<Integer> stack = run(instructions, result);
            if (instructions.equals(stack)) {
                System.out.println("Result: " + result);
                return;
            }
            if (s <= instructions.size()) {
                // Suffix of program of length s
                List<Integer> suffix = instructions.subList(instructions.size() - s, instructions.size());
                if (stack.equals(suffix)) {
                    result *= 8;
                    s++;
                    continue;
                }
            }
            result++;
        }
    }

    public static List<Integer> run(List<Integer> instructions, Long defaultA) {
        D17P2 instance = new D17P2();
        instance.registerA = defaultA;
        instance.registerB = 0;
        instance.registerC = 0;
        instance.instructionPointer = 0;

        while (instance.instructionPointer < instructions.size() - 1) {
            int instruction = instructions.get(instance.instructionPointer);
            int operand = instructions.get(instance.instructionPointer + 1);
            switch (instruction) {
                case 0:
                    instance.adv(operand);
                    break;
                case 1:
                    instance.bxk(operand);
                    break;
                case 2:
                    instance.bst(operand);
                    break;
                case 3:
                    instance.jnz(operand);
                    break;
                case 4:
                    instance.bxc();
                    break;
                case 5:
                    instance.out(operand);
                    break;
                case 6:
                    instance.bdv(operand);
                    break;
                case 7:
                    instance.cdv(operand);
                    break;
            }
            instance.instructionPointer += 2;
        }
        return instance.out;
    }

    private void adv(int operand) {
        this.registerA = (long) (this.registerA / Math.pow(2, getComboOperand(operand)));
    }

    private void bxk(int operand) {
        this.registerB = this.registerB ^ operand;
    }

    private void bst(int operand) {
        this.registerB = getComboOperand(operand) % 8;
    }

    private void jnz(int operand) {
        if (this.registerA == 0) {
            return;
        }
        this.instructionPointer = operand - 2;
    }

    private void bxc() {
        this.registerB = this.registerB ^ this.registerC;
    }

    private void out(int operand) {
        this.out.add((int) (getComboOperand(operand) % 8));
    }

    private void bdv(int operand) {
        this.registerB = (long) (this.registerA / Math.pow(2, getComboOperand(operand)));
    }

    private void cdv(int operand) {
        this.registerC = (long) (this.registerA / Math.pow(2, getComboOperand(operand)));
    }

    private long getComboOperand(int operand) {
        if (operand < 4) {
            return operand;
        }
        if (operand == 4) {
            return this.registerA;
        }
        if (operand == 5) {
            return this.registerB;
        }
        if (operand == 6) {
            return this.registerC;
        }
        throw new RuntimeException("Unsupported operand : " + operand);
    }
}