package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StyleFlagsTest {

    @Test
    void defaultConstructorHasAllFlagsFalse() {
        StyleFlags flags = new StyleFlags();
        assertFalse(flags.getBold());
        assertFalse(flags.getItalic());
        assertFalse(flags.getUnderline());
    }

    @Test
    void parameterizedConstructorSetsFlags() {
        StyleFlags flags = new StyleFlags(true, false, true);
        assertTrue(flags.getBold());
        assertFalse(flags.getItalic());
        assertTrue(flags.getUnderline());
    }

    @Test
    void copyConstructorCopiesValuesIndependently() {
        StyleFlags original = new StyleFlags(true, true, false);
        StyleFlags copy = new StyleFlags(original);
        original.reset();
        assertTrue(copy.getBold());
        assertTrue(copy.getItalic());
        assertFalse(copy.getUnderline());
    }

    @Test
    void resetClearsAllFlags() {
        StyleFlags flags = new StyleFlags(true, true, true);
        flags.reset();
        assertFalse(flags.getBold());
        assertFalse(flags.getItalic());
        assertFalse(flags.getUnderline());
    }
}