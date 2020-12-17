package com.km.mbottlecapcollector;

import com.km.mbottlecapcollector.util.ScreenRatioHelper;

import org.junit.Test;

import static org.junit.Assert.*;

public class BottleCapCollectorTest {
    @Test
    public void testCompareDoublesTwoDecimalPlacesReturnsTrue() {
        double x = 1.777777777777778;
        double y = 1.772511848341232;
        assertTrue(ScreenRatioHelper.compareDouble(x, y));
    }

    @Test
    public void testCompareDoublesTwoDecimalPlacesReturnsFalse() {
        double x = 1.777777777777778;
        double y = 1.762511848341232;
        assertFalse(ScreenRatioHelper.compareDouble(x, y));
    }

    @Test
    public void testCompareDoublesOneDecimalPlaceTwoDecimalPlacesReturnsTrue() {
        double x = 1.7;
        double y = 1.7;
        assertTrue(ScreenRatioHelper.compareDouble(x, y));
    }
}