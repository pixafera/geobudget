package com.geobudget.geobudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by joel on 21/10/17.
 */

public class EditActivity extends AppCompatActivity {
    private ArrayList<Budget> _budgets;
    private BudgetDatabase _db = new BudgetDatabase(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        _budgets = _db.getBudgets(true);

        calculateSavings();

        BudgetChangeListener l = new BudgetChangeListener() {
            @Override
            public void onAllowanceChanged(Budget b) {
                calculateSavings();
            }
        };

        for (Budget b: _budgets) {
            b.addChangeListener(l);
        }

        ListView editBudget = (ListView) findViewById(R.id.edit_budget);
        EditBudgetItemAdapter editBudgetAdapter = new EditBudgetItemAdapter(this, _budgets);
        editBudget.setAdapter(editBudgetAdapter);
    }

    private void calculateSavings() {
        EditText savings = (EditText) findViewById(R.id.savings);
        float totalSavings = 0;
        for(Budget b: _budgets) {
            if (b.getIsIncome()) {
                totalSavings += b.getAllowance();
            } else {
                totalSavings -= b.getAllowance();
            }
        }

        savings.setText(String.format("%.2f", totalSavings));
    }

    public void saveOnClick(View v) {
        for (Budget b: _budgets) {
            _db.updateBudget(b.getId(), b);
        }

        finish();
    }
}
