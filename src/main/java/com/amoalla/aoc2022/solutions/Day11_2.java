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

public class Day11_2 extends Day<Long> {

    public Day11_2() {
        super(11);
    }

    @Override
    public Long process() {
        Monkey[] monkeys = streamLineChunks(7)
                .map(MonkeyParser::parse)
                .toArray(Monkey[]::new);

        int[] nbInspectedItems = new int[monkeys.length];
        for (int i = 1; i <= 10_000; i++) {
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
                .mapToLong(Integer::longValue)
                .reduce(1, (i1, i2) -> i1 * i2);
    }

    record ItemTransaction(int targetMonkeyId, long item) {}

    @RequiredArgsConstructor
    static class Monkey {

        public static int lcm = 1;
        @Getter
        private final int id;
        @Getter
        private final List<Long> items;
        private final Function<Long, Long> worryLevelOperation;
        private final Function<Long, Integer> nextMonkey;

        public List<ItemTransaction> inspectItems() {
            List<ItemTransaction> transactions = items.stream()
                    .mapToLong(worryLevel -> worryLevelOperation.apply(worryLevel)%Monkey.lcm)
                    .mapToObj(newWorryLevel -> new ItemTransaction(nextMonkey.apply(newWorryLevel), newWorryLevel))
                    .toList();
            items.clear();
            return transactions;
        }

        public void addItem(long item) {
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

            List<Long> items = Arrays.stream(extract(ITEMS, monkeyNotes.get(1), "items").split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toCollection(ArrayList::new));

            Function<Long, Long> worryLevelOperation = extractOperator(monkeyNotes.get(2));

            int divisor = Integer.parseInt(extract(TEST, monkeyNotes.get(3), "divisor"));
            int targetIfTrue = Integer.parseInt(extract(TRUE, monkeyNotes.get(4), "target"));
            int targetIfFalse = Integer.parseInt(extract(FALSE, monkeyNotes.get(5), "target"));
            Function<Long, Integer> nextMonkey = worryLevel -> worryLevel % divisor == 0 ? targetIfTrue : targetIfFalse;

            Monkey.lcm *= divisor;

            return new Monkey(id, items, worryLevelOperation, nextMonkey);
        }

        private static Function<Long, Long> extractOperator(String str) {
            Matcher matcher = OPERATION.matcher(str);
            if (matcher.find()) {
                BinaryOperator<Long> operation = switch (matcher.group("operation")) {
                    case "+" -> Long::sum;
                    case "*" -> (i1, i2) -> i1 * i2;
                    default -> throw new RuntimeException();
                };

                String op1 = matcher.group("op1");
                String op2 = matcher.group("op2");
                if (op1.equals("old") && !op2.equals("old")) {
                    return old -> operation.apply(old, Long.parseLong(op2));
                } else if (!op1.equals("old") && op2.equals("old")) {
                    return old -> operation.apply(Long.parseLong(op1), old);
                } else if (!op1.equals("old")) {
                    return old -> operation.apply(Long.parseLong(op1), Long.parseLong(op2));
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
