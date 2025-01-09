package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D19P1 {

    final static String ACCEPTED = "A";
    final static String REJECTED = "R";
    final static String INITIAL = "in";

    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("src/main/resources/2023/D19.txt");
        List<String> rows = new ArrayList<String>();
        try {
            rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        Map<String, Instruction> instructions = new HashMap<>();
        int i = 0;
        while (!rows.get(i).isEmpty()) {
            Instruction newInstruction = new Instruction(rows.get(i));
            instructions.put(newInstruction.id, newInstruction);
            i++;
        }
        i++;
        List<Element> input = new ArrayList<>();
        while (i < rows.size()) {
            input.add(new Element(rows.get(i)));
            i++;
        }

        int result = 0;
        Instruction initInstruction = instructions.get(INITIAL);
        for (Element inputElement : input) {
            Instruction currentInstruction = initInstruction;
            while (true) {
                String nextInstructionId = currentInstruction.getNextInstructionId(inputElement);
                if (nextInstructionId.equals(ACCEPTED) || nextInstructionId.equals(REJECTED)) {
                    if (nextInstructionId.equals(ACCEPTED)) {
                        result += inputElement.x + inputElement.m + inputElement.a + inputElement.s;
                    }
                    break;
                }
                currentInstruction = instructions.get(nextInstructionId);
            }
        }
        System.out.println(result);
    }

    private static class Instruction {
        List<Step> steps;
        String id;

        Instruction(String row) {
            this.id = row.substring(0, row.indexOf('{'));
            row = row.substring(row.indexOf('{') + 1);
            row = row.substring(0, row.indexOf('}'));
            steps = new ArrayList<>();
            for (String stepString : row.split(",")) {
                steps.add(new Step(stepString));
            }
        }

        private String getNextInstructionId(Element element) {
            for (Step step : steps) {
                if (step.accepts(element)) {
                    return step.destination;
                }
            }
            throw new RuntimeException();
        }

        public String toString() {
            return this.id + " " + this.steps;
        }
    }

    private static class Step {
        String toCheckElement;
        Type type;
        String destination;
        int threshold;

        Step(String description) {
            toCheckElement = description.substring(0, 1);
            if (description.contains(":")) {
                if (description.charAt(1) == '<') {
                    type = Type.LESS_THAN;
                } else {
                    type = Type.GREATER_THAN;
                }
                threshold = Integer.parseInt(description.substring(2, description.indexOf(':')));
                destination = description.substring(description.indexOf(':') + 1);
            } else {
                type = Type.DIRECT;
                destination = description;
            }
        }

        private boolean accepts(Element element) {
            if (this.type == Type.GREATER_THAN) {
                return element.valueMap.get(this.toCheckElement) > this.threshold;
            } else if (this.type == Type.LESS_THAN) {
                return element.valueMap.get(this.toCheckElement) < this.threshold;
            }
            return true;
        }

        public String toString() {
            return this.toCheckElement + " " + this.type + " " + this.threshold + " " + this.destination;
        }
    }

    private static class Element {
        int x;
        int m;
        int a;
        int s;
        Map<String, Integer> valueMap;

        Element(String description) {
            description = description.substring(1);
            description = description.substring(0, description.length() - 1);
            String[] split = description.split(",");
            this.x = Integer.parseInt(split[0].substring(2));
            this.m = Integer.parseInt(split[1].substring(2));
            this.a = Integer.parseInt(split[2].substring(2));
            this.s = Integer.parseInt(split[3].substring(2));
            valueMap = new HashMap<String, Integer>();
            valueMap.put("x", x);
            valueMap.put("m", m);
            valueMap.put("a", a);
            valueMap.put("s", s);
        }

        public String toString() {
            return "x:" + this.x + ",m:" + this.m + ",a:" + this.a + ",s:" + this.s;
        }
    }

    private static enum Type {
        LESS_THAN, GREATER_THAN, DIRECT
    }
}