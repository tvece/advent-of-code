package com.advent._2023;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D15 {

	public static void main(String[] args) {
		Path filePath = Paths.get("src/main/resources/2023/D15.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		@SuppressWarnings("unchecked")
		List<String>[] boxes = new List[256];
		for (int i = 0; i < 256; i++) {
			boxes[i] = new ArrayList<String>();
		}

		String[] steps = rows.get(0).split(",");
		for (String step : steps) {
			if (step.contains("-")) {
				String lenseId = step.substring(0, step.length() - 1);
				List<String> box = boxes[getHash(lenseId)];
				String existingLense = box.stream().filter(lense -> lense.startsWith(lenseId)).findFirst().orElse(null);
				if (existingLense != null) {
					box.remove(existingLense);
				}
			} else {
				String lenseId = step.substring(0, step.indexOf("="));
				List<String> box = boxes[getHash(lenseId)];
				String existingLense = box.stream().filter(lense -> lense.startsWith(lenseId)).findFirst().orElse(null);
				if (existingLense == null) {
					box.add(step);
				} else {
					box.set(box.indexOf(existingLense), step);
				}
			}
		}

		int result = 0;
		for (int i = 0; i < 256; i++) {
			List<String> box = boxes[i];
			for (int j = 0; j < box.size(); j++) {
				String lense = box.get(j);
				int focalLength = Integer.valueOf(lense.substring(lense.indexOf("=") + 1));
				result += (i + 1) * (j + 1) * focalLength;
			}
		}
		System.out.println(result);
	}

	private static int getHash(String step) {
		int stepResult = 0;
		for (char character : step.toCharArray()) {
			stepResult += (int) character;
			stepResult = stepResult * 17;
			stepResult = stepResult % 256;
		}
		return stepResult;
	}
}