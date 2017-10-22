package com.geobudget.geobudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        ArrayList<Payment> payments = db.getPaymentsForBudget(budgetId);


    }
}
