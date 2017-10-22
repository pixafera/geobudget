package com.geobudget.geobudget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CategoryActivity extends AppCompatActivity {
    BudgetDatabase db = new BudgetDatabase(this);
    ArrayList<Payment> payments;
    int budgetId;
    boolean submitting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        this.budgetId = intent.getIntExtra("budgetId", -1);

        // No budgetId? NO ACCESS!
        if (budgetId == -1) {
            startActivity(new Intent(this, MainActivity.class));
        }

        Budget budget = db.getBudget(budgetId);

        TextView heading = findViewById(R.id.textView3);
        String category = budget.getCategory();
        heading.setText(category);

        ListView payment_list = findViewById(R.id.expandableListView);
        this.payments = db.getPaymentsForBudget(budgetId);
        payment_list.setAdapter(new CategoryPaymentItemAdapter(this, payments));

        TextView remaining_balance = findViewById(R.id.editText3);
        float remaining = budget.getAllowance() - budget.getTotalExpenditure();
        remaining_balance.setText("Remaining:" + String.format("£%.2f", remaining));
    }

    public void determineButtonFunction(View v) {
        if (!submitting) {
            addExpense(v);
        } else {
            submitExpense(v);
        }
    }

    public void addExpense(View v) {
        EditText newDate = findViewById(R.id.editText4);
        EditText newExpense = findViewById(R.id.editText5);
        Button add = findViewById(R.id.button2);

        newDate.setVisibility(View.VISIBLE);
        newExpense.setVisibility(View.VISIBLE);
        add.setText((CharSequence) "Submit");

        newDate.requestFocus();
        submitting = true;
    }

    public void submitExpense(View v) {
        EditText submittedDate = findViewById(R.id.editText4);
        EditText submittedExpense = findViewById(R.id.editText5);
        Button submit = findViewById(R.id.button2);

        if (submittedDate.getText().length() > 0 && submittedExpense.getText().length() > 0) {
            db.addPayment(
                    new Payment(
                            0, // any id is fine
                            Float.parseFloat(submittedExpense.getText().toString()),
                            submittedDate.getText().toString(),
                            this.budgetId
                    )
            );
        }

        ListView payment_list = findViewById(R.id.expandableListView);
        this.payments = db.getPaymentsForBudget(budgetId);
        payment_list.setAdapter(new CategoryPaymentItemAdapter(this, payments));

        submittedDate.setVisibility(View.GONE);
        submittedExpense.setVisibility(View.GONE);
        submit.setText((CharSequence) "Add");

        submitting = false;
    }
}
