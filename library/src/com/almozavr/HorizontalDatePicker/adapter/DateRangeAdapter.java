package com.almozavr.HorizontalDatePicker.adapter;

import android.widget.TextView;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.content.Context;

import com.almozavr.HorizontalDatePicker.R;

public abstract class DateRangeAdapter extends BaseAdapter {

    private final Context context;
    private boolean showValue;
    private boolean showAbbr;
    private final LayoutInflater inflater;
    private final int itemLayout;
    private int startRange;
    private int endRange;

    public DateRangeAdapter(Context context, int itemLayout, boolean showAbbr, boolean showValue) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemLayout = itemLayout;
        this.showAbbr = showAbbr;
        this.showValue = showValue;
    }

    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    @Override
    public int getCount() {
        return endRange - startRange + 1;
    }

    @Override
    public Integer getItem(int i) {
        return startRange + i;
    }

    @Override
    public long getItemId(int i) {
        return startRange + i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView != null) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(itemLayout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        int value = getItem(position);

        if (holder.abbr != null) {
            if (showAbbr) {
                String abbr = buildAbbrString(position);
                holder.abbr.setText(abbr);
                holder.abbr.setVisibility(View.VISIBLE);
            } else {
                holder.abbr.setVisibility(View.GONE);
            }
        }

        if (holder.value != null) {
            if (showValue) {
                String formattedValue = String.format(context.getString(R.string.date_item_value_mask), value);
                holder.value.setText(formattedValue);
                holder.value.setVisibility(View.VISIBLE);
            } else {
                holder.value.setVisibility(View.GONE);
            }
        }

        return view;
    }

    protected String buildAbbrString(int position) {
        return null;
    }

    class ViewHolder {

        TextView abbr;
        TextView value;

        ViewHolder(View rootView) {
            abbr = (TextView) rootView.findViewById(R.id.horizontal_date_picker_item_abbr);
            value = (TextView) rootView.findViewById(R.id.horizontal_date_picker_item_value);
        }
    }

}
