package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

public class Day4_2 extends Day<Integer> {

    public Day4_2() {
        super(4);
    }

    @Override
    public Integer process() {
        return (int) streamLines()
                .map(line -> line.split(","))
                .filter(this::isIntervalsOverlap)
                .count();
    }

    private boolean isIntervalsOverlap(String[] intervals) {
        Interval interval1 = Interval.of(intervals[0]);
        Interval interval2 = Interval.of(intervals[1]);
        return interval1.overlaps(interval2) || interval2.overlaps(interval1);
    }

    record Interval(int lowerBound, int upperBound) {

        public boolean overlaps(Interval other) {
            boolean lowerBoundContainedInOther = lowerBound <= other.upperBound && lowerBound >= other.lowerBound;
            boolean upperBoundContainedInOther = upperBound <= other.upperBound && upperBound >= other.lowerBound;
            return lowerBoundContainedInOther || upperBoundContainedInOther;
        }

        public static Interval of(String interval) {
            String[] bounds = interval.split("-");
            int lowerBound = Integer.parseInt(bounds[0]);
            int upperBound = Integer.parseInt(bounds[1]);
            return new Interval(lowerBound, upperBound);
        }
    }
}
