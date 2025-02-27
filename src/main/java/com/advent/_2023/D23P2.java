package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class D23P2 {
    static String[][] map;
    static int WIDTH;
    static int HEIGHT;
    static List<Node> nodes = new ArrayList<>();
    static List<Node> path = new ArrayList<>();
    static Node guardian = null;

    public static void main(String[] args) throws InterruptedException {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("../advent-of-code-input/2023/D23.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        HEIGHT = lines.size();
        WIDTH = lines.getFirst().length();
        map = new String[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            String line = lines.get(i);
            for (int j = 0; j < WIDTH; j++) {
                map[i][j] = String.valueOf(line.charAt(j));
            }
        }

        // remove slopes
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (!map[row][col].equals("#")) {
                    map[row][col] = ".";
                }
            }
        }

        createAllNodes();

        // build trips
        for (Node node : nodes) {
            walkFromNode(node);
        }

        System.out.println(findLengthOfLongestTravel());
    }

    static class Node {
        int row;
        int column;
        List<Trip> trips = new ArrayList<>();
        boolean isInPath = false;
        int tripIndex = -1;
        int totalDistance = 0;

        Node(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    record Trip(Node endNode, int distance) {
    }

    record Point(int row, int col) {
    }

    static void createAllNodes() {
        createAndRegisterNode(0, 1); // start
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                tryCreateNode(row, col);
            }
        }
        createAndRegisterNode(HEIGHT - 1, WIDTH - 2); // exit
    }

    static void tryCreateNode(int row, int col) {
        if (map[row][col].equals("#")) return;
        int a = isFree(row - 1, col);
        int b = isFree(row + 1, col);
        int c = isFree(row, col - 1);
        int d = isFree(row, col + 1);
        if (a + b + c + d < 3) return;
        createAndRegisterNode(row, col);
    }

    static int isFree(int row, int col) {
        if (row < 0 || col < 0 || row >= HEIGHT || col >= WIDTH) return 0;
        return map[row][col].equals("#") ? 0 : 1;
    }

    static void createAndRegisterNode(int row, int col) {
        Node node = new Node(row, col);
        nodes.add(node);
        // overwrite the map cell with the node's index.
        map[row][col] = String.valueOf(nodes.size() - 1);
    }
    
    static void walkFromNode(Node node) {
        int row = node.row;
        int col = node.column;
        walkFromNode2(node, row - 1, col);
        walkFromNode2(node, row + 1, col);
        walkFromNode2(node, row, col - 1);
        walkFromNode2(node, row, col + 1);
    }

    static void walkFromNode2(Node beginNode, int row1, int col1) {
        if (isFree(row1, col1) == 0) {
            return;
        }
        byte[] walked = new byte[WIDTH * HEIGHT];
        walked[beginNode.row * WIDTH + beginNode.column] = 1; // mark the begin node as visited
        walked[row1 * WIDTH + col1] = 1; // mark first step as visited
        Queue<Point> futurePoints = new LinkedList<>();
        futurePoints.add(new Point(row1, col1));
        int distance = 0;
        while (true) {
            if (futurePoints.isEmpty()) {
                throw new RuntimeException("Dead end!");
            }
            distance++;
            Point currentPoint = futurePoints.poll();
            int r = currentPoint.row;
            int c = currentPoint.col;
            if (!map[r][c].equals(".")) {   // found a node
                int indexOfEndNode = Integer.parseInt(map[r][c]);
                Node endNode = nodes.get(indexOfEndNode);
                beginNode.trips.add(new Trip(endNode, distance));
                return;
            }
            walkFromNode3(r - 1, c, walked, futurePoints);
            walkFromNode3(r + 1, c, walked, futurePoints);
            walkFromNode3(r, c - 1, walked, futurePoints);
            walkFromNode3(r, c + 1, walked, futurePoints);
        }
    }

    static void walkFromNode3(int row, int col, byte[] walked, Queue<Point> futurePoints) {
        if (row < 0 || col < 0 || row >= HEIGHT || col >= WIDTH) return;
        int index = row * WIDTH + col;
        if (walked[index] != 0) return;
        if (map[row][col].equals("#")) return;
        walked[index] = 1;
        futurePoints.add(new Point(row, col));
    }

    static int findLengthOfLongestTravel() {
        Node targetNode = nodes.getLast();
        if (targetNode.trips.size() == 1) {
            guardian = targetNode.trips.getFirst().endNode;
        }
        Node startNode = nodes.getFirst();
        path.add(startNode);
        startNode.isInPath = true;
        return search();
    }

    static int search() {
        Node targetNode = nodes.getLast();
        int best = 0;
        while (true) {
            if (path.isEmpty()) {
                return best;
            }
            Node node = path.getLast();
            if (node == targetNode) {
                if (node.totalDistance > best) {
                    best = node.totalDistance;
                }
                if (!path.isEmpty()) {
                    Node popped = path.removeLast();
                    popped.isInPath = false;
                }
                continue;
            }
            if (guardian != null) {
                if (node != guardian && guardian.isInPath) {
                    Node popped = path.removeLast();
                    popped.isInPath = false;
                    continue;
                }
            }
            node.tripIndex++;
            if (node.tripIndex >= node.trips.size()) {
                Node popped = path.removeLast();
                popped.isInPath = false;
                continue;
            }
            Trip trip = node.trips.get(node.tripIndex);
            Node nextNode = trip.endNode;
            if (nextNode.isInPath) {
                continue;
            }
            nextNode.tripIndex = -1;
            nextNode.isInPath = true;
            nextNode.totalDistance = node.totalDistance + trip.distance;
            path.add(nextNode);
        }
    }
}