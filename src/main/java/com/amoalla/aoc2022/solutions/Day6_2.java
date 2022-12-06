package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.ArrayDeque;
import java.util.Deque;

public class Day6_2 extends Day<Integer> {

    private static final int START_OF_MESSAGE_SIZE = 14;

    public Day6_2() {
        super(6);
    }

    @Override
    public Integer process() {
        String buffer = allLines().get(0);
        Deque<String> slidingWindow = new ArrayDeque<>();
        char[] charArray = buffer.toCharArray();
        int i;
        for (i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            String str = Character.toString(c);
            slidingWindow.addLast(str);
            if (slidingWindow.size() == START_OF_MESSAGE_SIZE) {
                if (slidingWindow.stream().distinct().count() == START_OF_MESSAGE_SIZE) {
                    break;
                }
                slidingWindow.removeFirst();
            }
        }
        return i + 1;
    }
}
