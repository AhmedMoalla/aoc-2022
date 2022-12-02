package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.Map;

public class Day2_1 extends Day<Integer> {

    public Day2_1() {
        super(2);
    }

    @Override
    public Integer process() {
        return streamLines()
                .map(line -> line.split(" "))
                .map(Round::new)
                .mapToInt(Round::score)
                .sum();
    }

    record Round(String opponent, String player) {

        public Round(String[] round) {
            this(round[0], round[1]);
        }

        public int score() {
            Shape opponentShape = Shape.parse(opponent);
            Shape playerShape = Shape.parse(player);
            return playerShape.calculateScore(opponentShape);
        }
    }

    enum Shape {
        ROCK(1), PAPER(2), SCISSORS(3);

        private static final Map<Shape, Shape> winsAgainst = Map.of(
                ROCK, SCISSORS,
                PAPER, ROCK,
                SCISSORS, PAPER
        );

        private final int score;

        Shape(int score) {
            this.score = score;
        }

        public int calculateScore(Shape opponent) {
            int finalScore = score;
            if (winsAgainst.get(this).equals(opponent)) {
                finalScore += 6;
            } else if (this.equals(opponent)) {
                finalScore += 3;
            }
            return finalScore;
        }

        public static Shape parse(String letter) {
            return switch (letter) {
                case "A", "X" -> ROCK;
                case "B", "Y" -> PAPER;
                case "C", "Z" -> SCISSORS;
                default -> throw new IllegalArgumentException();
            };
        }
    }
}
