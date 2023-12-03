package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class D03 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D03.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		Map<String, List<List<Integer>>> resultNumbers = new HashMap<>();
		for (int lineIterator = 0; lineIterator < lines.size(); lineIterator++) {
			String line = lines.get(lineIterator);
			int charIterator = 0;
			List<Integer> currentNumber = new ArrayList<>();
			String currentStarCords = "";
			boolean wasNextToSymbol = false;
			while (charIterator < line.length()) {
				char charString = line.charAt(charIterator);
				if (Character.isDigit(charString)) {
					if (!wasNextToSymbol) {
						String isNextToSymbol = isNextToSymbol(charIterator, lineIterator, lines);
						if (isNextToSymbol.equals(NEXT_TO_OTHER)) {
							wasNextToSymbol = true;
						} else if (!isNextToSymbol.equals(NEXT_TO_NONE) && !currentStarCords.contains(isNextToSymbol)) {
							currentStarCords += isNextToSymbol;
						}
					}
					currentNumber.add(Character.getNumericValue(charString));
				} else {
					if (!currentNumber.isEmpty() && !currentStarCords.isEmpty()) {
						String[] cords = currentStarCords.split(";");
						for (int i = 0; i < cords.length; i++) {
							String cord = cords[i];
							List<Integer> copy = new ArrayList<Integer>(currentNumber);
							if (resultNumbers.containsKey(cord)) {
								resultNumbers.get(cord).add(copy);
							} else if (!resultNumbers.containsKey(cord)) {
								resultNumbers.put(cord, new ArrayList<List<Integer>>());
								resultNumbers.get(cord).add(copy);
							}
						}
					}
					wasNextToSymbol = false;
					if (!currentNumber.isEmpty()) {
						currentNumber.clear();
					}
					if (!currentStarCords.isEmpty()) {
						currentStarCords = "";
					}
				}
				charIterator++;
			}
			if (!currentNumber.isEmpty() && !currentStarCords.isEmpty()) {
				String[] cords = currentStarCords.split(";");
				for (int i = 0; i < cords.length; i++) {
					String cord = cords[i];
					List<Integer> copy = new ArrayList<Integer>(currentNumber);
					if (resultNumbers.containsKey(cord)) {
						resultNumbers.get(cord).add(copy);
					} else if (!resultNumbers.containsKey(cord)) {
						resultNumbers.put(cord, new ArrayList<List<Integer>>());
						resultNumbers.get(cord).add(copy);
					}
				}
			}
			wasNextToSymbol = false;
			if (!currentNumber.isEmpty()) {
				currentNumber.clear();
			}
			if (!currentStarCords.isEmpty()) {
				currentStarCords = "";
			}
		}

		List<Integer> resultArray = new ArrayList<>();
		resultNumbers.keySet().forEach(key -> {
			if (resultNumbers.get(key).size() == 2) {
				resultArray.add(getNumberFromStringParts(resultNumbers.get(key).get(0))
						* getNumberFromStringParts(resultNumbers.get(key).get(1)));
			}
		});
		Integer result = 0;
		for (int i = 0; i < resultArray.size(); i++) {
			result += resultArray.get(i);
		}
		System.out.println(result);
	}

	private static Integer getNumberFromStringParts(List<Integer> parts) {
		Integer result = 0;
		for (int j = 0; j < parts.size(); j++) {
			result += (((int) Math.pow(10, parts.size() - j - 1) * parts.get(j)));
		}
		return result;
	}

	private static final String NEXT_TO_OTHER = "other";
	private static final String NEXT_TO_NONE = "none";

	private static String isNextToSymbol(int x, int y, List<String> lines) {
		String cords = "";
		boolean isNextToSymbol = false;
		for (int xrange = x - 1; xrange <= x + 1; xrange++) {
			if (xrange >= 0 && lines.get(0).length() > xrange) {
				for (int yrange = y - 1; yrange <= y + 1; yrange++) {
					if (yrange >= 0 && lines.size() > yrange) {
						if (!(xrange == x && yrange == y)) {
							char charCheck = lines.get(yrange).charAt(xrange);
							if (!Character.isDigit(charCheck) && charCheck != '.') {
								if (charCheck == '*') {
									cords += xrange + "," + yrange + ";";
								}
								isNextToSymbol = true;
							}
						}
					}
				}
			}
		}
		if (cords.isEmpty()) {
			if (isNextToSymbol) {
				return NEXT_TO_OTHER;
			} else {
				return NEXT_TO_NONE;
			}
		}

		return cords;
	}
}
