package com.example.Demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.almozavr.HorizontalDatePicker.widget.DatePicker;
import com.almozavr.HorizontalDatePicker.widget.HorizontalDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Calendar startRangeCalendar = new GregorianCalendar();
        Calendar endRangeCalendar = new GregorianCalendar();
        endRangeCalendar.roll(Calendar.YEAR, 1);
        endRangeCalendar.roll(Calendar.MONTH, 2);

        HorizontalDatePicker picker = (HorizontalDatePicker) findViewById(R.id.picker);
        picker.setDateRange(startRangeCalendar.getTimeInMillis(), endRangeCalendar.getTimeInMillis());
        picker.setDateChangeListener(new DatePicker.DateChangeListener() {

            @Override
            public void onDateChanged(long selectedDate) {
                String formattedtime = new SimpleDateFormat().format(new Date(selectedDate));
                Toast.makeText(getApplicationContext(), formattedtime, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
