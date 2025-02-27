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

// https://adventofcode.com/2023/day/19
public class D19P2 {

    final static String ACCEPTED = "A";
    final static String REJECTED = "R";
    final static String INITIAL = "in";

    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("../advent-of-code-input/2023/D19.txt");
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

        System.out.println(exploreWorkflow(1, 4000, 1, 4000, 1, 4000, 1, 4000, "in", instructions));
    }

    private static long exploreWorkflow(int minX, int maxX, int minM, int maxM, int minA, int maxA, int minS, int maxS, String currentInstructionId, Map<String, Instruction> instructions) {
        if (currentInstructionId.equals(REJECTED)) {
            return 0;
        }

        if (currentInstructionId.equals(ACCEPTED)) {
            System.out.println((maxX - minX + 1) + "*" + (maxM - minM + 1) + "*" + (maxA - minA + 1) + "*" + (maxS - minS + 1));
            System.out.println("x:" + minX + "-" + maxX + "; " + "m:" + minM + "-" + maxM + "; " + "a:" + minA + "-" + maxA + "; " + "s:" + minS + "-" + maxS + "; ");
            System.out.println();
            return (long) (maxX - minX + 1) * (maxM - minM + 1) * (maxA - minA + 1) * (maxS - minS + 1);
        }

        Instruction instruction = instructions.get(currentInstructionId);
        long result = 0;
        for (Step step : instruction.steps) {
            if (step.type.equals(Type.DIRECT)) {
                return result + exploreWorkflow(minX, maxX, minM, maxM, minA, maxA, minS, maxS, step.destination, instructions);
            } else {
                switch (step.toCheckElement) {
                    case "x":
                        if (step.type.equals(Type.GREATER_THAN)) {
                            int newMinX = Math.max(minX, step.threshold + 1);
                            if (newMinX <= maxX) {
                                result += exploreWorkflow(newMinX, maxX, minM, maxM, minA, maxA, minS, maxS, step.destination, instructions);
                                if (minX < newMinX) {
                                    maxX = newMinX - 1;
                                } else {
                                    return result;
                                }
                            }
                        } else if (step.type.equals(Type.LESS_THAN)) {
                            int newMaxX = Math.min(maxX, step.threshold - 1);
                            if (minX <= newMaxX) {
                                result += exploreWorkflow(minX, newMaxX, minM, maxM, minA, maxA, minS, maxS, step.destination, instructions);
                                if (maxX > newMaxX) {
                                    minX = newMaxX + 1;
                                } else {
                                    return result;
                                }
                            }
                        }
                        break;
                    case "m":
                        if (step.type.equals(Type.GREATER_THAN)) {
                            int newMinM = Math.max(minM, step.threshold + 1);
                            if (newMinM <= maxM) {
                                result += exploreWorkflow(minX, maxX, newMinM, maxM, minA, maxA, minS, maxS, step.destination, instructions);
                                if (minM < newMinM) {
                                    maxM = newMinM - 1;
                                } else {
                                    return result;
                                }
                            }
                        } else if (step.type.equals(Type.LESS_THAN)) {
                            int newMaxM = Math.min(maxM, step.threshold - 1);
                            if (minM <= newMaxM) {
                                result += exploreWorkflow(minX, maxX, minM, newMaxM, minA, maxA, minS, maxS, step.destination, instructions);
                                if (maxM > newMaxM) {
                                    minM = newMaxM + 1;
                                } else {
                                    return result;
                                }
                            }
                        }
                        break;
                    case "a":
                        if (step.type.equals(Type.GREATER_THAN)) {
                            int newMinA = Math.max(minA, step.threshold + 1);
                            if (newMinA <= maxA) {
                                result += exploreWorkflow(minX, maxX, minM, maxM, newMinA, maxA, minS, maxS, step.destination, instructions);
                                if (minA < newMinA) {
                                    maxA = newMinA - 1;
                                } else {
                                    return result;
                                }
                            }
                        } else if (step.type.equals(Type.LESS_THAN)) {
                            int newMaxA = Math.min(maxA, step.threshold - 1);
                            if (minA <= newMaxA) {
                                result += exploreWorkflow(minX, maxX, minM, maxM, minA, newMaxA, minS, maxS, step.destination, instructions);
                                if (maxA > newMaxA) {
                                    minA = newMaxA + 1;
                                } else {
                                    return result;
                                }
                            }
                        }
                        break;
                    case "s":
                        if (step.type.equals(Type.GREATER_THAN)) {
                            int newMinS = Math.max(minS, step.threshold + 1);
                            if (newMinS <= maxS) {
                                result += exploreWorkflow(minX, maxX, minM, maxM, minA, maxA, newMinS, maxS, step.destination, instructions);
                                if (minS < newMinS) {
                                    maxS = newMinS - 1;
                                } else {
                                    return result;
                                }
                            }
                        } else if (step.type.equals(Type.LESS_THAN)) {
                            int newMaxS = Math.min(maxS, step.threshold - 1);
                            if (minS <= newMaxS) {
                                result += exploreWorkflow(minX, maxX, minM, maxM, minA, maxA, minS, newMaxS, step.destination, instructions);
                                if (maxS > newMaxS) {
                                    minS = newMaxS + 1;
                                } else {
                                    return result;
                                }
                            }
                        }
                        break;
                    default:
                        throw new RuntimeException("Unsupported element " + step.toCheckElement);
                }
            }
        }
        throw new RuntimeException("No step of instruction " + instruction + " was accepted");
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
            if (description.contains(":")) {
                toCheckElement = description.substring(0, 1);
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
            return (this.toCheckElement != null ? (this.toCheckElement + " ") : "") + this.type + " " + this.threshold + " " + this.destination;
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