package com.geobudget.geobudget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
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
    private final Resources _resources;
    private final Bitmap _startCoin ;
    private final Bitmap _oneCentreCoin;
    private final Bitmap _endCoin;

    public MainBudgetItemAdapter(Context context, ArrayList<Budget> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        _resources = context.getResources();
        _startCoin = BitmapFactory.decodeResource(_resources, R.drawable.coin_bar_start);
        _oneCentreCoin = BitmapFactory.decodeResource(_resources, R.drawable.coin_bar_middle);
        _endCoin = BitmapFactory.decodeResource(_resources, R.drawable.coin_bar_end);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Budget b = values.get(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.main_budget_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.budgetName);
        textView.setText(b.getCategory());

        float allowance = b.getAllowance();

        TextView unspent = (TextView) rowView.findViewById(R.id.unspent);
        float unspentAmount = b.getAllowance() - b.getTotalExpenditure();
        unspent.setText(String.format("£%.2f", unspentAmount));

        TextView spent = (TextView) rowView.findViewById(R.id.spent);
        float spentAmount = b.getTotalExpenditure();
        spent.setText(String.format("£%.2f", spentAmount));

        float proportionSpent = spentAmount / allowance;
        float proportionUnspent = unspentAmount / allowance;

        // The screen width, minus the left nad right margins on yellow and grey coins,
        // minus the size of the start and end coin.
        float maxCoinWidth = getScreenWidth() - convertDpToPx(16 + 16 + 12 + 19 + 16);
        float oneCoinWidth = convertDpToPx(9);

        int yellowCoins;
        int grayCoins;

        if (proportionSpent == 0) {
            yellowCoins = (int)(maxCoinWidth / oneCoinWidth);
            grayCoins = -1;
        } else if (proportionUnspent == 0) {
            yellowCoins = -1;
            grayCoins = (int)(maxCoinWidth / oneCoinWidth);
        } else {
            // We will show both coins, so we need to remove another start and end coin with and
            // the margin between them.
            maxCoinWidth -= convertDpToPx(12 + 19 + 8);
            int maxCoins = (int)(maxCoinWidth / oneCoinWidth);
            yellowCoins = (int)(maxCoins * proportionUnspent);
            grayCoins = maxCoins - yellowCoins;
        }

        ImageView yellowCoin = (ImageView) rowView.findViewById(R.id.yellowCoin);
        drawCoinsToImageView(yellowCoin, yellowCoins, false);

        ImageView grayCoin = (ImageView) rowView.findViewById(R.id.grayCoin);
        drawCoinsToImageView(grayCoin, grayCoins, true);

        return rowView;
    }

    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int convertDpToPx(int dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, _resources.getDisplayMetrics());
    }

    private void drawCoinsToImageView(ImageView view, int numberOfCoins, boolean grayscale) {
        if (numberOfCoins == -1) {
            view.setVisibility(View.GONE);
        }
        Paint p = new Paint();

        if (grayscale) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            p.setColorFilter(f);
        }

        int bmpWidth = (_oneCentreCoin.getWidth() * numberOfCoins) + _startCoin.getWidth() + _endCoin.getWidth();
        Bitmap bmp = Bitmap.createBitmap(bmpWidth, _oneCentreCoin.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bmp);
        float left = 0;
        cv.drawBitmap(_startCoin, left, 0, p);
        left += _startCoin.getWidth();
        int dpWidth = (9 * numberOfCoins) + 12 + 19;
        while (numberOfCoins-- > 0) {
            cv.drawBitmap(_oneCentreCoin, left, 0, p);
            left += _oneCentreCoin.getWidth();
        }
        cv.drawBitmap(_endCoin, left, 0, p);

        ConstraintLayout.LayoutParams centreCoinLayout = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        float centreCoinWidth = convertDpToPx(dpWidth);
        centreCoinLayout.width = (int)centreCoinWidth;
        view.setLayoutParams(centreCoinLayout);
        view.setImageBitmap(bmp);
    }
}
