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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    BudgetNotificationManager _bnm;
    BudgetDatabase _db;
    ArrayList<Budget> budgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _db = new BudgetDatabase(this);

        if (_db.getBudgets().isEmpty()) {
            _db.addTestBudgets();
        }

        _bnm = new BudgetNotificationManager(this, _db);

        try {
            ListView budget_list = findViewById(R.id.budget_list);
            budget_list.setOnItemClickListener(this);
            this.budgets = _db.getBudgets();

            MainBudgetItemAdapter adapter = new MainBudgetItemAdapter(this, budgets);
            budget_list.setAdapter(adapter);
        } catch (Exception ex) {
            int i = 0;
        }
    }

    public void Notify_Clicked(View v) {
        _bnm.showNotificationForCategory(1, "Tesco Extra");
        _bnm.showNotificationForCategory(2, "Next");
        _bnm.showNotificationForCategory(3, "Farringdon Railway Station");
    }

    public void editBudget(View v) {
        startActivity(new Intent(this, EditActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Budget chosen = this.budgets.get(position);

        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("budgetId", chosen.getId());
        startActivity(intent);
    }
}
