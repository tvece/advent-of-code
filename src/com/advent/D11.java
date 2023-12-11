package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class D11 {

	// TODO improve performance (part 2 is not fast enough to print results in meaningful time - but probably works :) )
	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D11.txt");
		List<String> stringLines = new ArrayList<String>();
		try {
			stringLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		Set<Integer> okRows = new HashSet<>();
		Set<Integer> okColumns = new HashSet<>();

		for (int row = 0; row < stringLines.size(); row++) {
			for (int column = 0; column < stringLines.get(0).length(); column++) {
				if (stringLines.get(row).charAt(column) == '#') {
					okRows.add(row);
					okColumns.add(column);
				}
			}
		}

		List<String> rows = new ArrayList<>();
		for (int row = 0; row < stringLines.size(); row++) {
			String newRow = "";
			for (int column = 0; column < stringLines.get(0).length(); column++) {
				newRow += stringLines.get(row).charAt(column);
				if (!okColumns.contains(column)) {
					char[] array = new char[1000000];
					Arrays.fill(new char[1000000], '.');
					newRow += new String(array);
				}
			}
			rows.add(newRow);
			if (!okRows.contains(row)) {
				for(int i = 0; i<1000000;i++) {					
					String expandedRow = "";
					for (int j = 0; j < newRow.length(); j++) {
						expandedRow += ".";
					}
					rows.add(expandedRow);
				}
			}
		}

		//rows.forEach(row -> System.out.println(row));
		List<int[]> galaxies = new ArrayList<>();
		for (int row = 0; row < rows.size(); row++) {
			for (int column = 0; column < rows.get(0).length(); column++) {
				if (rows.get(row).charAt(column) == '#') {
					galaxies.add(new int[] { row, column });
				}
			}
		}

		List<int[][]> pairs = new ArrayList<int[][]>();
		for (int[] galaxy : galaxies) {
			for (int[] otherGalaxy : galaxies) {
				if (galaxy != otherGalaxy && !pairs.stream()
						.anyMatch(existingPair -> existingPair[0][0] == otherGalaxy[0]
								&& existingPair[0][1] == otherGalaxy[1] && existingPair[1][0] == galaxy[0]
								&& existingPair[1][1] == galaxy[1])) {
					pairs.add(new int[][] { galaxy, otherGalaxy });
				}
			}
		}

		double totalDistance = 0;
		for (int[][] pair : pairs) {
			//System.out.println(pair[0][0] + " " + pair[0][1] + "   " + pair[1][0] + " " + pair[1][1]);
			int distance = Math.abs(pair[0][0] - pair[1][0]) + Math.abs(pair[0][1] - pair[1][1]);
			//System.out.println(distance);
			//System.out.println();
			totalDistance += distance;
		}
		System.out.printf("%.12f\n", totalDistance);
	}

}