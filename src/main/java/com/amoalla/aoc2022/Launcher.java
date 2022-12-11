package com.amoalla.aoc2022;

import org.reflections.Reflections;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.reflections.scanners.Scanners.SubTypes;

@SuppressWarnings("rawtypes")
public class Launcher {

    private static final Pattern authorizedClassNames = Pattern.compile("Day(?<problemNumber>\\d+_[1-2])");

    public static void main(String[] args) {
        new Reflections(Launcher.class.getPackageName())
                .get(SubTypes.of(Day.class).asClass())
                .stream()
                .sorted(Launcher::byDay)
                .map(Launcher::newDay)
                .forEach(Launcher::executeDay);
    }

    private static int byDay(Class<?> class1, Class<?> class2) {
        DayAndPart dp1 = getDayAndPart(class1);
        DayAndPart dp2 = getDayAndPart(class2);
        if (dp1.day() != dp2.day()) {
            return dp1.day() - dp2.day();
        }
        return dp1.part() - dp2.part();
    }

    private static DayAndPart getDayAndPart(Class<?> clazz) {
        String simpleName = clazz.getSimpleName();
        Matcher matcher = authorizedClassNames.matcher(simpleName);
        if (!matcher.find()) {
            throw new RuntimeException("Could not extract problem number.");
        }
        String problemNumber = matcher.group("problemNumber");
        String[] split = problemNumber.split("_");
        int day = Integer.parseInt(split[0]);
        int part = Integer.parseInt(split[1]);
        return new DayAndPart(day, part);
    }

    record DayAndPart(int day, int part) {}

    public static Day newDay(Class<?> clazz) {
        try {
            String className = clazz.getSimpleName();
            if (!authorizedClassNames.asMatchPredicate().test(className)) {
                throw new RuntimeException("Class name '" + className + "' is unauthorized.");
            }
            return (Day) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeDay(Day day) {
        String simpleName = day.getClass().getSimpleName();
        Matcher matcher = authorizedClassNames.matcher(simpleName);
        if (!matcher.find()) {
            throw new RuntimeException("Could not extract problem number.");
        }
        String problemNumber = matcher.group("problemNumber");
        System.out.printf("Day %s -> %s%n", problemNumber, day.process());
    }
}
