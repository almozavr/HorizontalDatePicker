# HorizontalDatePicker
Simple but yet flexible android date picker view based on horizontal list views.


## What does it look like?
![Screenshot 1](https://api.monosnap.com/image/download?id=c6cRPtBLdIcrZdhFWHtT1QbeK "Ugly-styled screenshot")


## How to use? 

* Clone git repo, init and update submodules:

```
git clone git@github.com:almozavr/HorizontalDatePicker.git HorizontalDatePicker
cd HorizontalDatePicker
git submodule update --init --recursive
```
* add as ```library project``` (with contrib. HorizontalVariableListView dependancy)
* add to ```layout```
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent">

    <com.almozavr.HorizontalDatePicker.widget.HorizontalDatePicker
        xmlns:picker="http://schemas.android.com/apk/res-auto"
        android:id="@+id/picker"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        picker:pickers="year|month|day"
        picker:abbrs="month|day"
        picker:values="day"
        picker:year_picker_background="@android:color/white"
        />

</FrameLayout>
```
* use it 
```java
public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
```


## Developed By

* Aleksey Malevaniy - <almozavr@gmail.com>


## License

    Copyright 2013 Aleksey Malevaniy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
