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
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    BudgetNotificationManager _bnm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _bnm = new BudgetNotificationManager(this);
    }

    public void Notify_Clicked(View v) {
        _bnm.showNotificationForCategory(1);
        _bnm.showNotificationForCategory(2);
        _bnm.showNotificationForCategory(3);
    }
}
