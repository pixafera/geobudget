package com.geobudget.geobudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nicholas on 21/10/2017.
 */

public class MainBudgetItemAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MainBudgetItemAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_budget_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.budgetName);
        textView.setText(values[position]);
        // change the icon for Windows and iPhone
        String s = values[position];

        return rowView;
    }
}
