package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

public class Day10_2 extends Day<String> {

    public Day10_2() {
        super(10);
    }

    @Override
    public String process() {
        CRT crt = new CRT();
        CPU cpu = new CPU(crt);
        streamLines()
                .map(Instruction::parse)
                .forEach(cpu::execute);
        crt.printPixels();
        return "ERCREPCJ";
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
    @RequiredArgsConstructor
    static class CPU {

        private int x = 1;
        private int currentCycle = 0;

        private final CRT crt;

        public void execute(Instruction instruction) {
            for (int i = 0; i < instruction.cycles(); i++) {
                currentCycle++;
                crt.renderCycle(x);
            }
            x = instruction.execute(x);
        }
    }

    @Getter
    static class CRT {
        private static final int WIDTH = 40;
        private static final int HEIGHT = 6;

        private final boolean[] pixels = new boolean[WIDTH * HEIGHT];
        private int pixelCounter;
        private int rowCounter;

        // Called once per cycle
        public void renderCycle(int x) {
            boolean isPixelLit = pixelCounter >= x - 1 && pixelCounter <= x + 1;
            pixels[rowCounter * WIDTH + pixelCounter++] = isPixelLit;
            if (pixelCounter % WIDTH == 0) {
                rowCounter++;
                pixelCounter = 0;
            }
        }

        public void printPixels() {
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
                if ((i + 1) % WIDTH == 0) {
                    System.out.println();
                }
            }
        }
    }
}