package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class D04 {

	static List<Integer> resolvedMatches;

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D04.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		List<Integer> matches = new ArrayList<>(Arrays.asList(0));
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			line = line.substring(line.indexOf(":") + 1);
			List<Integer> winningNumbers = new ArrayList<>();
			List<Integer> actualNumbers = new ArrayList<>();
			boolean isBeforePipe = true;
			int index = 0;
			while (true) {
				if (index >= line.length() - 2) {
					break;
				}
				Integer currentNumber = 0;
				char currentChar = line.charAt(index + 1);
				char nextChar = line.charAt(index + 2);
				if (currentChar == '|') {
					isBeforePipe = false;
					index += 2;
					continue;
				}
				if (currentChar != ' ') {
					currentNumber = Character.getNumericValue(currentChar) * 10;
				}

				currentNumber += Character.getNumericValue(nextChar);
				if (isBeforePipe) {
					winningNumbers.add(currentNumber);
				} else {
					actualNumbers.add(currentNumber);
				}
				index += 3;
			}
			int count = actualNumbers.stream().filter(actualNumber -> winningNumbers.contains(actualNumber))
					.collect(Collectors.toList()).size();
			matches.add(count);
		}

		resolvedMatches = new ArrayList<>(Collections.nCopies(matches.size(), 0));
		for (int i = matches.size() - 1; i > 0; i--) {
			resolveMatches(i, matches);
		}

		System.out.println((int)resolvedMatches.stream().mapToDouble(a -> a).sum());
	}

	private static int resolveMatches(int currentIndex, List<Integer> matches) {
		Integer resolvedMatch = resolvedMatches.get(currentIndex);
		if(resolvedMatch!=0) {
			return resolvedMatch;
		}
		int result = 1;
		for (int i = 1; i <= matches.get(currentIndex); i++) {
			result += resolvedMatches.get(currentIndex + i);
		}
		resolvedMatches.set(currentIndex, result);
		return result;
	}

}
