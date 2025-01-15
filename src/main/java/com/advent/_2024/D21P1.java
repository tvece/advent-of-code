package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D21P1 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D21.txt");
        List<String> input;
        try {
            input = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (
                IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }


        long result = 0;
        for (String line : input) {
            System.out.println("line: " + line);

            NumericBot numericBot = new NumericBot(10);
            long length = 0;
            for (char character : line.toCharArray()) {
                length += getInstructionsLength(numericBot.press(character)[0], 0, 2);
            }

            int inputInt = Integer.parseInt(line.substring(0, line.length() - 1));

            System.out.println("line result: " + inputInt + " * " + length);
            result += inputInt * length;


        }
        System.out.println("Result: " + result);
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");

    }

    private static long getInstructionsLength(String instructions, int depth, int expectedDepth) {
        if (depth == expectedDepth) {
            return instructions.length();
        }


        DirectionalBot db = new DirectionalBot(1);
        long result = 0;
        for (char instruction : instructions.toCharArray()) {
            result += getInstructionsLength(db.press(instruction), depth + 1, expectedDepth);
        }
        return result;
    }

    private static class DirectionalBot {
        // @formatter:off
        /**
         *     +---+---+
         *     | ^ | A |
         * +---+---+---+
         * | < | v | > |
         * +---+---+---+
         * converts to:
         *     +---+---+
         *     | 0 | 1 |
         * +---+---+---+
         * | 2 | 3 | 4 |
         * +---+---+---+
         */
        int position;
        // @formatter:on

        public DirectionalBot(int position) {
            this.position = position;
        }

        /**
         * updates position and executes press of character
         *
         * @return move sets required for the execution
         */
        public String press(char character) {
            switch (position) {
                case 0:
                    switch (character) {
                        case '^':
                            position = 0;
                            return "A";
                        case 'A':
                            position = 1;
                            return ">A";
                        case '<':
                            position = 2;
                            return "v<A";
                        case 'v':
                            position = 3;
                            return "vA";
                        case '>':
                            position = 4;
                            return "v>A";

                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 1:
                    switch (character) {
                        case '^':
                            position = 0;
                            return "<A";
                        case 'A':
                            position = 1;
                            return "A";
                        case '<':
                            position = 2;
                            return "v<<A";
                        case 'v':
                            position = 3;
                            return "<vA";
                        case '>':
                            position = 4;
                            return "vA";
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 2:
                    switch (character) {
                        case '^':
                            position = 0;
                            return ">^A";
                        case 'A':
                            position = 1;
                            return ">>^A";
                        case '<':
                            position = 2;
                            return "A";
                        case 'v':
                            position = 3;
                            return ">A";
                        case '>':
                            position = 4;
                            return ">>A";
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 3:
                    switch (character) {
                        case '^':
                            position = 0;
                            return "^A";
                        case 'A':
                            position = 1;
                            return ">^A";
                        case '<':
                            position = 2;
                            return "<A";
                        case 'v':
                            position = 3;
                            return "A";
                        case '>':
                            position = 4;
                            return ">A";
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 4:
                    switch (character) {
                        case '^':
                            position = 0;
                            return "<^A";
                        case 'A':
                            position = 1;
                            return "^A";
                        case '<':
                            position = 2;
                            return "<<A";
                        case 'v':
                            position = 3;
                            return "<A";
                        case '>':
                            position = 4;
                            return "A";
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                default:
                    throw new RuntimeException("Unexpected move instruction: " + character);
            }

        }

    }

    private static class NumericBot {
        // @formatter:off
        /**
         * +---+---+---+
         * | 7 | 8 | 9 |
         * +---+---+---+
         * | 4 | 5 | 6 |
         * +---+---+---+
         * | 1 | 2 | 3 |
         * +---+---+---+
         *     | 0 | A |
         *     +---+---+
         * converts to:
         * +---+---+---+
         * | 7 | 8 | 9 |
         * +---+---+---+
         * | 4 | 5 | 6 |
         * +---+---+---+
         * | 1 | 2 | 3 |
         * +---+---+---+
         *     | 0 | 10 |
         *     +---+---+
         */
        int position;
        // @formatter:on

        public NumericBot(int position) {
            this.position = position;
        }

        /**
         * updates position and executes press of character
         *
         * @return possible move sets required for the execution
         */
        public String[] press(char character) {
            switch (position) {
                case 0:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"A"};
                        case '1':
                            this.position = 1;
                            return new String[]{"^<A",};
                        case '2':
                            this.position = 2;
                            return new String[]{"^A"};
                        case '3':
                            this.position = 3;
                            return new String[]{"^>A",};
                        case '4':
                            this.position = 4;
                            return new String[]{"^<^A"};
                        case '5':
                            this.position = 5;
                            return new String[]{"^^A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"^^>A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"^<^^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"^^^A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^^^>A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{">A"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 1:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{">vA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"A"};
                        case '2':
                            this.position = 2;
                            return new String[]{">A"};
                        case '3':
                            this.position = 3;
                            return new String[]{">>A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"^A"};
                        case '5':
                            this.position = 5;
                            return new String[]{">^A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"^>>A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"^^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"^^>A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^^>>A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{">>vA"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 2:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"vA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"<A"};
                        case '2':
                            this.position = 2;
                            return new String[]{"A"};
                        case '3':
                            this.position = 3;
                            return new String[]{">A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"^<A"};
                        case '5':
                            this.position = 5;
                            return new String[]{"^A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"^>A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"<^^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"^^A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^^>A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"v>A"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 3:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"<vA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"<<A"};
                        case '2':
                            this.position = 2;
                            return new String[]{"<A"};
                        case '3':
                            this.position = 3;
                            return new String[]{"A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"<<^A"};
                        case '5':
                            this.position = 5;
                            return new String[]{"<^A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"^A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"<<^^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"<^^A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^^A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"vA"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 4:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{">vvA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"vA"};
                        case '2':
                            this.position = 2;
                            return new String[]{"v>A"};
                        case '3':
                            this.position = 3;
                            return new String[]{"v>>A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"A"};
                        case '5':
                            this.position = 5;
                            return new String[]{">A"};
                        case '6':
                            this.position = 6;
                            return new String[]{">>A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"^>A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^>>A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{">>vvA"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 5:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"vvA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"<vA"};
                        case '2':
                            this.position = 2;
                            return new String[]{"vA"};
                        case '3':
                            this.position = 3;
                            return new String[]{"v>A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"^A"};
                        case '5':
                            this.position = 5;
                            return new String[]{"A"};
                        case '6':
                            this.position = 6;
                            return new String[]{">A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"<^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"^A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^>A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"vv>A"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 6:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"<vvA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"<<vA"};
                        case '2':
                            this.position = 2;
                            return new String[]{"<vA"};
                        case '3':
                            this.position = 3;
                            return new String[]{"vA"};
                        case '4':
                            this.position = 4;
                            return new String[]{"<<A"};
                        case '5':
                            this.position = 5;
                            return new String[]{"<A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"<<^A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"<^A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"vvA"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 7:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{">vvvA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"vvA"};
                        case '2':
                            this.position = 2;
                            return new String[]{"vv>A"};
                        case '3':
                            this.position = 3;
                            return new String[]{"vv>>A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"vA"};
                        case '5':
                            this.position = 5;
                            return new String[]{"v>A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"v>>A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"A"};
                        case '8':
                            this.position = 8;
                            return new String[]{">A"};
                        case '9':
                            this.position = 9;
                            return new String[]{">>A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{">>vvvA"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 8:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"vvvA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"<vvA"};
                        case '2':
                            this.position = 2;
                            return new String[]{"vvA"};
                        case '3':
                            this.position = 3;
                            return new String[]{"vv>A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"<vA"};
                        case '5':
                            this.position = 5;
                            return new String[]{"vA"};
                        case '6':
                            this.position = 6;
                            return new String[]{">vA"};
                        case '7':
                            this.position = 7;
                            return new String[]{"<A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"A"};
                        case '9':
                            this.position = 9;
                            return new String[]{">A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"vvv>A"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 9:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"<vvvA"};
                        case '1':
                            this.position = 1;
                            return new String[]{"<<vvA"};
                        case '2':
                            this.position = 2;
                            return new String[]{"<vvA"};
                        case '3':
                            this.position = 3;
                            return new String[]{"vvA"};
                        case '4':
                            this.position = 4;
                            return new String[]{"<<vA"};
                        case '5':
                            this.position = 5;
                            return new String[]{"<vA"};
                        case '6':
                            this.position = 6;
                            return new String[]{"vA"};
                        case '7':
                            this.position = 7;
                            return new String[]{"<<A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"<A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"vvvA"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                case 10:
                    switch (character) {
                        case '0':
                            this.position = 0;
                            return new String[]{"<A"};
                        case '1':
                            this.position = 1;
                            return new String[]{"^<<A"};
                        case '2':
                            this.position = 2;
                            return new String[]{"<^A"};
                        case '3':
                            this.position = 3;
                            return new String[]{"^A"};
                        case '4':
                            this.position = 4;
                            return new String[]{"^^<<A"};
                        case '5':
                            this.position = 5;
                            return new String[]{"<^^A"};
                        case '6':
                            this.position = 6;
                            return new String[]{"^^A"};
                        case '7':
                            this.position = 7;
                            return new String[]{"^^^<<A"};
                        case '8':
                            this.position = 8;
                            return new String[]{"<^^^A"};
                        case '9':
                            this.position = 9;
                            return new String[]{"^^^A"};
                        case 'A':
                            this.position = 10;
                            return new String[]{"A"};
                        default:
                            throw new RuntimeException("Unexpected move instruction: " + character);
                    }
                default:
                    throw new RuntimeException("Unexpected save position: " + position);
            }
        }

    }
}
