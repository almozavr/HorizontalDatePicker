package com.almozavr.HorizontalDatePicker.adapter;

import android.content.Context;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayRangeAdapter extends DateRangeAdapter {

    private int year;
    private int month;

    public DayRangeAdapter(Context context, int itemLayout, boolean showAbbr, boolean showValue) {
        super(context, itemLayout, showAbbr, showValue);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    protected String buildAbbrString(int position) {
        Integer day = getItem(position);
        Calendar cal = new GregorianCalendar(year, month, day);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return new DateFormatSymbols().getShortWeekdays()[dayOfWeek];
    }

}
