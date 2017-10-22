package com.geobudget.geobudget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nicholas on 22/10/2017.
 */

public class EditBudgetItemAdapter extends ArrayAdapter<Budget> {
    private final Context context;
    private final ArrayList<Budget> values;

    public EditBudgetItemAdapter(Context context, ArrayList<Budget> items) {
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Budget b = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.edit_budget_item, parent, false);

        final EditText allowance = (EditText) rowView.findViewById(R.id.allowance);
        allowance.setText(String.format("%.2f", b.getAllowance()));

        allowance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float allowanceValue;
                try {
                    allowanceValue = Float.parseFloat(s.toString());
                } catch (Exception ex) {
                    return;
                }

                b.setAllowance(allowanceValue);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        TextView category = (TextView) rowView.findViewById(R.id.category);
        category.setText(b.getCategory());

        return rowView;
    }
}
