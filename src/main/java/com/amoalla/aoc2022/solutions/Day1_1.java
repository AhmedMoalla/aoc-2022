package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

public class Day1_1 extends Day<Integer> {

    public Day1_1() {
        super(1);
    }

    @Override
    public Integer process() {
        int maxCalories = 0;
        int sumCalories = 0;
        for (String line : allLines()) {
            if (line.isEmpty()) {
                if (sumCalories > maxCalories) {
                    maxCalories = sumCalories;
                }
                sumCalories = 0;
                continue;
            }
            sumCalories += Integer.parseInt(line);
        }
        return maxCalories;
    }
}
