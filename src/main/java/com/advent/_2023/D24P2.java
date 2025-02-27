package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

// https://adventofcode.com/2023/day/24
// https://topaz.github.io/paste/#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
public class D24P2 {
    public static void main(String[] args) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("../advent-of-code-input/2023/D24.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        List<Hailstone> hailstones = lines.stream().map(Hailstone::from).toList();
        List<Function<Hailstone, Slice>> slicers = List.of(Hailstone::x, Hailstone::y, Hailstone::z);
        List<TreeMap<Long, Hailstone>> collisionRecords = slicers.stream().flatMap(slicer -> hailstones.stream().filter(candidate -> hailstones.stream().filter(h -> !h.equals(candidate)).allMatch(h -> {
            long differenceInPosition = slicer.apply(h).p() - slicer.apply(candidate).p();
            long differenceInVelocity = slicer.apply(h).v() - slicer.apply(candidate).v();
            return differenceInVelocity != 0 && differenceInPosition % differenceInVelocity == 0 && differenceInPosition / differenceInVelocity < 0;
        })).map(candidate ->
                hailstones.stream().filter(h -> !h.equals(candidate)).map(h -> {
                    long collisionTime = (slicer.apply(h).p() - slicer.apply(candidate).p()) / (slicer.apply(candidate).v() - slicer.apply(h).v());
                    return new Collision(collisionTime, h.positionAt(collisionTime));
                }).collect(Collectors.toMap(Collision::when, Collision::hailstone, Hailstone::merge, TreeMap::new)))).toList();
        // somehow we got two results - ignore the second one
        SortedMap<Long, Hailstone> result = collisionRecords.getFirst();
        Iterator<Map.Entry<Long, Hailstone>> i = result.entrySet().iterator();
        Map.Entry<Long, Hailstone> first = i.next();
        Map.Entry<Long, Hailstone> second = i.next();
        long firstCollisionTime = first.getKey();
        Hailstone firstHailstone = first.getValue();
        long secondCollisionTime = second.getKey();
        Hailstone secondHailstone = second.getValue();
        Coordinate3 velocity = secondHailstone.p().add(firstHailstone.p().multiply(-1)).divide(secondCollisionTime - firstCollisionTime);
        Coordinate3 magic = firstHailstone.p().add(velocity.multiply(-firstCollisionTime));
        System.out.println(magic.x() + magic.y() + magic.z());
    }

    record Coordinate3(long x, long y, long z) {
        static Coordinate3 from(String s) {
            long[] a = Arrays.stream(s.split(", +")).mapToLong(Long::valueOf).toArray();
            return new Coordinate3(a[0], a[1], a[2]);
        }

        Coordinate3 add(Coordinate3 other) {
            return new Coordinate3(x + other.x, y + other.y, z + other.z);
        }

        Coordinate3 multiply(long scalar) {
            return new Coordinate3(scalar * x, scalar * y, scalar * z);
        }

        Coordinate3 divide(long scalar) {
            if (x % scalar != 0 || y % scalar != 0 || z % scalar != 0) {
                throw new IllegalArgumentException("division would leave remainder");
            }
            return new Coordinate3(x / scalar, y / scalar, z / scalar);
        }

        @Override
        public String toString() {
            return x + ", " + y + ", " + z;
        }
    }

    record Slice(long p, long v) {
    }

    record Hailstone(Coordinate3 p, Coordinate3 v) {

        static Hailstone from(String s) {
            Coordinate3[] a = Arrays.stream(s.split(" @ +")).map(Coordinate3::from).toArray(Coordinate3[]::new);
            return new Hailstone(a[0], a[1]);
        }

        Hailstone positionAt(long t) {
            return new Hailstone(p.add(v.multiply(t)), v);
        }

        Slice x() {
            return new Slice(p.x(), v.x());
        }

        Slice y() {
            return new Slice(p.y(), v.y());
        }

        Slice z() {
            return new Slice(p.z(), v.z());
        }

        @Override
        public String toString() {
            return p + " @ " + v;
        }

        Hailstone merge(Hailstone other) {
            throw new IllegalStateException();
        }
    }

    record Collision(Long when, Hailstone hailstone) {
    }
}