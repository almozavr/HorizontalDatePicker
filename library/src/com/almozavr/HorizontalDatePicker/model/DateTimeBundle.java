package com.almozavr.HorizontalDatePicker.model;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
* Copyright /(c/) 2013 Quotient Solutions. All rights reserved.
*/
public class DateTimeBundle {

    SparseArray<SparseArray<List<Integer>>> dateBundle = new SparseArray<SparseArray<List<Integer>>>();

    public DateTimeBundle(long startDate, long endDate) {
        addDateTimeRange(startDate, endDate);
    }

    public void addDateTimeRange(long startDate, long endDate) {
        if (startDate > endDate)
            throw new IllegalArgumentException("You end before you start, no good");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startDate);
        calendar.clear(Calendar.DAY_OF_MONTH);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);

        do {
            addDateTime(calendar.getTimeInMillis());
            calendar.roll(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.getTimeInMillis() <= endDate);
    }

    public void addDateTime(long... dateInMillis) {
        Calendar calendar = Calendar.getInstance();

        for (long millis : dateInMillis) {
            calendar.setTimeInMillis(millis);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            addDateTime(year, month, day);
        }
    }

    public void addDateTime(int year, int month, int day) {
        if (month < 1 || month > 12 || day < 1 || day > 31)
            throw new IllegalArgumentException("Incorrect month/date");

        SparseArray<List<Integer>> bundledMonthes = dateBundle.get(year);
        if (bundledMonthes == null) {
            bundledMonthes = new SparseArray<List<Integer>>();
            dateBundle.append(year, bundledMonthes);
        }

        List<Integer> bundledDays = bundledMonthes.get(month);
        if (bundledDays == null) {
            bundledDays = new ArrayList<Integer>();
            bundledMonthes.append(month, bundledDays);
        }

        if (!bundledDays.contains(day)) {
            bundledDays.add(day);
        }
    }

    public void clear() {
        dateBundle.clear();
    }

}
