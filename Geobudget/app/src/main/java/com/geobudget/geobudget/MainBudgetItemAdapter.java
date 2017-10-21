package com.geobudget.geobudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nicholas on 21/10/2017.
 */

public class MainBudgetItemAdapter extends ArrayAdapter<Budget> {
    private final Context context;
    private final ArrayList<Budget> values;

    public MainBudgetItemAdapter(Context context, ArrayList<Budget> values) {
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
        textView.setText(values.get(position).getCategory());

        TextView totalBudget = (TextView) rowView.findViewById(R.id.totalBudget);
        totalBudget.setText(String.format("£%.2f", values.get(position).getAllowance()));
        return rowView;
    }
}
