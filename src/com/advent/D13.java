package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D13 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D13.txt");
		List<String> stringRows = new ArrayList<String>();
		try {
			stringRows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		List<List<String>> maps = new ArrayList<>();
		maps.add(new ArrayList<>());
		int i = 0;
		for (String row : stringRows) {
			if (row.equals("")) {
				maps.add(new ArrayList<>());
				i++;
				continue;
			}
			maps.get(i).add(row);
		}
		maps.getClass();

		double result = 0;

		for (List<String> map : maps) {
			char[][] rows = new char[map.size()][];
			for (int rowIndex = 0; rowIndex < map.size(); rowIndex++) {
				rows[rowIndex] = map.get(rowIndex).toCharArray();
			}

			char[][] columns = new char[map.get(0).length()][];
			for (int columnIndex = 0; columnIndex < map.get(0).length(); columnIndex++) {
				columns[columnIndex] = new char[map.size()];
				for (int j = 0; j < map.size(); j++) {
					columns[columnIndex][j] = map.get(j).charAt(columnIndex);
				}
			}

			Integer foundReflection = null;
			for (int rowIndex = 0; rowIndex < rows.length - 1; rowIndex++) {
				boolean isReflection = true;
				int leftCheck = rowIndex;
				for (int rightCheck = rowIndex + 1; rightCheck < rows.length
						&& rightCheck < ((rowIndex + 1) * 2); rightCheck++) {
					if (!Arrays.equals(rows[leftCheck], rows[rightCheck])) {
						isReflection = false;
						break;
					}
					leftCheck--;
				}
				if (isReflection) {
					foundReflection = rowIndex;
					result += 100 * (foundReflection + 1);
					break;
				}
			}

			if (foundReflection == null) {
				for (int columnIndex = 0; columnIndex < columns.length - 1; columnIndex++) {
					boolean isReflection = true;
					int leftCheck = columnIndex;
					for (int rightCheck = columnIndex + 1; rightCheck < columns.length
							&& rightCheck < ((columnIndex + 1) * 2); rightCheck++) {
						if (!Arrays.equals(columns[leftCheck], columns[rightCheck])) {
							isReflection = false;
							break;
						}
						leftCheck--;
					}
					if (isReflection) {
						foundReflection = columnIndex;
						result += foundReflection + 1;
						break;
					}
				}
			}

		}

		System.out.printf("%.0f\n", result);

	}
}