package com.amoalla.aoc2022;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

public abstract class Day<O> {

    private final int day;

    protected Day(int day) {
        this.day = day;
    }

    public abstract O process();

    protected List<String> allLines() {
        return streamLines().toList();
    }

    protected Stream<String> streamLines() {
        String inputPath = "inputs/day%d/input.txt".formatted(day);
        return readFile(inputPath).lines();
    }

    private BufferedReader readFile(String filePath) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(filePath);
        if (inputStream == null) {
            throw new RuntimeException("Input file was not found.");
        }
        return new BufferedReader(new InputStreamReader(inputStream));
    }

}
