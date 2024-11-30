package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

//TODO: improve part 1 so it does not guessing size of map 
//TODO: part 2
public class D18 {

	public static void main(String[] args) throws InterruptedException {
		Path filePath = Paths.get("src/main/resources/2023/D18.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		List<Instruction> instructions = new ArrayList<>();
		int maxRight = 0;
		int maxLeft = 0;
		int maxDown = 0;
		Cord currentIndex = new Cord(0, 0);
		for (String row : rows) {
			Instruction instruction = new Instruction(row);
			instructions.add(instruction);
			switch (instruction.direction) {
			case "L":
				currentIndex.y -= instruction.count;
				if (currentIndex.y < maxLeft) {
					maxLeft = currentIndex.y;
				}
				break;
			case "R":
				currentIndex.y += instruction.count;
				if (currentIndex.y > maxRight) {
					maxRight = currentIndex.y;
				}
				break;
			case "D":
				currentIndex.x += instruction.count;
				if (currentIndex.x > maxDown) {
					maxDown = currentIndex.x;
				}
				break;
			case "U":
				currentIndex.x -= instruction.count;
				break;
			}
		}

		char[][] map = new char[600][];
		for (int i = 0; i < 600; i++) {
			map[i] = new char[600];
			for (int j = 0; j < 600; j++) {
				map[i][j] = '.';
			}
		}
		map[300][300] = '#';

		currentIndex = new Cord(300, 300);
		for (Instruction instruction : instructions) {
			switch (instruction.direction) {
			case "U":
				for (int i = currentIndex.x - 1; i > currentIndex.x - 1 - instruction.count; i--) {
					map[i][currentIndex.y] = '#';
				}
				currentIndex.x = currentIndex.x - instruction.count;
				break;
			case "D":
				for (int i = currentIndex.x + 1; i < currentIndex.x + 1 + instruction.count; i++) {
					map[i][currentIndex.y] = '#';
				}
				currentIndex.x = currentIndex.x + instruction.count;
				break;
			case "R":
				for (int i = currentIndex.y + 1; i < currentIndex.y + 1 + instruction.count; i++) {
					map[currentIndex.x][i] = '#';
				}
				currentIndex.y = currentIndex.y + instruction.count;
				break;
			case "L":
				for (int i = currentIndex.y - 1; i > currentIndex.y - 1 - instruction.count; i--) {
					map[currentIndex.x][i] = '#';
				}
				currentIndex.y = currentIndex.y - instruction.count;
				break;
			}
		}

		Queue<Cord> toBeVisisited = new LinkedList<>();
		toBeVisisited.add(new Cord(0, 0));
		Set<String> visited = new HashSet<>();

		while (!toBeVisisited.isEmpty()) {
			Cord currentCord = toBeVisisited.poll();
			if (currentCord.x >= 0 && currentCord.y >= 0 && currentCord.x < map.length && currentCord.y < map[0].length
					&& map[currentCord.x][currentCord.y] == '.' && !visited.contains(currentCord.toString())) {
				map[currentCord.x][currentCord.y] = '-';
				visited.add(currentCord.toString());
				toBeVisisited.add(new Cord(currentCord.x + 1, currentCord.y));
				toBeVisisited.add(new Cord(currentCord.x - 1, currentCord.y));
				toBeVisisited.add(new Cord(currentCord.x, currentCord.y + 1));
				toBeVisisited.add(new Cord(currentCord.x, currentCord.y - 1));
			}
		}

		double area = 0;
		for (char[] row : map) {
			System.out.println(row);
			for (char character : row) {
				if (character == '.' || character == '#') {
					area++;
				}
			}
		}
		System.out.println(area);

	}

	private static class Cord {
		int x;
		int y;

		Cord(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return this.x + ";" + this.y;
		}
	}

	private static class Instruction {
		String direction;
		int count;
		String color;

		Instruction(String row) {
			String[] split = row.split(" ");
			this.direction = split[0];
			this.count = Integer.valueOf(split[1]);
			this.color = split[2];
		}

		public String toString() {
			return this.direction + " " + this.count + " " + this.color;
		}
	}
}