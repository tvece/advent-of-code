package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D06 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D06.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}
		
		List<Double> times = Arrays.asList(lines.get(0).replaceAll(" +", "").substring(5).split(" ")).stream()
				.map(string -> Double.parseDouble(string)).collect(Collectors.toList());
		List<Double> distances = Arrays.asList(lines.get(1).replaceAll(" +", "").substring(9).split(" ")).stream()
				.map(string -> Double.parseDouble(string)).collect(Collectors.toList());
		Integer results = 1;
		for (int i = 0; i < times.size(); i++) {
			int validTries = 0;
			Double totalTime = times.get(i);
			for (int windupTime = 1; windupTime < totalTime; windupTime++) {
				if (((totalTime - windupTime) * windupTime) > distances.get(i)) {
					validTries++;
				}
			}
			results = results * validTries;
		}
		System.out.println(results);
	}
}