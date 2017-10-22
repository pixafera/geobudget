package com.geobudget.geobudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

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
        Budget budget = db.getBudget(budgetId);

        TextView heading = findViewById(R.id.textView3);
        String category = budget.getCategory();
        heading.setText(category);

        ListView payment_list = findViewById(R.id.expandableListView);
        ArrayList<Payment> payments = db.getPaymentsForBudget(budgetId);
        payment_list.setAdapter(new CategoryPaymentItemAdapter(this, payments));

        TextView remaining_balance = findViewById(R.id.editText3);
        float remaining = budget.getAllowance() - budget.getTotalExpenditure();
        remaining_balance.setText("Remaining:" + String.format("£%.2f", remaining));
    }
}
