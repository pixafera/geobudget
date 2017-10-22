package com.geobudget.geobudget;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * Created by nicholas on 21/10/2017.
 */

public final class BudgetNotificationManager {
    private final static String NCHANNEL_BUDGET_REMINDERS = "budget_reminders";

    private Context _context;
    private NotificationManager _notificationManager;
    private BudgetDatabase _db;

    public BudgetNotificationManager(Context context, BudgetDatabase db) {
        _context = context;
        _notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        _db = db;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(NCHANNEL_BUDGET_REMINDERS,
                    "Budget Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            _notificationManager.createNotificationChannel(nc);
        }
    }

    private void createSummaryNotification() {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(_context,
                        NCHANNEL_BUDGET_REMINDERS)
                .setContentTitle("Summary")
                .setContentText("A summary")
                .setSmallIcon(R.drawable.budget_reminder)
                .setGroup("Budget Reminders")
                .setGroupSummary(true);

        Intent resultIntent = new Intent(_context, MainActivity.class);

        TaskStackBuilder tsb = TaskStackBuilder.create(_context);
        tsb.addParentStack(MainActivity.class);
        tsb.addNextIntent(resultIntent);
        PendingIntent pi = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);

        try {
            _notificationManager.notify(0, mBuilder.build());
        } catch (Exception ex) {
            int i = 0;
        }
    }

    public void showNotificationForCategory(String category, String storeName) {
        Budget b = _db.getBudget(category);
        if (b == null) {
            return;
        }

        createSummaryNotification();
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(_context,
                        NCHANNEL_BUDGET_REMINDERS)
                        .setContentTitle(storeName)
                        .setContentText(String.format("You have £%.2f left to spend on %s this month", b.getAllowance(), b.getCategory()))
                        .setSmallIcon(R.drawable.budget_reminder)
                        .setGroup("Budget Reminders")
                        .setGroupSummary(false);

        Intent resultIntent = new Intent(_context, MainActivity.class);

        TaskStackBuilder tsb = TaskStackBuilder.create(_context);
        tsb.addParentStack(MainActivity.class);
        tsb.addNextIntent(resultIntent);
        PendingIntent pi = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);

        /*NotificationCompat.InboxStyle expandedStyle = new NotificationCompat.InboxStyle();
        expandedStyle.setBigContentTitle("Big Content Title");
        expandedStyle.addLine("You have £9.77 left in your food budget");
        expandedStyle.addLine("You have £54.32 left in your entertainment budget");

        mBuilder.setStyle(expandedStyle);*/

        try {
            _notificationManager.notify(b.getId(), mBuilder.build());
        } catch (Exception ex) {
            int i = 0;
        }
    }
}
