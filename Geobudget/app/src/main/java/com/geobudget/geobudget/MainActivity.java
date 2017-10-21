package com.geobudget.geobudget;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    BudgetNotificationManager _bnm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _bnm = new BudgetNotificationManager(this);

        try {
            ListView budget_list = findViewById(R.id.budget_list);
            String[] values = new String[]{"Food", "Fuel", "Entertainment"};

            MainBudgetItemAdapter adapter = new MainBudgetItemAdapter(this, values);
            budget_list.setAdapter(adapter);
        } catch (Exception ex) {
            int i = 0;
        }
    }

    public void Notify_Clicked(View v) {
        _bnm.showNotificationForCategory(1);
        _bnm.showNotificationForCategory(2);
        _bnm.showNotificationForCategory(3);
    }
}
