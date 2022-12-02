package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.Map;

public class Day2_2 extends Day<Integer> {

    public Day2_2() {
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

    record Round(String opponent, String result) {

        public Round(String[] round) {
            this(round[0], round[1]);
        }

        public int score() {
            Shape opponentShape = Shape.parse(opponent);
            Result expectedResult = Result.parse(result);
            Shape playerShape = expectedResult.determineShape(opponentShape);
            return playerShape.calculateScore(opponentShape);
        }
    }

    enum Result {
        LOSE, DRAW, WIN;

        public Shape determineShape(Shape opponent) {
            return switch (this) {
                case LOSE -> opponent.winsAgainst();
                case DRAW -> opponent;
                case WIN -> opponent.winsAgainst().winsAgainst();
            };
        }

        public static Result parse(String letter) {
            return switch (letter) {
                case "X" -> LOSE;
                case "Y" -> DRAW;
                case "Z" -> WIN;
                default -> throw new IllegalArgumentException();
            };
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

        public Shape winsAgainst() {
            return winsAgainst.get(this);
        }

        public static Shape parse(String letter) {
            return switch (letter) {
                case "A" -> ROCK;
                case "B" -> PAPER;
                case "C" -> SCISSORS;
                default -> throw new IllegalArgumentException();
            };
        }
    }
}
