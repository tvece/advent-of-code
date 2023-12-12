package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

//TODO: part 2 probably works but is way too slow to check
public class D12 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D12.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		int total = 0;
		for (String row : rows) {
			String[] split = row.split(" ");
			int[] guide = Stream.of(split[1].split(",")).map(s -> Integer.valueOf(s)).mapToInt(s -> s).toArray();
			char[] input = split[0].toCharArray();
			int damaged = 0;
			for (char character : input) {
				if (character == '#') {
					damaged++;
				}
			}

			int guideCount = 0;
			for (int item : guide) {
				guideCount += item;
			}

			System.out.println(split[0] + " " + Arrays.toString(guide));
			String inputPart = split[0];
			String expandedInput = inputPart;
			for(int i = 0; i<4;i++) {
				expandedInput += "?" + inputPart;
			}
			int[]expandedGuide= new int[guide.length*5];
			for(int i = 0;i<guide.length*5;i++) {
				expandedGuide[i] = guide[i%guide.length];
			}
			//int currentArrangements = getArrangments(expandedInput.toCharArray(), 0, damaged*5, guideCount*5, expandedGuide);
			int currentArrangements = getArrangments(inputPart.toCharArray(), 0, damaged, guideCount, guide);
			total += currentArrangements;
			System.out.println(currentArrangements);
			System.out.println();
		}
		System.out.println(total);
	}

	public static int getArrangments(char[] input, int index, int currentNumberOfDamaged, int maxNumberOfDamaged,
			int[] guide) {
		if (currentNumberOfDamaged > maxNumberOfDamaged) {
			return 0;
		}

		if (input.length == index) {
			int[] parsedInput = Stream.of(String.valueOf(input).replaceAll("\\.+", ".").split("\\.")).filter(sequence->!sequence.isEmpty()).map(a->a.length()).mapToInt(s -> s).toArray();
			int isCorrectInput = Arrays.equals(parsedInput, guide)?1:0;
			if(isCorrectInput>0) {
				"".getClass();
			}
			return isCorrectInput;
		}

		int nextIndex = index + 1;
		if (input[index] == '?') {
			int result = 0;
			input[index] = '.';
			result += getArrangments(Arrays.copyOf(input, input.length), nextIndex, currentNumberOfDamaged, maxNumberOfDamaged, guide);
			input[index] = '#';
			result += getArrangments(Arrays.copyOf(input, input.length), nextIndex, currentNumberOfDamaged + 1, maxNumberOfDamaged, guide);
			return result;
		} else {
			return getArrangments(Arrays.copyOf(input, input.length), nextIndex, currentNumberOfDamaged, maxNumberOfDamaged, guide);
		}
	}
}