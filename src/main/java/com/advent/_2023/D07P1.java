package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class D07P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("src/main/resources/2023/D07.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        List<Hand> hands = lines.stream().map(Hand::new).sorted().toList();
        long result = 0;
        int index = 1;
        for (Hand hand : hands) {
            System.out.println(hand.inputCards + " " + hand.type + " " + hand.bid + "*" + index);
            result += ((long) hand.bid * index++);
        }
        System.out.print(result);
    }

    private static class Hand implements Comparable<Hand> {
        List<Integer> cards;
        String inputCards;
        Integer bid;
        Integer type;

        public Hand(String line) {
            String[] split = line.split(" ");
            inputCards = split[0];
            setCards(split[0]);
            bid = Integer.parseInt(split[1]);
            type = getType();
        }

        private void setCards(String charRepresentation) {
            cards = new ArrayList<>();
            for (Character character : charRepresentation.toCharArray()) {
                switch (character) {
                    case 'A':
                        cards.add(14);
                        break;
                    case 'K':
                        cards.add(13);
                        break;
                    case 'Q':
                        cards.add(12);
                        break;
                    case 'J':
                        cards.add(11);
                        break;
                    case 'T':
                        cards.add(10);
                        break;
                    default:
                        cards.add(Integer.parseInt(String.valueOf(character)));
                }
            }
        }

        @Override
        public int compareTo(Hand o) {
            int typeCompare = this.type.compareTo(o.type);
            if (typeCompare == 0) {
                for (int i = 0; i < this.cards.size(); i++) {
                    Integer thisCard = this.cards.get(i);
                    Integer otherCard = o.cards.get(i);
                    if (!Objects.equals(thisCard, otherCard)) {
                        return thisCard.compareTo(otherCard);
                    }
                }
            }
            return typeCompare;
        }

        private Integer getType() {
            LinkedHashMap<Integer, Long> groups = cards.stream()
                    .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));
            List<Long> counters = groups.values().stream().sorted(Comparator.reverseOrder()).toList();

            if (counters.size() == 1) {
                // Five of a kind, where all five cards have the same label: AAAAA
                return 7;
            } else if (counters.get(0) == 4) {
                // Four of a kind, where four cards have the same label and one card has a
                // different label: AA8AA
                return 6;
            } else if (counters.get(0) == 3 && counters.get(1) == 2) {
                // Full house, where three cards have the same label, and the remaining two
                // cards share a different label: 23332
                return 5;
            } else if ((counters.get(0) == 3 && counters.get(1) == 1)) {
                // Three of a kind, where three cards have the same label, and the remaining two
                // cards are each different from any other card in the hand: TTT98
                return 4;
            } else if ((counters.get(0) == 2 && counters.get(1) == 2)) {
                // Two pair, where two cards share one label, two other cards share a second
                // label, and the remaining card has a third label: 23432
                return 3;
            } else if ((counters.get(0) == 2 && counters.size() == 4)) {
                // One pair, where two cards share one label, and the other three cards have a
                // different label from the pair and each other: A23A4
                return 2;
            }
            // High card, where all cards' labels are distinct: 23456
            return 1;
        }
    }
}