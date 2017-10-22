package com.geobudget.geobudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        int budgetId = intent.getIntExtra("budgetId", -1);

        // No budgetId? NO ACCESS!
        if (budgetId == -1) {
            startActivity(new Intent(this, MainActivity.class));
        }

        BudgetDatabase db = new BudgetDatabase(this);
        ArrayList<Budget> b = db.getBudgets();
        ArrayList<Payment> payments = db.getPaymentsForBudget(budgetId);

        ListView payment_list = findViewById(R.id.expandableListView);
        payment_list.setAdapter(new CategoryPaymentItemAdapter(this, payments));
    }
}
