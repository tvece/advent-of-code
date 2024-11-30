package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class D10 {

	// TODO: fix part two
	public static void main(String[] args) {
		Path filePath = Paths.get("src/main/resources/2023/D10.txt");
		List<String> stringLines = new ArrayList<String>();
		try {
			stringLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}
		char[][] map = new char[stringLines.size()][stringLines.get(0).length()];
		int[] currentPosition = null;
		for (int i = 0; i < stringLines.size(); i++) {
			String stringLine = stringLines.get(i);
			for (int j = 0; j < stringLine.length(); j++) {

				char currentChar = stringLine.charAt(j);
				if (currentChar == 'S') {
					currentPosition = new int[] { i, j };
				}
				map[i][j] = currentChar;
			}
		}

		int[] previousPosition = { currentPosition[0], currentPosition[1] };
		List<int[]> path = new ArrayList<int[]>();
		path.add(currentPosition);
		path.add(currentPosition);
		while (true) {
			previousPosition = path.get(path.size() - 2);
			char currentChar = map[currentPosition[0]][currentPosition[1]];
			switch (currentChar) {
			case 'S':
				// north
				if ((previousPosition[0] == currentPosition[0] - 1
						&& previousPosition[1] == currentPosition[1]) == false
						&& isInRange(currentPosition[0] - 1, currentPosition[1], map)) {
					char testChar = map[currentPosition[0] - 1][currentPosition[1]];
					if (testChar == '|' || testChar == '7' || testChar == 'F') {
						currentPosition[0] -= 1;
						break;
					}
				}
				// east
				if ((previousPosition[0] == currentPosition[0]
						&& previousPosition[1] == currentPosition[1] + 1) == false
						&& isInRange(currentPosition[0], currentPosition[1] + 1, map)) {
					char testChar = map[currentPosition[0]][currentPosition[1] + 1];
					if (testChar == '-' || testChar == 'J' || testChar == '7') {
						currentPosition[1] += 1;
						break;
					}
				}
				// south
				if ((previousPosition[0] == currentPosition[0] + 1
						&& previousPosition[1] == currentPosition[1]) == false
						&& isInRange(currentPosition[0] + 1, currentPosition[1], map)) {
					char testChar = map[currentPosition[0]][currentPosition[1] + 1];
					if (testChar == '|' || testChar == 'L' || testChar == 'J') {
						currentPosition[0] += 1;
						break;
					}
				}
				// west
				if ((previousPosition[0] == currentPosition[0]
						&& previousPosition[1] == currentPosition[1] - 1) == false
						&& isInRange(currentPosition[0], currentPosition[1] - 1, map)) {
					char testChar = map[currentPosition[0]][currentPosition[1] - 1];
					if (testChar == '-' || testChar == 'L' || testChar == 'F') {
						currentPosition[1] -= 1;
						break;
					}
				}
			case ('|'):
				if ((previousPosition[0] == currentPosition[0] - 1
						&& previousPosition[1] == currentPosition[1]) == false) {
					// north
					currentPosition[0] -= 1;
				} else {
					// south
					currentPosition[0] += 1;
				}
				break;
			case ('-'):
				if ((previousPosition[0] == currentPosition[0]
						&& previousPosition[1] == currentPosition[1] + 1) == false) {
					// east
					currentPosition[1] += 1;
				} else {
					// west
					currentPosition[1] -= 1;
				}
				break;
			case ('L'):
				if ((previousPosition[0] == currentPosition[0] - 1
						&& previousPosition[1] == currentPosition[1]) == false) {
					// north
					currentPosition[0] -= 1;
				} else {
					// east
					currentPosition[1] += 1;
				}
				break;
			case ('J'):
				if ((previousPosition[0] == currentPosition[0] - 1
						&& previousPosition[1] == currentPosition[1]) == false) {
					// north
					currentPosition[0] -= 1;
				} else {
					// west
					currentPosition[1] -= 1;
				}
				break;
			case ('7'):
				if ((previousPosition[0] == currentPosition[0]
						&& previousPosition[1] == currentPosition[1] - 1) == false) {
					// west
					currentPosition[1] -= 1;
				} else {
					// south
					currentPosition[0] += 1;
				}
				break;
			case ('F'):
				if ((previousPosition[0] == currentPosition[0]
						&& previousPosition[1] == currentPosition[1] + 1) == false) {
					// east
					currentPosition[1] += 1;
				} else {
					// south
					currentPosition[0] += 1;
				}
				break;
			default:
				break;
			}

			if (map[currentPosition[0]][currentPosition[1]] == 'S') {
				break;
			}
			path.add(new int[] { currentPosition[0], currentPosition[1] });
		}
		System.out.println((path.size() - 1) / 2 + "\n");

		int totalEnclosed = 0;
		for (int row = 0; row < map.length; row++) {
			int brackets = 0;
			for (int column = 0; column < map[row].length; column++) {
				char character = map[row][column];
				if (isPath(row, column, path) && character == '|' || character == 'J' || character == 'L' || character == 'S') {
					brackets++;
				} else if (map[row][column] == '.') {
					if (brackets % 2 == 1) {
						System.out.println(row + " " + column);
						totalEnclosed++;
					}
					
				}
			}
		}
		System.out.println(totalEnclosed);
	}

	private static List<int[]> getIntervals(int row, List<int[]> path) {
		return path.stream().filter(a -> a[0] == row).collect(Collectors.toList());
	}

	private static boolean isPath(int row, int column, List<int[]> path) {
		return path.stream().anyMatch(element -> element[0] == row && element[1] == column);
	}

	private static boolean isInRange(int xPosition, int yPosition, char[][] map) {
		return xPosition >= 0 && xPosition < map.length && yPosition >= 0 && map[0].length > yPosition;
	}
}