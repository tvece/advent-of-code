package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

//TODO: part 2
public class D17 {
    private int registerA;
    private int registerB;
    private int registerC;

    private int instructionPointer = 0;

    private String out = "";

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2024/D17.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        int defaultB = Integer.parseInt(input.get(1).substring("Register B: ".length()));
        int defaultC = Integer.parseInt(input.get(2).substring("Register C: ".length()));

        String goal = input.get(4).substring("Program: ".length());
        List<Integer> instructions = Arrays.stream(goal.split(",")).map(Integer::parseInt).toList();
        int result = 0;
        while (true) {
            D17 instance = new D17(result, defaultB, defaultC);

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
                instance.instructionPointer++;
                instance.instructionPointer++;
            }

            // -1 because of ',' at the end
            if (instance.out.substring(0, instance.out.length() - 1).equals(goal)) {
                System.out.println(result);
                return;
            }
            /*
            System.out.println(result);
            System.out.println(instance.out);
            System.out.println();
            */
            result++;
        }
    }

    D17(int registerA, int registerB, int registerC) {
        this.registerA = registerA;
        this.registerB = registerB;
        this.registerC = registerC;
    }

    private void adv(int operand) {
        this.registerA = (int) (this.registerA / Math.pow(2, getComboOperand(operand)));
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
        this.out += getComboOperand(operand) % 8 + ",";
    }

    private void bdv(int operand) {
        this.registerB = (int) (this.registerA / Math.pow(2, getComboOperand(operand)));
    }

    private void cdv(int operand) {
        this.registerC = (int) (this.registerA / Math.pow(2, getComboOperand(operand)));
    }

    private int getComboOperand(int operand) {
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
        throw new RuntimeException("Unsupported operand 7");
    }
}
