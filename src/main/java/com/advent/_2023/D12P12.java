package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

// https://adventofcode.com/2023/day/12
public class D12P12 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D12.txt");
        long part1 = 0;
        long part2 = 0;
        Map<Input, Long> cache = new HashMap<>();
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            for (Iterator<String> it = lines.iterator(); it.hasNext(); ) {
                String line = it.next();
                String[] parts = line.split(" ");
                List<Integer> regularGroups = Stream.of(parts[1].split(",")).map(Integer::parseInt).toList();
                part1 += countPermutations(parts[0], regularGroups, cache);
                String unfoldedCondition = unfoldCondition(parts[0], 5);
                List<Integer> unfoldedGroups = unfoldGroups(regularGroups, 5);
                part2 += countPermutations(unfoldedCondition, unfoldedGroups, cache);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    private static String unfoldCondition(String condition, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times - 1; i++) {
            sb.append(condition);
            sb.append("?");
        }
        sb.append(condition);
        return sb.toString();
    }

    private static List<Integer> unfoldGroups(List<Integer> groups, int times) {
        List<Integer> unfoldedGroups = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            unfoldedGroups.addAll(groups);
        }
        return unfoldedGroups;
    }


    private static long countPermutations(String condition, List<Integer> groups, Map<Input, Long> cache) {
        Input input = new Input(condition, groups);
        if (cache.containsKey(input)) {
            return cache.get(input);
        }

        if (condition.isEmpty()) {
            return groups.isEmpty() ? 1 : 0;
        }

        char firstChar = condition.charAt(0);
        long permutations;
        if (firstChar == '.') {
            // working
            permutations = countPermutations(condition.substring(1), groups, cache);
        } else if (firstChar == '?') {
            // unknown - try both working and damaged
            permutations = countPermutations("." + condition.substring(1), groups, cache) +
                    countPermutations("#" + condition.substring(1), groups, cache);
        } else {
            // not working
            if (groups.isEmpty()) {
                permutations = 0;
            } else {
                int nrDamaged = groups.getFirst();
                // does the damaged spring fit into remaining condition?
                if (nrDamaged <= condition.length() &&
                        condition.chars().limit(nrDamaged).allMatch(c -> c == '#' || c == '?')) {
                    List<Integer> newGroups = groups.subList(1, groups.size());
                    if (nrDamaged == condition.length()) {
                        // if it is equal there must be no group to be processed or the permutation is not valid
                        permutations = newGroups.isEmpty() ? 1 : 0;
                    } else if (condition.charAt(nrDamaged) == '.') {
                        // next spring is valid
                        permutations = countPermutations(condition.substring(nrDamaged + 1), newGroups, cache);
                    } else if (condition.charAt(nrDamaged) == '?') {
                        // damaged group is always followed by working spring
                        permutations = countPermutations("." + condition.substring(nrDamaged + 1), newGroups, cache);
                    } else {
                        // damaged group is followed by another damaged spring - not valid
                        permutations = 0;
                    }
                } else {
                    // size of the group is greater that the remaining condition or the next nrDamaged springs are not either damaged or unknown
                    permutations = 0;
                }
            }
        }
        cache.put(input, permutations);
        return permutations;
    }

    public record Input(String condition, List<Integer> groups) {
    }
}
