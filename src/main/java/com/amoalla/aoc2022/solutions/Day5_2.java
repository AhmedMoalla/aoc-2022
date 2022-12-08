package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5_2 extends Day<String> {

    private List<Deque<String>> stacks = new ArrayList<>();

    public Day5_2() {
        super(5);
    }

    @Override
    public String process() {
        readStacks();
        applyInstructions();
        return stacks.stream()
                .map(Deque::peek)
                .collect(Collectors.joining());
    }

    private void readStacks() {
        streamLines()
                .takeWhile(line -> !line.isEmpty())
                .forEach(this::fillStacks);
    }

    private void fillStacks(String line) {
        char[] chars = line.toCharArray();
        for (int i = 1, stackIndex = 0; i < chars.length; i += 4, stackIndex++) {
            if (Character.isDigit(chars[i])) {
                break;
            }

            String letter = Character.toString(chars[i]);
            if (stackIndex == stacks.size()) {
                stacks.add(stackIndex, new ArrayDeque<>());
            }
            if (!letter.isBlank()) {
                stacks.get(stackIndex).addLast(letter);
            }
        }
    }

    private void applyInstructions() {
        int indexLineAndEmptyLine = 2;
        int linesToSkip = stacks.stream()
                .mapToInt(Deque::size)
                .max()
                .orElseThrow() + indexLineAndEmptyLine;
        streamLines()
                .skip(linesToSkip)
                .map(Instruction::parse)
                .forEach(this::applyInstruction);
    }

    private void applyInstruction(Instruction instruction) {
        Deque<String> source = stacks.get(instruction.sourceStack());
        Deque<String> destination = stacks.get(instruction.destinationStack());

        Deque<String> intermediaryStack = new ArrayDeque<>();
        for (int i = 0; i < instruction.nbElements(); i++) {
            String letter = source.removeFirst();
            intermediaryStack.addFirst(letter);
        }
        for (int i = 0; i < instruction.nbElements(); i++) {
            String letter = intermediaryStack.removeFirst();
            destination.addFirst(letter);
        }
    }

    record Instruction(int nbElements, int sourceStack, int destinationStack) {
        private static final Pattern PATTERN = Pattern.compile("move (?<nb>\\d+) from (?<from>\\d+) to (?<to>\\d+)");

        public static Instruction parse(String instruction) {
            Matcher matcher = PATTERN.matcher(instruction);
            if (matcher.find()) {
                int nbElements = Integer.parseInt(matcher.group("nb"));
                int sourceStack = Integer.parseInt(matcher.group("from"));
                int destinationStack = Integer.parseInt(matcher.group("to"));
                return new Instruction(nbElements, sourceStack - 1, destinationStack - 1);
            }
            throw new RuntimeException("Bad instruction format: '" + instruction + "'");
        }
    }
}
