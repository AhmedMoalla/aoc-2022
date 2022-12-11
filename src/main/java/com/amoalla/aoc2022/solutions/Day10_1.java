package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;
import lombok.Getter;

import java.util.Set;

public class Day10_1 extends Day<Integer> {

    public Day10_1() {
        super(10);
    }

    @Override
    public Integer process() {
        CPU cpu = new CPU();
        streamLines()
                .map(Instruction::parse)
                .forEach(cpu::execute);
        return cpu.getTotalSignalStrength();
    }

    abstract static class Instruction {

        protected int operand;
        protected Instruction(int operand) {
            this.operand = operand;
        }

        abstract int execute(int x);
        abstract int cycles();
        static Instruction parse(String instruction) {
            String[] split = instruction.split(" ");
            return switch (split[0]) {
                case "addx" -> new AddX(Integer.parseInt(split[1]));
                case "noop" -> new Noop();
                default -> throw new RuntimeException();
            };
        }
    }

    static class AddX extends Instruction {

        public AddX(int operand) {
            super(operand);
        }

        @Override
        public int execute(int x) {
            return x + operand;
        }

        @Override
        public int cycles() {
            return 2;
        }
    }

    static class Noop extends Instruction {

        public Noop() {
            super(0);
        }

        @Override
        public int execute(int x) {
            return x;
        }

        @Override
        public int cycles() {
            return 1;
        }
    }

    @Getter
    static class CPU {

        private static final Set<Integer> cyclesToCheck = Set.of(20, 60, 100, 140, 180, 220);
        private int x = 1;
        private int currentCycle = 0;
        private int totalSignalStrength = 0;

        public void execute(Instruction instruction) {
            for (int i = 0; i < instruction.cycles(); i++) {
                currentCycle++;
                if (cyclesToCheck.contains(currentCycle)) {
                    totalSignalStrength += x * currentCycle;
                }
            }
            x = instruction.execute(x);
        }
    }
}