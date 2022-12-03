package com.amoalla.aoc2022;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
        return readFile().lines();
    }

    protected Stream<List<String>> streamLineChunks(int chunkSize) {
        InputStream inputStream = getInputStream();
        Scanner scanner = new Scanner(inputStream);
        return scanner.findAll("(.*\\R){1," + chunkSize + "}")
                .map(mr -> Arrays.asList(mr.group().split("\\R")));
    }

    private BufferedReader readFile() {
        InputStream inputStream = getInputStream();
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private InputStream getInputStream() {
        String inputPath = "inputs/day%d/input.txt".formatted(day);
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(inputPath);
        if (inputStream == null) {
            throw new RuntimeException("Input file was not found.");
        }
        return inputStream;
    }

}
