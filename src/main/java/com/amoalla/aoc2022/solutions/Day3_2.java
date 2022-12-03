package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.List;

public class Day3_2 extends Day<Integer> {

    public Day3_2() {
        super(3);
    }

    @Override
    public Integer process() {
        return streamLineChunks(3)
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

    private String findCommonLetter(List<String> rucksacks) {
        return rucksacks.get(0)
                .chars()
                .mapToObj(Character::toString)
                .filter(rucksacks.get(1)::contains)
                .filter(rucksacks.get(2)::contains)
                .findFirst()
                .orElseThrow();
    }
}
