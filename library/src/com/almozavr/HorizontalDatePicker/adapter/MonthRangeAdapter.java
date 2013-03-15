package com.almozavr.HorizontalDatePicker.adapter;

import android.content.Context;

import java.text.DateFormatSymbols;

public class MonthRangeAdapter extends DateRangeAdapter {

    public MonthRangeAdapter(Context context, int itemLayout, boolean showAbbr, boolean showValue) {
        super(context, itemLayout, showAbbr, showValue);
    }

    @Override
    public Integer getItem(int i) {
        return super.getItem(i) + 1;
    }

    @Override
    protected String buildAbbrString(int position) {
        Integer item = getItem(position);
        return new DateFormatSymbols().getShortMonths()[item - 1];
    }

}
