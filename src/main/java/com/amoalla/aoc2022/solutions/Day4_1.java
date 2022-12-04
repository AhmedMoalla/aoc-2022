package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

public class Day4_1 extends Day<Integer> {

    public Day4_1() {
        super(4);
    }

    @Override
    public Integer process() {
        return (int) streamLines()
                .map(line -> line.split(","))
                .filter(this::isIntervalContainerInOther)
                .count();
    }

    private boolean isIntervalContainerInOther(String[] intervals) {
        Interval interval1 = Interval.of(intervals[0]);
        Interval interval2 = Interval.of(intervals[1]);
        return interval1.isContainedIn(interval2) || interval2.isContainedIn(interval1);
    }

    record Interval(int lowerBound, int upperBound) {

        public boolean isContainedIn(Interval other) {
            return lowerBound >= other.lowerBound && upperBound <= other.upperBound;
        }

        public static Interval of(String interval) {
            String[] bounds = interval.split("-");
            int lowerBound = Integer.parseInt(bounds[0]);
            int upperBound = Integer.parseInt(bounds[1]);
            return new Interval(lowerBound, upperBound);
        }
    }
}
