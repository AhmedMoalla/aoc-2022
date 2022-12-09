package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8_1 extends Day<Integer> {

    public Day8_1() {
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

        int edgeTrees = lines.size() * 2 + (lines.get(0).length() - 2) * 2;
        int visibleTrees = edgeTrees;
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
                if (isTreeVisible(tree, byX.get(tree.x()), byY.get(tree.y()))) {
                    visibleTrees++;
                }
            }
        }

        return visibleTrees;
    }

    private boolean isTreeVisible(Tree tree, List<Tree> sameX, List<Tree> sameY) {
        boolean isVisibleLeft = sameY.stream()
                .filter(t -> t.x() < tree.x())
                .allMatch(t -> t.height() < tree.height());

        boolean isVisibleRight = sameY.stream()
                .filter(t -> t.x() > tree.x())
                .allMatch(t -> t.height() < tree.height());

        boolean isVisibleTop = sameX.stream()
                .filter(t -> t.y() < tree.y())
                .allMatch(t -> t.height() < tree.height());

        boolean isVisibleBottom = sameX.stream()
                .filter(t -> t.y() > tree.y())
                .allMatch(t -> t.height() < tree.height());
        return isVisibleLeft || isVisibleRight || isVisibleTop || isVisibleBottom;
    }

    record Tree(int x, int y, int height) {}
}
