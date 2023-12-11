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

	//TODO part 1 and 2
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

		List<String> expandedRows = new ArrayList<>();
		for (int row = 0; row < stringLines.size(); row++) {
			String newRow = "";
			for (int column = 0; column < stringLines.get(0).length(); column++) {
				newRow += stringLines.get(row).charAt(column);
				if (!okColumns.contains(column)) {
					newRow += ".";
				}
			}
			expandedRows.add(newRow);
			if (!okRows.contains(row)) {
				String expandedRow = "";
				for (int i = 0; i < (stringLines.get(0).length() + (stringLines.get(0).length() - okRows.size())); i++) {
					expandedRow+=".";
				}
				expandedRows.add(expandedRow);
			}
		}
		
		expandedRows.forEach(row->System.out.println(row));

	}

}