package com.almozavr.HorizontalDatePicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import com.almozavr.HorizontalDatePicker.R;
import com.almozavr.HorizontalDatePicker.adapter.DayRangeAdapter;
import com.almozavr.HorizontalDatePicker.adapter.MonthRangeAdapter;
import com.almozavr.HorizontalDatePicker.adapter.YearRangeAdapter;
import com.almozavr.HorizontalDatePicker.util.WidgetUtil;
import it.sephiroth.android.library.widget.HorizontalVariableListView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class HorizontalDatePicker extends LinearLayout implements DatePicker {

    /* Views */
    HorizontalVariableListView yearPicker;
    HorizontalVariableListView monthPicker;
    HorizontalVariableListView dayPicker;

    YearRangeAdapter yearAdapter;
    MonthRangeAdapter monthAdapter;
    DayRangeAdapter dayAdapter;

    /* Attrs */
    int yearItemRes;
    int monthItemRes;
    int dayItemRes;

    Drawable yearPickerBackground;
    Drawable monthPickerBackground;
    Drawable dayPickerBackground;

    private Set<Picker> pickers = new HashSet<Picker>(3);
    private int monthStart;
    private int monthEnd;
    private int dayStart;
    private int dayEnd;
    private int startYear;
    private int endYear;

    enum Picker implements WidgetUtil.AttrValue {

        YEAR(1), MONTH(2), DAY(4);

        private int index;

        private Picker(int index) {
            this.index = index;
        }

        @Override
        public int getAttrIndex() {
            return index;
        }
    }

    private Set<Abbreviation> abbreviations = new HashSet<Abbreviation>(2);

    enum Abbreviation implements WidgetUtil.AttrValue {

        MONTH(1), DAY(2);

        private int index;

        private Abbreviation(int index) {
            this.index = index;
        }

        @Override
        public int getAttrIndex() {
            return index;
        }
    }

    private Set<Value> values = new HashSet<Value>(2);

    enum Value implements WidgetUtil.AttrValue {

        MONTH(1), DAY(2);

        private int index;

        private Value(int index) {
            this.index = index;
        }

        @Override
        public int getAttrIndex() {
            return index;
        }
    }

    /* Date holders */
    private long rangeStartDate;
    private long rangeEndDate;
    private long selectedDate;
    // private DateTimeBundle highlightedDateBundle;
    private GregorianCalendar sharedCalendar = new GregorianCalendar();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public HorizontalDatePicker(Context context) {
        super(context);
    }

    public HorizontalDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalDatePicker);

        // pickers to show
        pickers.addAll(WidgetUtil.readAttrValues(typedArray,
                R.styleable.HorizontalDatePicker_pickers,
                Picker.values()));
        if (pickers.isEmpty()) {
            pickers.add(Picker.YEAR);
            pickers.add(Picker.MONTH);
            pickers.add(Picker.DAY);
        }
        // abbreviations and values on month/day picker
        abbreviations.addAll(WidgetUtil.readAttrValues(typedArray,
                R.styleable.HorizontalDatePicker_abbrs,
                Abbreviation.values()));
        values.addAll(WidgetUtil.readAttrValues(typedArray,
                R.styleable.HorizontalDatePicker_values,
                Value.values()));
        // pickers items' layouts
        yearItemRes = typedArray.getResourceId(
                R.styleable.HorizontalDatePicker_year_item_layout,
                R.layout.horizontal_date_picker_value_item);
        monthItemRes = typedArray.getResourceId(
                R.styleable.HorizontalDatePicker_month_item_layout,
                R.layout.horizontal_date_picker_abbr_item);
        dayItemRes = typedArray.getResourceId(
                R.styleable.HorizontalDatePicker_day_item_layout,
                R.layout.horizontal_date_picker_abbr_item);
        // pickers' backgrounds
        yearPickerBackground = typedArray.getDrawable(R.styleable.HorizontalDatePicker_year_picker_background);
        monthPickerBackground = typedArray.getDrawable(R.styleable.HorizontalDatePicker_month_picker_background);
        dayPickerBackground = typedArray.getDrawable(R.styleable.HorizontalDatePicker_day_picker_background);

        typedArray.recycle();
    }

    private void initView(Context context) {
        inflate(context, R.layout.horizontal_date_picker, this);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAdapters();
        initPickers();
    }

    private void initAdapters() {
        yearAdapter = new YearRangeAdapter(getContext(), yearItemRes);
        monthAdapter = new MonthRangeAdapter(getContext(),
                monthItemRes,
                abbreviations.contains(Abbreviation.MONTH),
                values.contains(Value.MONTH));
        dayAdapter = new DayRangeAdapter(getContext(),
                dayItemRes,
                abbreviations.contains(Abbreviation.DAY),
                values.contains(Value.DAY));
    }

    private void initPickers() {
        yearPicker = (HorizontalVariableListView) findViewById(R.id.horizontal_date_picker_year);
        yearPicker.setChoiceMode(HorizontalVariableListView.CHOICE_MODE_SINGLE);
        yearPicker.setVisibility(pickers.contains(Picker.YEAR) ? VISIBLE : GONE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            yearPicker.setBackgroundDrawable(yearPickerBackground);
        else
            yearPicker.setBackground(yearPickerBackground);

        monthPicker = (HorizontalVariableListView) findViewById(R.id.horizontal_date_picker_month);
        monthPicker.setChoiceMode(HorizontalVariableListView.CHOICE_MODE_SINGLE);
        monthPicker.setVisibility(pickers.contains(Picker.MONTH) ? VISIBLE : GONE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            monthPicker.setBackgroundDrawable(monthPickerBackground);
        else
            monthPicker.setBackground(monthPickerBackground);

        dayPicker = (HorizontalVariableListView) findViewById(R.id.horizontal_date_picker_day);
        dayPicker.setChoiceMode(HorizontalVariableListView.CHOICE_MODE_SINGLE);
        dayPicker.setVisibility(pickers.contains(Picker.DAY) ? VISIBLE : GONE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            dayPicker.setBackgroundDrawable(dayPickerBackground);
        else
            dayPicker.setBackground(dayPickerBackground);

        yearPicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedCalendar.setTimeInMillis(rangeStartDate);
                int year = sharedCalendar.get(Calendar.YEAR) + position;
                setMonthRagne(year);
            }
        });

        monthPicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedCalendar.setTimeInMillis(selectedDate);
                int year = sharedCalendar.get(Calendar.YEAR);
                int month = monthStart + position;
                setDayRange(year, month);

            }
        });

        dayPicker.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sharedCalendar.setTimeInMillis(selectedDate);
                sharedCalendar.set(Calendar.DAY_OF_MONTH, dayStart + position);
                selectedDate = sharedCalendar.getTimeInMillis();

                if (dateChangeListener != null)
                    dateChangeListener.onDateChanged(selectedDate);
            }
        });
    }

    // /////////////////////////////////////////////////////////////////////////
    // Date related methods
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void setDateRange(long startDate, long endDate) {
        if (startDate > endDate)
            throw new IllegalArgumentException("You finish before you start, no good");

        rangeStartDate = startDate;
        rangeEndDate = endDate;
        selectedDate = startDate;

        yearPicker.setAdapter(yearAdapter);
        monthPicker.setAdapter(monthAdapter);
        dayPicker.setAdapter(dayAdapter);

        setYearRange(startDate, endDate);

    }

    private void setYearRange(long startDate, long endDate) {
        sharedCalendar.setTimeInMillis(rangeStartDate);
        startYear = sharedCalendar.get(Calendar.YEAR);
        sharedCalendar.setTimeInMillis(rangeEndDate);
        endYear = sharedCalendar.get(Calendar.YEAR);

        yearAdapter.setStartRange(startYear);
        yearAdapter.setEndRange(endYear);
        yearAdapter.notifyDataSetChanged();

        yearPicker.setItemChecked(0, true);

        setMonthRagne(startYear);
    }

    private int setMonthRagne(int year) {
        monthStart = Calendar.JANUARY;
        monthEnd = Calendar.DECEMBER;

        sharedCalendar.setTimeInMillis(rangeStartDate);
        if (sharedCalendar.get(Calendar.YEAR) == year) {
            monthStart = sharedCalendar.get(Calendar.MONTH);
        }

        sharedCalendar.set(Calendar.YEAR, year);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(rangeEndDate);
        if (year == calendar.get(Calendar.YEAR)) {
            monthEnd = calendar.get(Calendar.MONTH);
        }

        monthAdapter.setStartRange(monthStart);
        monthAdapter.setEndRange(monthEnd);
        monthAdapter.notifyDataSetChanged();

        monthPicker.setItemChecked(0, true);

        setDayRange(year, monthStart);

        return monthStart;

    }

    private int setDayRange(int year, int month) {
        dayStart = 0;
        dayEnd = 0;

        sharedCalendar.setTimeInMillis(rangeStartDate);
        if (sharedCalendar.get(Calendar.MONTH) == month) {
            dayStart = sharedCalendar.get(Calendar.DAY_OF_MONTH);
        } else {
            dayStart = 1;
        }

        sharedCalendar.set(Calendar.YEAR, year);
        sharedCalendar.set(Calendar.MONTH, month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(rangeEndDate);

        if (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH)) {
            dayEnd = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            dayEnd = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        dayAdapter.setYear(year);
        dayAdapter.setMonth(month);
        dayAdapter.setStartRange(dayStart);
        dayAdapter.setEndRange(dayEnd);
        dayAdapter.notifyDataSetChanged();

        dayPicker.setItemChecked(0, true);

        sharedCalendar.set(Calendar.DAY_OF_MONTH, dayStart);
        selectedDate = sharedCalendar.getTimeInMillis();

        if (dateChangeListener != null && enableDateChangeListener)
            dateChangeListener.onDateChanged(selectedDate);

        return dayStart;
    }

    @Override
    public long getSelectedDate() {
        return selectedDate;
    }

    @Override
    public void setSelectedDate(long dateInMillis) {
        if (dateInMillis < rangeStartDate || dateInMillis > rangeEndDate)
            return; // out of range, do nothing

        sharedCalendar.setTimeInMillis(dateInMillis);
        int year = sharedCalendar.get(Calendar.YEAR);
        int month = sharedCalendar.get(Calendar.MONTH);
        int day = sharedCalendar.get(Calendar.DAY_OF_MONTH);

        yearPicker.setItemChecked(year - startYear, true);
        int startMonth = setMonthRagne(year);
        monthPicker.setItemChecked(month - startMonth, true);
        int startDay = setDayRange(year, month);
        dayPicker.setItemChecked(day - startDay, true);

    }

    private DateChangeListener dateChangeListener;
    private boolean enableDateChangeListener = true;

    public void setDateChangeListener(DateChangeListener listener) {
        dateChangeListener = listener;
    }

    // /////////////////////////////////////////////////////////////////////////
    // LifeCycle
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.rangeStartDate = this.rangeStartDate;
        ss.rangeEndDate = this.rangeEndDate;
        ss.selectedDate = this.selectedDate;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.rangeStartDate = ss.rangeStartDate;
        this.rangeEndDate = ss.rangeEndDate;

        this.enableDateChangeListener = false;
        setDateRange(rangeStartDate, rangeEndDate);
        this.selectedDate = ss.selectedDate;
        setSelectedDate(ss.selectedDate);
        this.enableDateChangeListener = true;
    }

    static class SavedState extends BaseSavedState {

        private long rangeStartDate;
        private long rangeEndDate;
        private long selectedDate;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.rangeStartDate = in.readLong();
            this.rangeEndDate = in.readLong();
            this.selectedDate = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(rangeStartDate);
            out.writeLong(rangeEndDate);
            out.writeLong(selectedDate);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
