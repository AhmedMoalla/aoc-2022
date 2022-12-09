package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.*;

public class Day8_2 extends Day<Integer> {

    public Day8_2() {
        super(8);
    }

    @Override
    public Integer process() {
        Map<Integer, List<Tree>> byX = new HashMap<>();
        Map<Integer, List<Tree>> byY = new HashMap<>();
        List<String> lines = allLines();
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.toCharArray().length; x++) {
                int height = Integer.parseInt(Character.toString(line.charAt(x)));
                Tree tree = new Tree(x, y, height);
                byX.putIfAbsent(x, new ArrayList<>());
                byY.putIfAbsent(y, new ArrayList<>());
                byX.get(x).add(tree);
                byY.get(y).add(tree);
            }
        }

        Set<Integer> scores = new HashSet<>();
        for (var entry : byX.entrySet()) {
            int x = entry.getKey();
            if (x == 0 || x == byX.size() - 1) {
                continue;
            }

            List<Tree> row = entry.getValue();
            for (Tree tree : row) {
                if (tree.y() == 0 || tree.y() == byY.size() - 1) {
                    continue;
                }
                int score = calculateSceneryScore(tree, byX.get(tree.x()), byY.get(tree.y()));
                scores.add(score);
            }
        }

        return scores.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow();
    }

    private int calculateSceneryScore(Tree tree, List<Tree> sameX, List<Tree> sameY) {

        long leftScore = 0;
        List<Tree> leftTrees = sameY.stream()
                .filter(t -> t.x() < tree.x())
                .toList();
        for (int i = leftTrees.size() - 1; i >= 0; i--) {
            leftScore++;
            if (leftTrees.get(i).height() >= tree.height()) {
                break;
            }
        }

        long rightScore = 0;
        List<Tree> rightTrees = sameY.stream()
                .filter(t -> t.x() > tree.x())
                .toList();
        for (Tree rightTree : rightTrees) {
            rightScore++;
            if (rightTree.height() >= tree.height()) {
                break;
            }
        }


        long topScore = 0;
        List<Tree> topTrees = sameX.stream()
                .filter(t -> t.y() < tree.y())
                .toList();
        for (int i = topTrees.size() - 1; i >= 0; i--) {
            topScore++;
            if (topTrees.get(i).height() >= tree.height()) {
                break;
            }
        }

        long bottomScore = 0;
        List<Tree> bottomTrees = sameX.stream()
                .filter(t -> t.y() > tree.y())
                .toList();
        for (Tree rightTree : bottomTrees) {
            bottomScore++;
            if (rightTree.height() >= tree.height()) {
                break;
            }
        }

        return (int) (leftScore * rightScore * topScore * bottomScore);
    }

    record Tree(int x, int y, int height) {
    }
}
