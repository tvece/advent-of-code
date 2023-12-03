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
			for (int j = 0; j < lines.get(i).length(); j++) {
				char character = lines.get(i).charAt(j);
				if (Character.isDigit(character)) {
					Integer value = Character.getNumericValue(character);
					if (firstValue == null) {
						firstValue = value;
					} else {
						lastValue = value;
					}
				}
			}
			System.out.println(firstValue + " " + lastValue);
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