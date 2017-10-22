package com.geobudget.geobudget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by joel on 21/10/17.
 */

public class EditActivity extends AppCompatActivity {
    private ArrayList<Budget> budgets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_budget_item);
    }

    public void updateBudget(View v) {
        ListView editableBudgetList = findViewById(R.id.budget_list); // TODO: Change to correct view
        
    }
}
