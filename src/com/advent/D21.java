package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

//TODO?
public class D21 {

	static int TARGET_STEPS = 64;
	
	public static void main(String[] args) throws InterruptedException {
		Path filePath = Paths.get("resources/D21.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		char[][] map = new char[rows.size()][];
		int[] startChar = new int[2];
		for (int i = 0; i < rows.size(); i++) {
			String row = rows.get(i);
			map[i] = row.toCharArray();
			int indexOfS = row.indexOf("S");
			if (indexOfS != -1) {
				startChar = new int[] { i, indexOfS };
			}
		}
		
		Set<String> paths = new HashSet<>();
		Queue<Step> runningSteps = new LinkedList<>();
		runningSteps.add(new Step(startChar, 0, Step.indexToPathElement(startChar)));
		while(!runningSteps.isEmpty()) {
			Step currentStep = runningSteps.poll();
			if(currentStep.depth == TARGET_STEPS) {
				paths.add(currentStep.path);
			} else {
				runningSteps.addAll(currentStep.getNextSteps(map));
			}
		}
		Set<String> result = new HashSet<>();
		paths.forEach(path-> {
			System.out.println(path);
			String[] stringIndexes = path.split(";");
			result.add(Step.indexToPathElement(Stream.of(stringIndexes[stringIndexes.length-1].split(",")).mapToInt(value->Integer.valueOf(value)).toArray()));
		});
		
		List<String> test = new ArrayList<>();
		test.addAll(result);
		test.sort((a,b)->a.compareTo(b));
		System.out.println(test);
		for(int i = 0; i<map.length; i++) {
			for(int j = 0; j<map[0].length; j++) {
				if(result.contains(Step.indexToPathElement(new int[]{i, j}))) {
					System.out.print("O");
				} else {
					System.out.print(map[i][j]);
				}
			}
			System.out.println();
		}
		
		
		System.out.println(result.size());
	}
	
	private static class Step {
		int[] currentIndex;
		int depth;
		String path;
		public Step (int[] currentIndex, int depth, String path) {
			this.currentIndex = currentIndex;
			this.depth = depth;
			this.path = path;
		}
		
		public List<Step> getNextSteps(char[][]map) {
			List<Step> result = new ArrayList<>();
			int[] west = {currentIndex[0], currentIndex[1]-1};
			int[] east = {currentIndex[0], currentIndex[1]+1};
			int[] north = {currentIndex[0]-1, currentIndex[1]};
			int[] south = {currentIndex[0]+1, currentIndex[1]};
			if (isValidNieghbor(west, map)) {				
				result.add(new Step(west, this.depth + 1, (this.path + Step.indexToPathElement(west))));
			}
			if (isValidNieghbor(east, map)) {				
				result.add(new Step(east, this.depth + 1, this.path + Step.indexToPathElement(east)));
			}
			if (isValidNieghbor(north, map)) {				
				result.add(new Step(north, this.depth + 1, this.path + Step.indexToPathElement(north)));
			}
			if (isValidNieghbor(south, map)) {				
				result.add(new Step(south, this.depth + 1, this.path + Step.indexToPathElement(south)));
			}
			return result;
		}
		
		public static String indexToPathElement(int[]index) {
			return (index[0]+0) + "," + (index[1]+0) + ";";
		}
		
		private boolean isValidNieghbor(int[]index, char[][]map) {
			if(index[0] == 2 && index[1] == 9) {
				"".getClass();
			}
			return index[0] >=0 && index[0] < map.length && index[1] >= 0 && index[1]<map[0].length && map[index[0]][index[1]] != '#';
		}
	}
}