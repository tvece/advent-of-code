package com.advent._2025;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

//TODO: cleanup + is there better way to do this?
// https://adventofcode.com/2025/day/2
public class D02P1 {

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2025/D02.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }
        String input = lines.getFirst();
        List<Range> ranges = new ArrayList<>();
        AtomicLong maxNumber = new AtomicLong(0L);
        Stream.of(input.split(",")).forEach((stringRange) -> {
            String[] rangeSplit = stringRange.split("-");
            Range range = new Range(Long.parseLong(rangeSplit[0]), Long.parseLong(rangeSplit[1]));
            ranges.add(range);
            if (range.end > maxNumber.get()) {
                maxNumber.set(range.end);
            }
        });

        // collect accepted IDs
        List<Long> acceptedIDs = new ArrayList<>();
        String stringMaxNumber = String.valueOf(maxNumber);
        long maxHalf = Long.parseLong(stringMaxNumber.substring(0, stringMaxNumber.length() / 2));
        for (long i = 1; i <= maxHalf; i++) {
            long acceptedID = Double.valueOf((i * Math.pow(10, String.valueOf(i).length())) + i).longValue();
            //System.out.println(i);
            //System.out.println(acceptedID);
            acceptedIDs.add(acceptedID);
        }

        long result = 0;
        //check how many accepted ids are in each range
        for (Range range : ranges) {
            int firstIDIndex = -1;
            long startLookupValue = range.start();
            int lookupIndex = 0;
            while (true) {
                if (startLookupValue <= acceptedIDs.get(lookupIndex) && range.end() > acceptedIDs.get(lookupIndex)) {
                    firstIDIndex = lookupIndex;
                    break;
                }
                if (acceptedIDs.get(lookupIndex) > range.end()) {
                    break;
                }
                lookupIndex++;
            }

            if (firstIDIndex == -1) {
                continue;
            }

            int lastIdIndex = -1;
            long endLookupValue = range.end();
            lookupIndex = acceptedIDs.size() - 1;
            while (lookupIndex > firstIDIndex) {
                if (endLookupValue >= acceptedIDs.get(lookupIndex)) {
                    lastIdIndex = lookupIndex;
                    break;
                }
                lookupIndex--;
            }

            if (lastIdIndex == -1) {
                lastIdIndex = firstIDIndex;
            }

            for (int i = firstIDIndex; i <= lastIdIndex; i++) {
                result += acceptedIDs.get(i);
            }
        }
        System.out.println(result);
    }

    record Range(long start, long end) {
    }
}