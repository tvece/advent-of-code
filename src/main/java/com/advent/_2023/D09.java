package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D09 {

	public static void main(String[] args) {
		Path filePath = Paths.get("src/main/resources/2023/D09.txt");
		List<String> stringLines = new ArrayList<String>();
		try {
			stringLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}
		List<List<Integer>> lines = stringLines.stream().map(
				line -> Stream.of(line.split(" ")).map(string -> Integer.valueOf(string)).collect(Collectors.toList()))
				.collect(Collectors.toList());
		List<Integer> results = new ArrayList<>();
		lines.forEach(line -> {
			List<List<Integer>> diffLines = new ArrayList<>(Arrays.asList(line));
			List<Integer> previousLine = new ArrayList<>(line);
			while (true) {
				List<Integer> diffLine = new ArrayList<>();
				for (int i = 0; i < previousLine.size() - 1; i++) {
					diffLine.add(previousLine.get(i + 1) - previousLine.get(i));
				}
				diffLines.add(diffLine);
				if (diffLine.stream().allMatch(diff -> diff.equals(0))) {
					Integer value = 0;
					for (int i = diffLines.size() - 1; i >= 0; i--) {
						value = diffLines.get(i).get(0) - value;
					}
					results.add(value);
					break;
				}
				previousLine = diffLine;
			}

		});
		System.out.printf("%.12f", results.stream().mapToDouble(a -> a).sum());
	}
}