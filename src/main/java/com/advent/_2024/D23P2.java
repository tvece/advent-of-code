package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class D23P2 {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Path filePath = Paths.get("src/main/resources/2024/D23.txt");
        Map<String, Set<String>> neighbors = new HashMap<>();

        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                String[] split = line.split("-");
                if (neighbors.containsKey(split[0])) {
                    neighbors.get(split[0]).add(split[1]);
                } else {
                    neighbors.put(split[0], new HashSet<>(List.of(split[1])));
                }

                if (neighbors.containsKey(split[1])) {
                    neighbors.get(split[1]).add(split[0]);
                } else {
                    neighbors.put(split[1], new HashSet<>(List.of(split[0])));
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        Set<String> P = new HashSet<>(neighbors.keySet());
        List<String> maxClique = new ArrayList<>(bronKerbosch(new HashSet<>(), P, new HashSet<>(), neighbors, Collections.emptySet()));
        Collections.sort(maxClique);
        System.out.println("result: " + String.join(",", maxClique));
        System.out.println("Finished in: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private static Set<String> bronKerbosch(Set<String> R,
                                            Set<String> P,
                                            Set<String> X,
                                            Map<String, Set<String>> neighbors,
                                            Set<String> bestCliqueSoFar) {

        if (P.isEmpty() && X.isEmpty()) {
            return (R.size() > bestCliqueSoFar.size()) ? R : bestCliqueSoFar;
        }

        Set<String> bestClique = bestCliqueSoFar;

        for (String v : new HashSet<>(P)) {
            Set<String> newR = new HashSet<>(R);
            newR.add(v);

            Set<String> newP = new HashSet<>(P);
            newP.retainAll(new HashSet<>(neighbors.get(v)));

            Set<String> newX = new HashSet<>(X);
            newX.retainAll(new HashSet<>(neighbors.get(v)));

            Set<String> candidateClique = bronKerbosch(newR, newP, newX, neighbors, bestClique);
            if (candidateClique.size() > bestClique.size()) {
                bestClique = candidateClique;
            }

            P.remove(v);
            X.add(v);
        }

        return bestClique;
    }
}
