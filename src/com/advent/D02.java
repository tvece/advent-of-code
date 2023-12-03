package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D02 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D02.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		Integer result = 0;
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			Game game = new Game(line, i + 1);
			if (game.red <= 12 && game.green <= 13 && game.blue <= 14) {
				System.out.println(line);
				result += game.id;
			}
		}

		System.out.println(result);
	}

	private static class Game {
		public int red = 0;
		public int blue = 0;
		public int green = 0;

		public int id;

		public Game(String line, int id) {
			this.id = id;
			// remove "Game XY: "
			line = line.substring(line.indexOf(": ") + 2);
			List<String> grabs = Arrays.asList(line.split("; "));
			for (int i = 0; i < grabs.size(); i++) {
				List<String> groups = Arrays.asList(grabs.get(i).split(", "));
				for (int j = 0; j < groups.size(); j++) {
					String groupString = groups.get(j);
					String[] group = groupString.split(" ");
					Integer count = Integer.parseInt(group[0]);
					switch (group[1]) {
					case "red":
						if (red < count) {
							red = count;
						}
						break;
					case "blue":
						if (blue < count) {
							blue = count;
						}
						break;
					case "green":
						if (green < count) {
							green = count;
						}
						break;
					default:
						throw new RuntimeException("Unexpected dice color: " + group[1]);
					}

				}
			}
		}

	}
}
