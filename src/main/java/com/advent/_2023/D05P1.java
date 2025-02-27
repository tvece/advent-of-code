package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// https://adventofcode.com/2023/day/5
public class D05P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2023/D05.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        List<List<Transformation>> transformations = new ArrayList<>();
        int index = 3;
        List<Transformation> ss = new ArrayList<>();
        index = fillTransformations(ss, index, lines);
        ss.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(ss);
        List<Transformation> sf = new ArrayList<>();
        index = fillTransformations(sf, index + 2, lines);
        sf.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(sf);
        List<Transformation> fw = new ArrayList<>();
        index = fillTransformations(fw, index + 2, lines);
        fw.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(fw);
        List<Transformation> wl = new ArrayList<>();
        index = fillTransformations(wl, index + 2, lines);
        wl.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(wl);
        List<Transformation> lt = new ArrayList<>();
        index = fillTransformations(lt, index + 2, lines);
        lt.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(lt);
        List<Transformation> th = new ArrayList<>();
        index = fillTransformations(th, index + 2, lines);
        th.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(th);
        List<Transformation> hl = new ArrayList<>();
        fillTransformations(hl, index + 2, lines);
        hl.sort((a, b) -> (int) (a.source - b.source));
        transformations.add(hl);

        String firstLine = lines.getFirst();
        firstLine = firstLine.substring(firstLine.indexOf(" ") + 1);
        List<Double> seedNumbers = Arrays.stream(firstLine.split(" "))
                .map(Double::parseDouble).toList();

        Double minimum = Double.MAX_VALUE;
        for (Double transfomedValue : seedNumbers) {
            for (List<Transformation> transformer : transformations) {
                transfomedValue = transform(transfomedValue, transformer);
            }
            if (minimum > transfomedValue) {
                minimum = transfomedValue;
                System.out.println(minimum);
            }
        }
        System.out.printf("%.9f", minimum);
    }

    private static int fillTransformations(List<Transformation> transformations, int index, List<String> lines) {
        String line = lines.get(index);
        while (!line.isEmpty()) {
            List<Double> record = Arrays.stream(line.split(" ")).map(Double::parseDouble).toList();
            transformations.add(new Transformation(record.get(1), record.get(0), record.get(2)));
            index++;
            if (index == lines.size()) {
                return index;
            }
            line = lines.get(index);
        }
        return index;
    }

    private static Double transform(Double source, List<Transformation> transformations) {
        for (Transformation transformation : transformations) {
            if (source < transformation.source) {
                return source;
            }
            if (source < (transformation.source + transformation.range)) {
                return source - transformation.source + transformation.destination;
            }
        }
        return source;
    }

    private static class Transformation {
        public Double source;
        public Double destination;
        public Double range;

        public Transformation(Double source, Double destination, Double range) {
            this.source = source;
            this.destination = destination;
            this.range = range;
        }
    }
}
