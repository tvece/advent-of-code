package com.advent._2024;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// https://adventofcode.com/2024/day/11

public class D11P12 {

    // PART 1
    public static final int CYCLES = 25;
    // PART 2
    //public static final int CYCLES = 75;

    public static void main(String[] args) {
        Path filePath = Paths.get("../advent-of-code-input/2024/D11.txt");
        String input;
        try {
            input = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input data!", e);
        }

        String[] parts = input.split(" ");

        HashMap<Long, Long> stoneCount = new HashMap<>();
        for (String part : parts) {
            updateCountMap(stoneCount, Long.parseLong(part), 1);
        }
        Map<Long, ValueTransformationCacheItem> valueTransformationCache = new HashMap<>();
        for (int i = 0; i < CYCLES; i++) {
            HashMap<Long, Long> nextStoneCount = new HashMap<>();
            for (Long stone : stoneCount.keySet()) {
                if (valueTransformationCache.containsKey(stone)) {
                    ValueTransformationCacheItem cacheValue = valueTransformationCache.get(stone);
                    for (Long value : cacheValue.values) {
                        updateCountMap(nextStoneCount, value, stoneCount.get(stone));
                    }
                } else {
                    if (stone == 0L) {
                        valueTransformationCache.put(0L, new ValueTransformationCacheItem(1L));
                        updateCountMap(nextStoneCount, 1L, stoneCount.get(0L));
                    } else {
                        List<Integer> numbers = new ArrayList<>();
                        long parser = stone;
                        while (true) {
                            numbers.addFirst((int) (parser % 10));
                            if (parser < 10) {
                                break;
                            }
                            parser = parser / 10;
                        }
                        if (numbers.size() % 2 == 0) {
                            long stoneA = 0L;
                            for (int firstNumberIndex = 0; firstNumberIndex < (numbers.size() / 2); firstNumberIndex++) {
                                stoneA += (long) Math.pow(10, (numbers.size() / 2.0) - firstNumberIndex - 1) * numbers.get(firstNumberIndex);
                            }
                            long stoneB = 0L;
                            for (int secondNumberIndex = numbers.size() / 2; secondNumberIndex < numbers.size(); secondNumberIndex++) {
                                stoneB += (long) Math.pow(10, (numbers.size()) - secondNumberIndex - 1) * numbers.get(secondNumberIndex);
                            }
                            valueTransformationCache.put(stone, new ValueTransformationCacheItem(new long[]{stoneA, stoneB}));
                            updateCountMap(nextStoneCount, stoneA, stoneCount.get(stone));
                            updateCountMap(nextStoneCount, stoneB, stoneCount.get(stone));
                        } else {
                            long nextStone = stone * 2024;
                            valueTransformationCache.put(stone, new ValueTransformationCacheItem(nextStone));
                            updateCountMap(nextStoneCount, nextStone, stoneCount.get(stone));
                        }

                    }
                }
            }
            stoneCount = nextStoneCount;
        }

        long count = 0;
        for (Long key : stoneCount.keySet()) {
            count += stoneCount.get(key);
        }
        System.out.println(count);
    }

    private static void updateCountMap(HashMap<Long, Long> countMap, long value, long newCount) {
        Long existingCount = countMap.get(value);
        if (existingCount == null) {
            existingCount = 0L;
        }
        countMap.put(value, existingCount + newCount);
    }

    private static class ValueTransformationCacheItem {
        long[] values;

        ValueTransformationCacheItem(long value) {
            this.values = new long[]{value};
        }

        ValueTransformationCacheItem(long[] values) {
            this.values = values;
        }
    }
}
