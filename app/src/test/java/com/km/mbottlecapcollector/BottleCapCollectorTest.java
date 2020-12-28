package com.km.mbottlecapcollector;

import com.km.mbottlecapcollector.util.ScreenRatioHelper;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class BottleCapCollectorTest {
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    private WelcomeScreenActivity startActivity = new WelcomeScreenActivity();

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

    @Test
    public void testDateRange1006ReturnsTrue() throws ParseException {
        Date currentDate = formatter.parse("10:05");
        assertTrue(startActivity.isCurrentDateBetweenRange(currentDate));
    }

    @Test
    public void testDateRange1004ReturnsFalse() throws ParseException {
        Date currentDate = formatter.parse("10:04");
        assertFalse(startActivity.isCurrentDateBetweenRange(currentDate));
    }

    @Test
    public void testDateRange1500ReturnsTrue() throws ParseException {
        Date currentDate = formatter.parse("15:00");
        assertTrue(startActivity.isCurrentDateBetweenRange(currentDate));
    }

    @Test
    public void testDateRange0015ReturnsTrue() throws ParseException {
        Date currentDate = formatter.parse("00:15");
        assertTrue(startActivity.isCurrentDateBetweenRange(currentDate));
    }

    @Test
    public void testDateRange0016ReturnsFalse() throws ParseException {
        Date currentDate = formatter.parse("00:16");
        assertFalse(startActivity.isCurrentDateBetweenRange(currentDate));
    }

    @Test
    public void testDateRange0300ReturnsFalse() throws ParseException {
        Date currentDate = formatter.parse("03:00");
        assertFalse(startActivity.isCurrentDateBetweenRange(currentDate));
    }
}