package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"rawtypes", "unchecked"})
class Tests {

    private static final Map<Class<? extends Day<?>>, Object> TESTS = new LinkedHashMap<>();

    @BeforeAll
    static void init() {
        entry(Day1_1.class, 24000);
        entry(Day1_2.class, 45000);
        entry(Day2_1.class, 15);
        entry(Day2_2.class, 12);
        entry(Day3_1.class, 157);
        entry(Day3_2.class, 70);
        entry(Day4_1.class, 2);
        entry(Day4_2.class, 4);
        entry(Day5_1.class, "CMZ");
        entry(Day5_2.class, "MCD");
        entry(Day6_1.class, 11);
        entry(Day6_2.class, 26);
        entry(Day7_1.class, 95437);
        entry(Day7_2.class, 24933642);
        entry(Day8_1.class, 21);
        entry(Day8_2.class, 8);
        entry(Day9_1.class, 13);
        entry(Day9_2.class, 1);
        entry(Day10_1.class, 13140);
        entry(Day11_1.class, 10605);
        entry(Day11_2.class, 2_713_310_158L);
    }

    @Test
    void tests() {
        for (var clazz : TESTS.keySet()) {
            Day day = newDay(clazz);
            assertEquals(TESTS.get(clazz), day.process(), clazz.getSimpleName());
        }
    }

    @Test
    void lastDayTest() {
        var clazz = (Class<? extends Day<?>>) TESTS.keySet().toArray()[TESTS.size() -1];
        Day day = newDay(clazz);
        assertEquals(TESTS.get(clazz), day.process(), clazz.getSimpleName());
    }

    private Day newDay(Class<?> clazz) {
        try {
            return (Day) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void entry(Class<? extends Day<?>> dayClass, Object expectedValue) {
        TESTS.put(dayClass, expectedValue);
    }
}
