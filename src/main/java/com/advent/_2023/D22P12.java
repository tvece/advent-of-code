package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// https://adventofcode.com/2023/day/22
public class D22P12 {
    public static void main(String[] args) throws InterruptedException {
        Path filePath = Paths.get("../advent-of-code-input/2023/D22.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        List<Brick> bricks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            List<Coord> snapshot = parseBrickLine(lines.get(i));
            bricks.add(new Brick(i, snapshot));
        }

        // lowest bricks settle first
        bricks.sort(Comparator.comparingInt(b -> b.minSnapshotZ));

        // map of "x,y" and their highest occupied z
        Map<String, Integer> colHighest = new HashMap<>();

        // for each brick (sorted) compute how far it can fall and populate colHighest
        for (Brick brick : bricks) {
            int fallDistance = Integer.MAX_VALUE;
            // determine allowed fall of each cube
            for (Coord c : brick.snapshotCubes) {
                String key = c.x + "," + c.y;
                int currentHighest = colHighest.getOrDefault(key, 0);
                int allowed = c.z - (currentHighest + 1);
                if (allowed < fallDistance) {
                    fallDistance = allowed;
                }
            }

            // calculate final cuber
            for (Coord c : brick.snapshotCubes) {
                brick.finalCubes.add(new Coord(c.x, c.y, c.z - fallDistance));
                // Update the column occupancy.
                String key = c.x + "," + c.y;
                int newZ = c.z - fallDistance;
                colHighest.put(key, Math.max(colHighest.getOrDefault(key, 0), newZ));
            }
        }

        // map coordinates to brick ids
        Map<Coord, Integer> occupancy = new HashMap<>();
        for (Brick brick : bricks) {
            for (Coord c : brick.finalCubes) {
                occupancy.put(c, brick.id);
            }
        }

        // calculate supportSet bricks
        for (Brick brick : bricks) {
            for (Coord c : brick.finalCubes) {
                if (c.z == 1) {
                    brick.supportSet.add(-1); // ground support
                } else {
                    Coord below = new Coord(c.x, c.y, c.z - 1);
                    Integer supporter = occupancy.get(below);
                    if (supporter != null && supporter != brick.id) {
                        brick.supportSet.add(supporter);
                    }
                }
            }
        }

        // for each brick calculate which other bricks depends on it
        Map<Integer, List<Integer>> supportsMap = new HashMap<>();
        for (Brick brick : bricks) {
            for (Integer sup : brick.supportSet) {
                if (sup == -1) continue; // skip ground
                supportsMap.computeIfAbsent(sup, k -> new ArrayList<>()).add(brick.id);
            }
        }
        // store the dependencies in bricks
        for (Brick brick : bricks) {
            List<Integer> deps = supportsMap.getOrDefault(brick.id, new ArrayList<>());
            brick.dependents.addAll(deps);
        }

        // determine which bricks are safe
        int safeCount = 0;
        int sumOtherFallen = 0;
        for (Brick removedBrick : bricks) {
            // simulate fall
            Set<Integer> fallen = new HashSet<>();
            fallen.add(removedBrick.id);
            boolean changed = true;
            while (changed) {
                changed = false;
                for (Brick brick : bricks) {
                    if (!fallen.contains(brick.id)) {
                        // remove supports from fallen bricks
                        Set<Integer> remaining = new HashSet<>(brick.supportSet);
                        remaining.removeAll(fallen);
                        // no support remains -> brick falls.
                        if (remaining.isEmpty()) {
                            fallen.add(brick.id);
                            changed = true;
                        }
                    }
                }
            }
            // only current brick falls -> it is safe
            if (fallen.size() == 1) {
                safeCount++;
            }
            // count other bricks that fell because this fall
            sumOtherFallen += fallen.size() - 1;
        }

        System.out.println("Part 1: " + safeCount);
        System.out.println("Part 2: " + sumOtherFallen);
    }

    static class Coord {
        int x, y, z;

        public Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coord c)) return false;
            return x == c.x && y == c.y && z == c.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + "," + z + ")";
        }
    }

    static class Brick {
        int id;
        List<Coord> snapshotCubes;
        List<Coord> finalCubes;  // after falling
        int minSnapshotZ; // lowest z among snapshot cubes
        Set<Integer> supportSet; // brick ids (or -1 for ground) supporting this brick
        List<Integer> dependents; // bricks that depend on this brick (reverse of supportSet)

        Brick(int id, List<Coord> snapshotCubes) {
            this.id = id;
            this.snapshotCubes = snapshotCubes;
            this.finalCubes = new ArrayList<>();
            this.supportSet = new HashSet<>();
            this.dependents = new ArrayList<>();
            // find min z
            int minz = Integer.MAX_VALUE;
            for (Coord c : snapshotCubes) {
                if (c.z < minz) minz = c.z;
            }
            this.minSnapshotZ = minz;
        }
    }

    static List<Coord> parseBrickLine(String line) {
        String[] parts = line.split("~");

        String[] p1 = parts[0].split(",");
        String[] p2 = parts[1].split(",");
        int x1 = Integer.parseInt(p1[0].trim());
        int y1 = Integer.parseInt(p1[1].trim());
        int z1 = Integer.parseInt(p1[2].trim());
        int x2 = Integer.parseInt(p2[0].trim());
        int y2 = Integer.parseInt(p2[1].trim());
        int z2 = Integer.parseInt(p2[2].trim());

        // collect all cubes of brick
        List<Coord> cubes = new ArrayList<>();
        int dx = Integer.compare(x2, x1);
        int dy = Integer.compare(y2, y1);
        int dz = Integer.compare(z2, z1);
        int len = Math.abs(x2 - x1) + Math.abs(y2 - y1) + Math.abs(z2 - z1) + 1;
        for (int i = 0; i < len; i++) {
            int x = x1 + dx * i;
            int y = y1 + dy * i;
            int z = z1 + dz * i;
            cubes.add(new Coord(x, y, z));
        }
        return cubes;
    }
}
