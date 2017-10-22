package com.geobudget.geobudget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joel on 22/10/17.
 */

public class CategoryPaymentItemAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private List<Payment> payments;

    public CategoryPaymentItemAdapter(Context context, List<Payment> paymnets) {
        super(context, -1, paymnets);
        this.context = context;
        this.payments = paymnets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.category_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textView6);
        textView.setText(String.format("£%.2f", payments.get(position).getExpenditure()));

        TextView totalBudget = (TextView) rowView.findViewById(R.id.textView5);
        totalBudget.setText(payments.get(position).getDate().toString());
        return rowView;
    }
}
