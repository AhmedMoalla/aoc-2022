package com.amoalla.aoc2022;

import org.reflections.Reflections;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.reflections.scanners.Scanners.SubTypes;

@SuppressWarnings("rawtypes")
public class Launcher {

    private static final Pattern authorizedClassNames = Pattern.compile("Day(?<problemNumber>[1-9]+(_?[1-9]+)?)");

    public static void main(String[] args) {
        new Reflections(Launcher.class.getPackageName())
                .get(SubTypes.of(Day.class).asClass())
                .stream()
                .sorted(Comparator.comparing(Class::getSimpleName))
                .map(Launcher::newDay)
                .forEach(Launcher::executeDay);
    }

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
