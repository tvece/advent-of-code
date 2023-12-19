package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

//TODO: fix this (not even part 1 working)
public class D17 {

	public static void main(String[] args) throws InterruptedException {
		Path filePath = Paths.get("resources/D17.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		Queue<Cord> queue = new LinkedList<>();

		int[][] map = new int[rows.size()][];
		int[][] distances = new int[rows.size()][];
		Cord[][] previous = new Cord[rows.size()][];

		for (int i = 0; i < rows.size(); i++) {
			map[i] = new int[rows.get(0).length()];
			distances[i] = new int[rows.get(0).length()];
			previous[i] = new Cord[rows.get(0).length()];
			for (int j = 0; j < rows.get(0).length(); j++) {
				map[i][j] = Integer.parseInt(String.valueOf(rows.get(i).charAt(j)));
				queue.add(new Cord(i, j));
				distances[i][j] = Integer.MAX_VALUE;
				previous[i][j] = null;
			}
		}

		distances[0][0] = map[0][0];

		// map[0][4] = 999;

		while (!queue.isEmpty()) {
			Cord currentCord = queue.stream()
					.min((cordA, cordB) -> Integer.compare(distances[cordA.x][cordA.y], distances[cordB.x][cordB.y]))
					.get();
			if (currentCord.x == map.length - 1 && currentCord.y == map[0].length - 1) {
				break;
			}
			/*
			for (int row = 0; row < distances.length; row++) {
				for (int column = 0; column < distances[0].length; column++) {
					System.out.print((distances[row][column]<500?distances[row][column]:"X")+ "   ");
				}
				System.out.println();
			}
			System.out.println();
			*/
			if(currentCord.x == 1 && currentCord.y==4) {
				"".getClass();
			}
			queue.remove(currentCord);
			for (int i = currentCord.x - 1; i <= currentCord.x + 1; i++) {
				for (int j = currentCord.y - 1; j <= currentCord.y + 1; j++) {
					int iDiff = currentCord.x - i;
					int jDiff = currentCord.y - j;

					if (i >= 0 && i < map.length && j >= 0 && j < map[0].length && iDiff != jDiff && (iDiff == 0 || jDiff == 0)) {
						final int finalI = i;
						final int finalJ = j;
						Optional<Cord> optionalNeighbor = queue.stream()
								.filter(cord -> cord.x == finalI && cord.y == finalJ).findFirst();
						if (optionalNeighbor.isPresent()) {
							Cord neighbor = optionalNeighbor.get();
							//System.out.println(currentCord + "->" + neighbor);
							List<Cord> sequence = new ArrayList<D17.Cord>();
							sequence.add(neighbor);
							Cord sequenceCheck = currentCord;
							while (sequence.size() < 5 && sequenceCheck != null) {
								sequence.add(sequenceCheck);
								sequenceCheck = previous[sequenceCheck.x][sequenceCheck.y];
							}
							Collections.reverse(sequence);
							if (sequence.size() == 5) {
								String previousDirection = null;
								Cord previousSequenceElement = null;
								boolean foundDifference = false;
								previousSequenceElement = sequence.get(0);
								String currentDirection = null;
								for (int k = 1; k < sequence.size(); k++) {
									Cord currentSequenceElement = sequence.get(k);
									if (currentSequenceElement.x == previousSequenceElement.x
											&& currentSequenceElement.y > previousSequenceElement.y) {
										currentDirection = "E";
									} else if (currentSequenceElement.x == previousSequenceElement.x
											&& currentSequenceElement.y < previousSequenceElement.y) {
										currentDirection = "W";
									} else if (currentSequenceElement.x > previousSequenceElement.x
											&& currentSequenceElement.y == previousSequenceElement.y) {
										currentDirection = "S";
									} else if (currentSequenceElement.x < previousSequenceElement.x
											&& currentSequenceElement.y == previousSequenceElement.y) {
										currentDirection = "N";
									}
									if (previousDirection != null && currentDirection != previousDirection) {
										foundDifference = true;
										break;
									}
									previousSequenceElement = currentSequenceElement;
									previousDirection = currentDirection;
								}
								//System.out.println(sequence + " " + previousDirection + " " + currentDirection + " " + foundDifference);
								if (!foundDifference) {
									continue;
								}
							}

							int alt = distances[currentCord.x][currentCord.y] + map[neighbor.x][neighbor.y];
							//System.out.println(currentCord + "->" + neighbor + "   " + distances[neighbor.x][neighbor.y] + "->" + alt);
							if (alt < distances[neighbor.x][neighbor.y]) {
								distances[neighbor.x][neighbor.y] = alt;
								previous[neighbor.x][neighbor.y] = currentCord;
							}
						}
					}
				}
			}
		}

		distances.getClass();
		List<Cord> sequence = new ArrayList<>();
		//Cord currentCord = new Cord(map.length - 1, map[0].length - 1);
		Cord currentCord = new Cord(0, map[0].length - 1);
		int heatLoss = 0;
		if (previous[currentCord.x][currentCord.y] != null && !(currentCord.x == 0 && currentCord.y == 0)) {
			while (currentCord != null) {
				sequence.add(currentCord);
				heatLoss += map[currentCord.x][currentCord.y];
				Cord previousCord = previous[currentCord.x][currentCord.y];
				if (previousCord == null) {
					break;
				}
				currentCord = new Cord(previous[currentCord.x][currentCord.y].x,
						previous[currentCord.x][currentCord.y].y);
			}
		}

		System.out.println(sequence);

		System.out.println();
		for (int row = 0; row < map.length; row++) {
			for (int column = 0; column < map[0].length; column++) {
				System.out.print(map[row][column]);
			}
			System.out.println();
		}
		System.out.println();
		
		for (int row = 0; row < map.length; row++) {
			for (int column = 0; column < map[0].length; column++) {
				final int finalRow = row;
				final int finalColumn = column;
				if (sequence.stream().filter(cord -> cord.x == finalRow && cord.y == finalColumn).findFirst()
						.isPresent()) {
					System.out.print('#');
				} else {
					System.out.print(map[row][column]);
				}
			}
			System.out.println();
		}

		System.out.println(heatLoss);
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
}