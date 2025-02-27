package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// https://adventofcode.com/2023/day/25
public class D25 {

    static final int WIRES_TO_CUT = 3;

    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("../advent-of-code-input/2023/D25.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        // build graph
        Map<String, Set<String>> graph = new HashMap<>();
        for (String line : lines) {
            String[] parts = line.split(": ");

            String node = parts[0];
            String[] connections = parts[1].split(" ");

            graph.putIfAbsent(node, new HashSet<>());
            for (String neighbor : connections) {
                graph.putIfAbsent(neighbor, new HashSet<>());
                graph.get(node).add(neighbor);
                graph.get(neighbor).add(node);
            }
        }

        // find the three edges to remove using Karger's algorithm
        // https://en.wikipedia.org/wiki/Karger%27s_algorithm
        List<String[]> edgesToRemove = findMinCut(graph);

        if (edgesToRemove.size() != 3) {
            throw new RuntimeException("Error: Could not find exactly 3 edges to remove.");
        }

        // remove those edges
        for (String[] edge : edgesToRemove) {
            graph.get(edge[0]).remove(edge[1]);
            graph.get(edge[1]).remove(edge[0]);
        }

        // find sizes of the two disconnected groups
        List<Integer> groupSizes = getConnectedComponentSizes(graph);

        if (groupSizes.size() == 2) {
            System.out.println("Result: " + (groupSizes.get(0) * groupSizes.get(1)));
        } else {
            throw new RuntimeException("Graph did not split into exactly two groups.");
        }
    }

    private static List<String[]> findMinCut(Map<String, Set<String>> graph) {
        List<String[]> minCutEdges;
        List<String[]> allEdges = new ArrayList<>();

        // convert adjacency list to edge list
        for (String node : graph.keySet()) {
            for (String neighbor : graph.get(node)) {
                if (node.compareTo(neighbor) < 0) { // Avoid duplicate edges
                    allEdges.add(new String[]{node, neighbor});
                }
            }
        }

        Random rand = new Random();

        while (true) { // keep running until we get exactly two groups
            // copy graph structure
            Map<String, String> parent = new HashMap<>();
            for (String node : graph.keySet()) parent.put(node, node);

            int components = graph.size();
            List<String[]> edgesUsed = new ArrayList<>(allEdges);

            while (components > 2) {
                // pick a random edge to contract
                int index = rand.nextInt(edgesUsed.size());
                String[] edge = edgesUsed.remove(index);
                String u = find(parent, edge[0]);
                String v = find(parent, edge[1]);

                if (!u.equals(v)) {
                    union(parent, u, v);
                    components--;
                }
            }

            // find the edges that cross the cut
            List<String[]> cutEdges = new ArrayList<>();
            for (String[] edge : allEdges) {
                String u = find(parent, edge[0]);
                String v = find(parent, edge[1]);
                if (!u.equals(v)) {
                    cutEdges.add(edge);
                }
            }

            if (cutEdges.size() == WIRES_TO_CUT) {
                minCutEdges = cutEdges;
                break;
            }
        }
        return minCutEdges;
    }

    // find function for union-find
    private static String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent, parent.get(node)));
        }
        return parent.get(node);
    }

    // union function for union-find
    private static void union(Map<String, String> parent, String u, String v) {
        parent.put(find(parent, u), find(parent, v));
    }

    private static List<Integer> getConnectedComponentSizes(Map<String, Set<String>> graph) {
        Set<String> visited = new HashSet<>();
        List<Integer> sizes = new ArrayList<>();

        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                int size = bfsSize(graph, node, visited);
                sizes.add(size);
            }
        }

        return sizes;
    }

    // BFS function to get the size of a connected component
    private static int bfsSize(Map<String, Set<String>> graph, String start, Set<String> visited) {
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);
        int size = 0;

        while (!queue.isEmpty()) {
            String node = queue.poll();
            size++;

            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return size;
    }
}