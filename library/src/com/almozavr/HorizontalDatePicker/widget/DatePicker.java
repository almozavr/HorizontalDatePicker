package com.almozavr.HorizontalDatePicker.widget;

/**
 * Copyright /(c/) 2013 Quotient Solutions. All rights reserved.
 */
public interface DatePicker {

    public void setDateRange(long rangeStart, long rangeEnd);

    public long getSelectedDate();

    public void setSelectedDate(long dateInMillis);

    public static interface DateChangeListener {

        public void onDateChanged(long selectedDate);
    }
}
