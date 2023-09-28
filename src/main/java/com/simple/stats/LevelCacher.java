package com.simple.stats;

import java.util.HashMap;
import java.util.Map;

public class LevelCacher<T extends Comparable<T>> {

    private final Map<Integer, T> cache = new HashMap<>();
    private final Func<T> func;

    private int currentLevel;

    public LevelCacher(Func<T> func) {
        this.func = func;
    }

    public T getMaxValueInLevel(int level) {
        if (cache.containsKey(level)) {
            return cache.get(level);
        }

        T maxValueForLevel = func.getValue(level);
        cache.put(level, maxValueForLevel);
        return maxValueForLevel;
    }

    // cache
    // 3 = 1000
    // 4 = 5000
    // 5 = 10_000
    public int getLevel(T value, int currentLevel) { // GET 10_000
        if (currentLevel >= 0) {
            if (getMaxValueInLevel(currentLevel + 1).compareTo(value) >= 0) {
                return currentLevel;
            }
            return currentLevel + 1;
        }
        return 0;
    }

    public void resetCache() {
        cache.clear();
    }

    @FunctionalInterface

    public interface Func<T> {

        T getValue(int level);
    }
}
