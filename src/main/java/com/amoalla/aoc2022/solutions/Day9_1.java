package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Day9_1 extends Day<Integer> {

    public Day9_1() {
        super(9);
    }

    @Override
    public Integer process() {
        Knot head = new Knot(0, 0);
        Knot tail = new Knot(0, 0);
        streamLines()
                .map(Action::parse)
                .flatMap(Arrays::stream)
                .forEach(action -> {
                    action.move(head);
                    tail.follow(head);
                });
        return tail.getVisitedPositions().size();
    }

    @Setter
    @Getter
    static class Knot {
        private int x;
        private int y;
        private Set<Position> visitedPositions = new HashSet<>();

        public Knot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void follow(Knot knot) {
            doFollow(knot);
            markCurrentPositionAsVisited();
        }

        private void doFollow(Knot other) {
            if (isTouching(other) || moveOnSameRow(other) || moveOnSameCol(other)) {
                return;
            }

            moveDiagonally(other);
        }

        private boolean moveOnSameRow(Knot other) {
            if (y != other.y) {
                return false;
            }

            if (other.x < x) {
                x = other.x + 1;
            } else {
                x = other.x - 1;
            }
            return true;
        }

        private boolean moveOnSameCol(Knot other) {
            if (x != other.x) {
                return false;
            }

            if (other.y < y) {
                y = other.y + 1;
            } else {
                y = other.y - 1;
            }
            return true;
        }

        private void markCurrentPositionAsVisited() {
            visitedPositions.add(new Position(x, y));
        }

        private void moveDiagonally(Knot other) {
            if (other.x > x && other.y > y) {
                x += 1;
                y += 1;
            } else if (other.x > x && other.y < y) {
                x += 1;
                y -= 1;
            } else if (other.x < x && other.y > y) {
                x -= 1;
                y += 1;
            } else if (other.x < x && other.y < y) {
                x -= 1;
                y -= 1;
            }
        }

        private boolean isTouching(Knot other) {
            return Math.abs(x - other.x) <= 1
                    && Math.abs(y - other.y) <= 1;
        }

        public void incrementX(int increment) {
            x += increment;
        }

        public void incrementY(int increment) {
            y += increment;
        }

    }

    record Position(int x, int y) {
    }

    interface Action {
        void move(Knot knot);

        static Action[] parse(String actionStr) {
            String[] split = actionStr.split(" ");
            int nbSteps = Integer.parseInt(split[1]);
            Action action = switch (split[0]) {
                case "R" -> knot -> knot.incrementX(1);
                case "L" -> knot -> knot.incrementX(-1);
                case "U" -> knot -> knot.incrementY(1);
                case "D" -> knot -> knot.incrementY(-1);
                default -> throw new RuntimeException();
            };

            Action[] actions = new Action[nbSteps];
            Arrays.fill(actions, action);
            return actions;
        }
    }
}
