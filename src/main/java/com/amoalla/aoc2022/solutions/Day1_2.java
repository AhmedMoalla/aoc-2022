package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Day1_2 extends Day<Integer> {

    public Day1_2() {
        super(1);
    }

    @Override
    public Integer process() {
        Set<Integer> sums = new TreeSet<>(Comparator.reverseOrder());
        int sumCalories = 0;
        for (String line : allLines()) {
            if (line.isEmpty()) {
                sums.add(sumCalories);
                sumCalories = 0;
                continue;
            }
            sumCalories += Integer.parseInt(line);
        }

        return sums.stream()
                .limit(3)
                .mapToInt(value -> value)
                .sum();
    }
}
