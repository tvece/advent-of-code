package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// vm argument -Xss100m to allow deep recursion
public class D16 {

	public static void main(String[] args) throws InterruptedException {
		Path filePath = Paths.get("src/main/resources/2023/D16.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		char[][] map = new char[rows.size()][];
		for (int i = 0; i < rows.size(); i++) {
			map[i] = rows.get(i).toCharArray();
		}

		int max = 0;

		for (int i = 0; i < map[0].length; i++) {
			Set<String> energized = new HashSet<>();
			traverse(0, i, 'S', map, energized);
			max = (int) Math.max(max,
					energized.stream().map(element -> element.substring(0, element.indexOf(';'))).distinct().count());
		}

		for (int i = 0; i < map.length; i++) {
			Set<String> energized = new HashSet<>();
			traverse(i, 0, 'E', map, energized);
			max = (int) Math.max(max,
					energized.stream().map(element -> element.substring(0, element.indexOf(';'))).distinct().count());
		}

		for (int i = 0; i < map[0].length; i++) {
			Set<String> energized = new HashSet<>();
			traverse(i, map[0].length - 1, 'W', map, energized);
			max = (int) Math.max(max,
					energized.stream().map(element -> element.substring(0, element.indexOf(';'))).distinct().count());
		}

		for (int i = 0; i < map.length; i++) {
			Set<String> energized = new HashSet<>();
			traverse(map.length - 1, i, 'N', map, energized);
			max = (int) Math.max(max,
					energized.stream().map(element -> element.substring(0, element.indexOf(';'))).distinct().count());
		}

		System.out.println(max);
	}

	private static void traverse(int x, int y, char direction, char[][] map, Set<String> result)
			throws InterruptedException {
		if (x < 0 || y < 0 || x > map.length - 1 || y > map[0].length - 1) {
			return;
		}
		String key = x + "," + y + ";" + direction;
		if (result.contains(key)) {
			return;
		}
		System.out.println(key + "\t" + result.size());
		result.add(key);

		char[] nextDirections = new char[2];
		char currentChar = map[x][y];

		if (currentChar == '.' || (currentChar == '-' && (direction == 'W' || direction == 'E'))
				|| (currentChar == '|' && (direction == 'N' || direction == 'S'))) {
			switch (direction) {
			case 'N':
				nextDirections[0] = 'N';
				break;
			case 'E':
				nextDirections[0] = 'E';
				break;
			case 'S':
				nextDirections[0] = 'S';
				break;
			case 'W':
				nextDirections[0] = 'W';
				break;
			}
		} else if (currentChar == '/') {
			switch (direction) {
			case 'N':
				nextDirections[0] = 'E';
				break;
			case 'E':
				nextDirections[0] = 'N';
				break;
			case 'S':
				nextDirections[0] = 'W';
				break;
			case 'W':
				nextDirections[0] = 'S';
				break;
			}
		} else if (currentChar == '\\') {
			switch (direction) {
			case 'N':
				nextDirections[0] = 'W';
				break;
			case 'E':
				nextDirections[0] = 'S';
				break;
			case 'S':
				nextDirections[0] = 'E';
				break;
			case 'W':
				nextDirections[0] = 'N';
				break;
			}
		} else if (currentChar == '|') {
			switch (direction) {
			case 'W':
				nextDirections[0] = 'N';
				nextDirections[1] = 'S';
				break;
			case 'E':
				nextDirections[0] = 'N';
				nextDirections[1] = 'S';
				break;
			}
		} else if (currentChar == '-') {
			switch (direction) {
			case 'N':
				nextDirections[0] = 'W';
				nextDirections[1] = 'E';
				break;
			case 'S':
				nextDirections[0] = 'W';
				nextDirections[1] = 'E';
				break;
			}
		}

		for (char nextDirection : nextDirections) {
			switch (nextDirection) {
			case 'N':
				traverse(x - 1, y, 'N', map, result);
				break;
			case 'E':
				traverse(x, y + 1, 'E', map, result);
				break;
			case 'S':
				traverse(x + 1, y, 'S', map, result);
				break;
			case 'W':
				traverse(x, y - 1, 'W', map, result);
				break;
			}
		}
	}
}