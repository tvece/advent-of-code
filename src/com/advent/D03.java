package com.advent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class D03 {

	public static void main(String[] args) {
		Path filePath = Paths.get("resources/D03.txt");
		List<String> lines = new ArrayList<String>();
		try {
			lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		List<List<Integer>> resultNumbers = new ArrayList<>();
		for(int lineIterator = 0; lineIterator < lines.size(); lineIterator++) {
			String line = lines.get(lineIterator);
			int charIterator = 0;	
			List<Integer> currentNumber = new ArrayList<>();

				boolean wasNextToSymbol = false;
				while(charIterator < line.length()) {
					char charString = line.charAt(charIterator);
					if(Character.isDigit(charString)){
						if(!wasNextToSymbol && isNextToSymbol(charIterator, lineIterator, lines)) {
							wasNextToSymbol = true;
						}							
						currentNumber.add(Character.getNumericValue(charString)); 
					} else {						
						if(!currentNumber.isEmpty() && wasNextToSymbol) {
							resultNumbers.add(new ArrayList<Integer>(currentNumber));
						}
						wasNextToSymbol = false;
						if(!currentNumber.isEmpty()) {
							currentNumber.clear();
						}
					}
					charIterator++;
				}
				if(!currentNumber.isEmpty() && wasNextToSymbol) {
					resultNumbers.add(new ArrayList<Integer>(currentNumber));
				}
				wasNextToSymbol = false;
				if(!currentNumber.isEmpty()) {							
					currentNumber.clear();
				}
		}
		
		Integer result = 0;
		for (int i = 0; i<resultNumbers.size(); i++) {
			List<Integer> resultNumber = resultNumbers.get(i);
			for (int j = 0; j<resultNumber.size(); j++) {			
				result+= (((int)Math.pow(10, resultNumber.size()-j-1) *resultNumber.get(j)));
			}
		}
		System.out.println(result);
		
	}

	private static boolean isNextToSymbol(int x, int y, List<String> lines) {
		for (int xrange = x-1; xrange<=x+1;xrange++) {
			if(xrange >= 0 && lines.get(0).length() > xrange) {
				for(int yrange = y-1; yrange<=y+1;yrange++) {
					if(yrange >= 0 && lines.size() > yrange) {
						if(xrange != 0 && yrange != 0) {							
							char charCheck = lines.get(yrange).charAt(xrange);
							if(!Character.isDigit(charCheck) && charCheck != '.') {
								return true;
							}
						}
					}
				}				
			}
		}
		return false;
	}
	
	
}
