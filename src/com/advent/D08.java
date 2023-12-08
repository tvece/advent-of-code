package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//TODO: probably ok but slow
public class D08 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D08.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		int instructionsLength = lines.get(0).length(); 
		int[] instructions = lines.get(0).chars().map(character->character == 'L'?0:1).toArray();

		int[] map = new int[9999992];
		List<Integer> currentStepsList = new ArrayList<>();
		for (int i = 2; i < lines.size(); i++) {
			String line = lines.get(i);
			String brackets = line.substring(line.indexOf('(') + 1, line.lastIndexOf(')'));
			String[] stringDestinations = brackets.split(", ");
			String indexPath = line.substring(0, line.indexOf(' '));
			int mapIndex = getStringToIntPath(indexPath);
			if(indexPath.charAt(2)=='A') {
				currentStepsList.add(mapIndex);
			}
			
			map[mapIndex] = getStringToIntPath(stringDestinations[0]);
			map[mapIndex+1] = getStringToIntPath(stringDestinations[1]);
			
		}
		for(int i =0; i<9999992; i++) {			
			if(map[i] != 0) {				
				System.out.println(i + " " + map[i]);
			}
		}

		Integer[] currentSteps = currentStepsList.toArray(Integer[]::new);

		int index = -1;		
		double stepCounter = 0;
		while (true) {
			boolean isFinished = true;
			for (int step : currentSteps) {
				if ((step%1000)/10 != 90) {
					isFinished = false;
					break;
				}
			}
			if (isFinished) {
				System.out.printf("%.12f\n", stepCounter);
				return;
			}

			stepCounter++;
			index++;
			if (index == instructionsLength) {
				index = 0;
			}

			if (instructions[index] == 0) {
				//L
				for(int i = 0; i<currentSteps.length; i++) {
					currentSteps[i] = map[currentSteps[i]];
				}
				
			} else {
				// R
				for(int i = 0; i<currentSteps.length; i++) {
					currentSteps[i] = map[currentSteps[i]+1];
				}
			}
		}
	}

	private static int getStringToIntPath(String stringPath) {
		return (charToInt(stringPath.charAt(0)) * 100000) + (charToInt(stringPath.charAt(1)) * 1000)
				+ (charToInt(stringPath.charAt(2))*10);
	}

	private static int charToInt(char character) {
		return ((int) character);
	}
}