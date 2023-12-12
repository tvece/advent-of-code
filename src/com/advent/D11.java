package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class D11 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D11.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		Set<Integer> okRows = new HashSet<>();
		Set<Integer> okColumns = new HashSet<>();

		for (int row = 0; row < rows.size(); row++) {
			for (int column = 0; column < rows.get(0).length(); column++) {
				if (rows.get(row).charAt(column) == '#') {
					okRows.add(row);
					okColumns.add(column);
				}
			}
		}

		// rows.forEach(row -> System.out.println(row));
		List<int[]> galaxies = new ArrayList<>();
		for (int row = 0; row < rows.size(); row++) {
			for (int column = 0; column < rows.get(0).length(); column++) {
				if (rows.get(row).charAt(column) == '#') {
					galaxies.add(new int[] { row, column });
				}
			}
		}

		List<int[][]> pairs = new ArrayList<int[][]>();
		HashSet<String> existenceCheck = new HashSet<String>();
		for (int[] galaxy : galaxies) {
			for (int[] otherGalaxy : galaxies) {
				if (galaxy[0] != otherGalaxy[0] || galaxy[1] != otherGalaxy[1]) {
					String reverseHash = otherGalaxy[0] + "," + otherGalaxy[1] + ";" + galaxy[0] + "," + galaxy[1];
					if (!existenceCheck.contains(reverseHash)) {
						pairs.add(new int[][] { galaxy, otherGalaxy });
						existenceCheck.add(galaxy[0] + "," + galaxy[1] + ";" + otherGalaxy[0] + "," + otherGalaxy[1]);
					}
				}

			}
		}

		double totalDistance = 0;
		for (int[][] pair : pairs) {
			int startX = pair[0][0] < pair[1][0] ? pair[0][0] : pair[1][0];
			int startY = pair[0][1] < pair[1][1] ? pair[0][1] : pair[1][1];

			int endX = startX == pair[0][0] ? pair[1][0] : pair[0][0];
			int endY = startY == pair[0][1] ? pair[1][1] : pair[0][1];

			int corrupted = 0;

			for (int i = startX + 1; i < endX; i++) {
				if (!okRows.contains(i)) {
					corrupted++;
				}
			}

			for (int i = startY + 1; i < endY; i++) {
				if (!okColumns.contains(i)) {
					corrupted++;
				}
			}

			int galaxyDistance = Math.abs(pair[0][0] - pair[1][0]) + Math.abs(pair[0][1] - pair[1][1]);
			corrupted = corrupted * 999999;
			galaxyDistance += corrupted;

			System.out.println(pair[0][0] + "-" + pair[0][1] + ";" + pair[1][0] + "-" + pair[1][1] + "   "
					+ galaxyDistance + "   " + corrupted);
			totalDistance += galaxyDistance;
		}
		System.out.printf("%.0f\n", totalDistance);
	}

}