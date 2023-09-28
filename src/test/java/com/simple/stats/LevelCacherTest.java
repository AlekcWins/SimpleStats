package com.simple.stats;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LevelCacherTest {

    static LevelCacher<Long> getInstance() {
        LevelCacher.Func<Long> func = x -> (long) (500L * x + (500L * x) * 0.5 * x);
        LevelCacher<Long> ch = new LevelCacher<Long>(func);
        return ch;
    }

    @Test
    void zeroTest() {
        LevelCacher<Long> ch = getInstance();
        assertEquals(0, ch.getLevel(0L, 0));
    }

    @Test
    void twoTest() {
        LevelCacher<Long> ch = getInstance();
        assertEquals(1, ch.getLevel(752L, 0));
    }
}
