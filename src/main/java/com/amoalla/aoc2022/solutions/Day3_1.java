package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

public class Day3_1 extends Day<Integer> {

    public Day3_1() {
        super(3);
    }

    @Override
    public Integer process() {
        return streamLines()
                .map(this::splitInTwo)
                .map(this::findCommonLetter)
                .mapToInt(this::calculatePriority)
                .sum();
    }

    private int calculatePriority(String letterStr) {
        char letter = letterStr.charAt(0);
        if (Character.isLowerCase(letter)) {
            return letter - 'a' + 1;
        }
        return letter - 'A' + 27;
    }

    private String findCommonLetter(String[] rucksacks) {
        return rucksacks[0].chars()
                .mapToObj(Character::toString)
                .filter(rucksacks[1]::contains)
                .findFirst()
                .orElseThrow();
    }

    private String[] splitInTwo(String line) {
        return new String[]{
                line.substring(0, line.length() / 2),
                line.substring(line.length() / 2)
        };
    }

}
