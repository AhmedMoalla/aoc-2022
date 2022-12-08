package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;

import java.util.*;

public class Day7_2 extends Day<Integer> {

    private static final int TOTAL_DISK_SIZE = 70_000_000;
    private static final int UPDATE_SIZE = 30_000_000;

    public Day7_2() {
        super(7);
    }

    @Override
    public Integer process() {
        Set<Node> selectedNodes = new HashSet<>();
        Node root = new Node("/", null);
        Node currentNode = root;
        Command currentCommand = null;
        for (String line : allLines()) {
            String[] split = line.split(" ");
            if (line.startsWith("$")) {
                currentCommand = Command.parse(split[1], root, selectedNodes);
                if (!currentCommand.hasMultilineInput()) {
                    currentNode = currentCommand.execute(currentNode, split[2]);
                }
                continue;
            }

            currentNode = currentCommand.execute(currentNode, line);
        }

        int remainingSpace = TOTAL_DISK_SIZE - root.size();
        int spaceToFree = UPDATE_SIZE - remainingSpace;

        return selectedNodes.stream()
                .mapToInt(Node::size)
                .filter(size -> size >= spaceToFree)
                .min()
                .orElseThrow();
    }

    interface Command {
        Node execute(Node root, String input);
        boolean hasMultilineInput();

        static Command parse(String command, Node root, Set<Node> selectedNodes) {
            return switch (command) {
                case "cd" -> new CdCommand(root);
                case "ls" -> new LsCommand(root, selectedNodes);
                default -> throw new RuntimeException();
            };
        }
    }

    static class CdCommand implements Command {

        private final Node root;

        public CdCommand(Node root) {
            this.root = root;
        }

        @Override
        public Node execute(Node currentDirectory, String directory) {
            return switch (directory) {
                case "/" -> root;
                case ".." -> currentDirectory.parent();
                default -> currentDirectory.children().stream()
                        .filter(node -> node.name().equals(directory))
                        .findFirst()
                        .orElseThrow();
            };
        }

        @Override
        public boolean hasMultilineInput() {
            return false;
        }
    }

    static class LsCommand implements Command {

        private final Node root;
        private final Set<Node> selectedNodes;

        public LsCommand(Node root, Set<Node> selectedNodes) {
            this.root = root;
            this.selectedNodes = selectedNodes;
        }

        @Override
        public Node execute(Node currentNode, String input) {

            int remainingSpace = TOTAL_DISK_SIZE - root.size();
            int spaceToFree = UPDATE_SIZE - remainingSpace;

            File currentFile = readFile(input);
            if (currentFile.isDirectory()) {
                Node newNode = new Node(currentFile.name(), currentNode);
                currentNode.children().add(newNode);
            } else {
                currentNode.children().add(new Node(currentFile.name(), currentNode, currentFile.size()));
                currentNode.setSize(currentNode.size() + currentFile.size());
                if (currentNode.size() >= spaceToFree) {
                    selectedNodes.add(currentNode);
                }
                Node parent = currentNode;
                while((parent = parent.parent()) != null) {
                    parent.setSize(parent.size() + currentFile.size());
                    if (parent.size() >= spaceToFree) {
                        selectedNodes.add(parent);
                    }
                }
            }
            return currentNode;
        }

        private File readFile(String input) {
            String[] split = input.split(" ");
            String name = split[1];
            if (split[0].equals("dir")) {
                return new File(name, 0, true);
            } else {
                int size = Integer.parseInt(split[0]);
                return new File(name, size, false);
            }
        }

        record File(String name, int size, boolean isDirectory) {}

        @Override
        public boolean hasMultilineInput() {
            return true;
        }
    }

    static class Node {
        private final String name;
        private final Node parent;
        private final List<Node> children = new ArrayList<>();
        private int size;

        public Node(String name, Node parent) {
            this.name = name;
            this.parent = parent;
        }

        public Node(String name, Node parent, int size) {
            this(name, parent);
            this.size = size;
        }

        public String name() {
            return name;
        }

        public Node parent() {
            return parent;
        }

        public List<Node> children() {
            return children;
        }

        public int size() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name) && Objects.equals(parent, node.parent);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, parent);
        }
    }

}

