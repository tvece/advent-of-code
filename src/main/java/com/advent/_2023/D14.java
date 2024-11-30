package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D14 {

	// part 2 is slow but since there is repeating pattern after some initial
	// rotations it is possible to guess the result from that
	public static void main(String[] args) {
		Path filePath = Paths.get("src/main/resources/2023/D14.txt");
		List<String> stringRows = new ArrayList<String>();
		try {
			stringRows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		char[][] rows = new char[stringRows.size()][];

		for (int i = 0; i < stringRows.size(); i++) {
			rows[i] = stringRows.get(i).toCharArray();
		}

		for (int i = 0; i < stringRows.size(); i++) {
			rows[i] = stringRows.get(i).toCharArray();
		}

		for (int i = 0; i < 1000000000; i++) {
			System.out.println(i);
			boolean nChange = north(rows);
			boolean wChange = west(rows);
			boolean sChange = south(rows);
			boolean eChange = east(rows);
			if (!nChange && !eChange && !sChange && !wChange) {
				break;
			}
			int weight = 0;
			for (int j = 0; j < rows.length; j++) {
				for (int k = 0; k < rows[j].length; k++) {
					if (rows[j][k] == 'O') {
						weight += rows.length - j;
					}
				}
			}
			System.out.println(weight);
		}

	}

	private static boolean north(char[][] rows) {
		boolean change = false;
		for (int currentRow = 0; currentRow < rows.length; currentRow++) {
			char[] row = rows[currentRow];
			for (int currentColumn = 0; currentColumn < row.length; currentColumn++) {
				if (row[currentColumn] == 'O') {
					Integer freeSpot = null;
					int rowFinder = currentRow - 1;
					while (rowFinder >= 0) {
						char finderChar = rows[rowFinder][currentColumn];
						if (finderChar == '#' || finderChar == 'O') {
							break;
						}
						freeSpot = rowFinder;
						rowFinder--;
					}
					if (freeSpot != null) {
						rows[currentRow][currentColumn] = '.';
						rows[freeSpot][currentColumn] = 'O';
						change = true;
					}
				}
			}
		}
		return change;
	}

	private static boolean east(char[][] rows) {
		boolean change = false;
		for (int currentColumn = rows[0].length - 2; currentColumn >= 0; currentColumn--) {
			for (int currentRow = 0; currentRow < rows.length; currentRow++) {
				if (rows[currentRow][currentColumn] == 'O') {
					Integer freeSpot = null;
					int columnFinder = currentColumn + 1;
					while (columnFinder < rows[0].length) {
						char finderChar = rows[currentRow][columnFinder];
						if (finderChar == '#' || finderChar == 'O') {
							break;
						}
						freeSpot = columnFinder;
						columnFinder++;
					}
					if (freeSpot != null) {
						rows[currentRow][currentColumn] = '.';
						rows[currentRow][freeSpot] = 'O';
						change = true;
					}
				}
			}
		}
		return change;
	}

	private static boolean south(char[][] rows) {
		boolean change = false;
		for (int currentRow = rows.length - 2; currentRow >= 0; currentRow--) {
			char[] row = rows[currentRow];
			for (int currentColumn = 0; currentColumn < row.length; currentColumn++) {
				if (row[currentColumn] == 'O') {
					Integer freeSpot = null;
					int rowFinder = currentRow + 1;
					while (rowFinder < row.length) {
						char finderChar = rows[rowFinder][currentColumn];
						if (finderChar == '#' || finderChar == 'O') {
							break;
						}
						freeSpot = rowFinder;
						rowFinder++;
					}
					if (freeSpot != null) {
						rows[currentRow][currentColumn] = '.';
						rows[freeSpot][currentColumn] = 'O';
						change = true;
					}
				}
			}
		}
		return change;
	}

	private static boolean west(char[][] rows) {
		boolean change = false;
		for (int currentColumn = 0; currentColumn < rows[0].length; currentColumn++) {
			for (int currentRow = 0; currentRow < rows.length; currentRow++) {
				if (rows[currentRow][currentColumn] == 'O') {
					Integer freeSpot = null;
					int columnFinder = currentColumn - 1;
					while (columnFinder >= 0) {
						char finderChar = rows[currentRow][columnFinder];
						if (finderChar == '#' || finderChar == 'O') {
							break;
						}
						freeSpot = columnFinder;
						columnFinder--;
					}
					if (freeSpot != null) {
						rows[currentRow][currentColumn] = '.';
						rows[currentRow][freeSpot] = 'O';
						change = true;
					}
				}
			}
		}
		return change;
	}

}