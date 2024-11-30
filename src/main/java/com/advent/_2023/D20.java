package com.advent._2023;

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
import java.util.stream.Stream;

//TODO: part 2
public class D20 {

	private static String FALLBACK = "FALLBACK";
	
	public static void main(String[] args) throws InterruptedException {
		Path filePath = Paths.get("src/main/resources/2023/D20.txt");
		List<String> rows = new ArrayList<String>();
		try {
			rows = Files.readAllLines(filePath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read input data!", e);
		}

		String[] input = new String[1];
		Map<String, Module> map = new HashMap<>();
		for (String row : rows) {
			if (row.startsWith("broadcaster")) {
				row = row.substring(15);
				input = row.split(", ");
			} else if (row.startsWith("%")) {
				FlipFlop module = new FlipFlop(row);
				map.put(module.id, module);
			} else if (row.startsWith("&")) {
				Conjunction module = new Conjunction(row);
				map.put(module.id, module);
			}
		}

		map.values().forEach(module -> {
			if (module instanceof Conjunction) {
				final List<String> inputs = new ArrayList<>();
				map.values().forEach(destinationCheck -> {
					if (destinationCheck.destinations.contains(module.id)) {
						inputs.add(destinationCheck.id);
					}
				});
				((Conjunction) module).setMemory(inputs);
			}
		});
		map.put(FALLBACK, new Fallback());

		int lowPulsesCount = 0;
		int highPulsesCount = 0;
		for (int i = 0; i < 1000; i++) {
			List<Module> initialModules = Stream.of(input).map(id -> map.get(id)).toList();
			final List<Command> currentCommands = new ArrayList<>();
			// +1 for broadcaster
			lowPulsesCount++;
			initialModules.forEach(module -> currentCommands.addAll(module.input(Pulse.LOW, null)));
			lowPulsesCount += initialModules.size();

			while (!currentCommands.isEmpty()) {
				currentCommands.size();
				List<Command> nextCommands = new ArrayList<>();
				for (Command command : currentCommands) {
					if (command.pulse == Pulse.HIGH) {
						highPulsesCount++;
					} else {
						lowPulsesCount++;
					}
					nextCommands.addAll(map.get(map.containsKey(command.moduleId)?command.moduleId: "FALLBACK").input(command.pulse, command.origin));
				}
				currentCommands.clear();
				currentCommands.addAll(nextCommands);
			}
		}

		System.out.println(highPulsesCount * lowPulsesCount);
	}

	private static class Module {
		protected List<String> destinations;
		String id;

		public List<Command> input(Pulse pulse, Module module) {
			throw new RuntimeException("Not implemented");
		};

		public Module(String row) {
			row = row.substring(1);
			this.id = row.substring(0, row.indexOf(' '));
			row = row.substring(row.indexOf('>') + 2);
			destinations = Arrays.asList(row.split(", "));
		}
	}

	private static class FlipFlop extends Module {
		public FlipFlop(String row) {
			super(row);
		}

		boolean state = false;

		public List<Command> input(Pulse pulse, Module module) {
			System.out.println((module != null ? module.id : "broadcaster") + " " + pulse + " -> " + this.id);
			if (pulse == Pulse.LOW) {
				state = !state;
				return destinations.stream()
						.map(destination -> new Command(destination, state ? Pulse.HIGH : Pulse.LOW, this)).toList();
			}
			return Arrays.asList();
		}

		public String toString() {
			return id + " " + state + " " + destinations;
		}
	}

	private static class Conjunction extends Module {
		public Conjunction(String row) {
			super(row);
		}

		private Map<String, Pulse> memory;

		public List<Command> input(Pulse pulse, Module module) {
			System.out.println((module != null ? module.id : "broadcaster") + " " + pulse + " -> " + this.id);
			memory.put(module.id, pulse);
			return destinations.stream().map(destination -> new Command(destination,
					memory.values().contains(Pulse.LOW) ? Pulse.HIGH : Pulse.LOW, this)).toList();
		}

		public void setMemory(List<String> moduleIds) {
			memory = new HashMap<>();
			moduleIds.forEach(id -> {
				memory.put(id, Pulse.LOW);
			});
		}

		public String toString() {
			return id + " " + memory + " " + destinations;
		}
	}

	private static class Fallback extends Module {
		public Fallback() {
			super(FALLBACK + " -> " + FALLBACK);
		}

		public List<Command> input(Pulse pulse, Module module) {
			return Arrays.asList();
		}

		public String toString() {
			return id;
		}
	}
	
	private static class Command {
		String moduleId;
		Pulse pulse;
		Module origin;

		public Command(String moduleId, Pulse pulse, Module origin) {
			this.moduleId = moduleId;
			this.pulse = pulse;
			this.origin = origin;
		}

		public String toString() {
			return moduleId + " " + pulse + " (" + origin + ")";
		}
	}

	public enum Pulse {
		LOW, HIGH
	}
}