package com.amoalla.aoc2022.solutions;

import com.amoalla.aoc2022.Day;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11_1 extends Day<Integer> {
    public Day11_1() {
        super(11);
    }

    @Override
    public Integer process() {
        Monkey[] monkeys = streamLineChunks(7)
                .map(MonkeyParser::parse)
                .toArray(Monkey[]::new);

        int[] nbInspectedItems = new int[monkeys.length];
        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeys) {
                List<ItemTransaction> transactions = monkey.inspectItems();
                nbInspectedItems[monkey.getId()] += transactions.size();
                for (ItemTransaction transaction : transactions) {
                    Monkey targetMonkey = monkeys[transaction.targetMonkeyId()];
                    targetMonkey.addItem(transaction.item());
                }
            }
        }
        return Arrays.stream(nbInspectedItems)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .reduce(1, (i1, i2) -> i1 * i2);
    }

    record ItemTransaction(int targetMonkeyId, int item) {}

    @RequiredArgsConstructor
    static class Monkey {
        @Getter
        private final int id;
        @Getter
        private final List<Integer> items;
        private final Function<Integer, Integer> worryLevelOperation;
        private final Function<Integer, Integer> nextMonkey;

        public List<ItemTransaction> inspectItems() {
            List<ItemTransaction> transactions = items.stream()
                    .mapToInt(worryLevel -> worryLevelOperation.apply(worryLevel) / 3)
                    .mapToObj(newWorryLevel -> new ItemTransaction(nextMonkey.apply(newWorryLevel), newWorryLevel))
                    .toList();
            items.clear();
            return transactions;
        }

        public void addItem(int item) {
            items.add(item);
        }
    }

    static class MonkeyParser {
        private static final Pattern ID = Pattern.compile("Monkey (?<id>\\d+):");
        private static final Pattern ITEMS = Pattern.compile("\\s+Starting items: (?<items>.*)");
        private static final Pattern OPERATION = Pattern.compile("\\s+Operation: new = (?<op1>old|\\d+) (?<operation>[*+]) (?<op2>old|\\d+)");
        private static final Pattern TEST = Pattern.compile("\\s+Test: divisible by (?<divisor>\\d+)");
        private static final Pattern TRUE = Pattern.compile("\\s+If true: throw to monkey (?<target>\\d+)");
        private static final Pattern FALSE = Pattern.compile("\\s+If false: throw to monkey (?<target>\\d+)");
        public static Monkey parse(List<String> monkeyNotes) {

            int id = Integer.parseInt(extract(ID, monkeyNotes.get(0), "id"));

            List<Integer> items = Arrays.stream(extract(ITEMS, monkeyNotes.get(1), "items").split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toCollection(ArrayList::new));

            Function<Integer, Integer> worryLevelOperation = extractOperator(monkeyNotes.get(2));

            int divisor = Integer.parseInt(extract(TEST, monkeyNotes.get(3), "divisor"));
            int targetIfTrue = Integer.parseInt(extract(TRUE, monkeyNotes.get(4), "target"));
            int targetIfFalse = Integer.parseInt(extract(FALSE, monkeyNotes.get(5), "target"));
            Function<Integer, Integer> nextMonkey = worryLevel -> worryLevel % divisor == 0 ? targetIfTrue : targetIfFalse;

            return new Monkey(id, items, worryLevelOperation, nextMonkey);
        }

        private static Function<Integer, Integer> extractOperator(String str) {
            Matcher matcher = OPERATION.matcher(str);
            if (matcher.find()) {
                BinaryOperator<Integer> operation = switch (matcher.group("operation")) {
                    case "+" -> Integer::sum;
                    case "*" -> (i1, i2) -> i1 * i2;
                    default -> throw new RuntimeException();
                };

                String op1 = matcher.group("op1");
                String op2 = matcher.group("op2");
                if (op1.equals("old") && !op2.equals("old")) {
                    return old -> operation.apply(old, Integer.parseInt(op2));
                } else if (!op1.equals("old") && op2.equals("old")) {
                    return old -> operation.apply(Integer.parseInt(op1), old);
                } else if (!op1.equals("old")) {
                    return old -> operation.apply(Integer.parseInt(op1), Integer.parseInt(op2));
                } else {
                    return old -> operation.apply(old, old);
                }
            }

            throw new RuntimeException();
        }

        private static String extract(Pattern pattern, String str, String groupName) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                return matcher.group(groupName);
            }
            throw new RuntimeException();
        }
    }
}
