package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D01 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D01.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		Integer result = 0;
		Integer firstValue = null;
		Integer lastValue = null;
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			for (int j = 0; j < line.length(); j++) {
				char character = line.charAt(j);
				Integer value = null;
				String subString = line.substring(j);
				if (Character.isDigit(character)) {
					value = Character.getNumericValue(character);
				} else if (subString.startsWith("one")) {
					value = 1;
				} else if (subString.startsWith("two")) {
					value = 2;
				} else if (subString.startsWith("three")) {
					value = 3;
				} else if (subString.startsWith("four")) {
					value = 4;
				} else if (subString.startsWith("five")) {
					value = 5;
				} else if (subString.startsWith("six")) {
					value = 6;
				} else if (subString.startsWith("seven")) {
					value = 7;
				} else if (subString.startsWith("eight")) {
					value = 8;
				} else if (subString.startsWith("nine")) {
					value = 9;
				}

				if (value != null) {
					if (firstValue == null) {
						firstValue = value;
					} else {
						lastValue = value;
					}
				}

			}
			System.out.println(lines.get(i) + " " + firstValue + " " + lastValue);
			if (firstValue != null) {
				result += (firstValue * 10);
				if (lastValue != null) {
					result += lastValue;
				} else {
					result += firstValue;
				}
			}

			firstValue = null;
			lastValue = null;
		}
		System.out.println(result);
	}
}