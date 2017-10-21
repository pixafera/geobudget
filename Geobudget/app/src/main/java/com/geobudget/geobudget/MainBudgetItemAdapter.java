package com.geobudget.geobudget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
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

        ImageView centreCoin = (ImageView) rowView.findViewById(R.id.centrecoin);
        Resources res = context.getResources();
        Bitmap oneCentreCoin = BitmapFactory.decodeResource(res, R.drawable.coin_bar_middle);
        Bitmap bmp = Bitmap.createBitmap(oneCentreCoin.getWidth() * (position + 1), oneCentreCoin.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bmp);
        for(int i = 0;i<(position + 1);i++) {
            cv.drawBitmap(oneCentreCoin, i * oneCentreCoin.getWidth(), 0, null);
        }

        ConstraintLayout.LayoutParams centreCoinLayout = (ConstraintLayout.LayoutParams) centreCoin.getLayoutParams();
        float centreCoinWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, res.getDisplayMetrics()) * (position + 1);
        centreCoinLayout.width = (int)centreCoinWidth;
        centreCoin.setLayoutParams(centreCoinLayout);
        centreCoin.setImageBitmap(bmp);
        return rowView;
    }
}
