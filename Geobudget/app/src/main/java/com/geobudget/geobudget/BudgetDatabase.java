package com.geobudget.geobudget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by joel on 21/10/17.
 */

public class BudgetDatabase {

    // --- DATABASE SCHEMA ---
    private class BudgetDatabaseContract {

        private BudgetDatabaseContract() {}

        public class Budget implements BaseColumns {
            public static final String TABLE_NAME = "budget";
            public static final String CATEGORY = "category";
            public static final String ALLOWANCE = "allowance";
        }

        public class Transaction implements BaseColumns {
            public static final String TABLE_NAME = "transaction";
            public static final String EXPENDITURE = "expenditure";
            public static final String DATE = "date";
            public static final String BUDGET = "budget";
        }
    }
    // --- ---

    // --- DATABASE INTERACTION HELPER ---
    private class BudgetDatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Geobudget.db";
        private final Float FLOAT_PRECISION = (float) 24;
        private final String CREATE_BUDGET_TABLE =
                "CREATE TABLE " + BudgetDatabaseContract.Budget.TABLE_NAME + "("
                + BudgetDatabaseContract.Budget._ID + " INTEGER PRIMARY KEY,"
                + BudgetDatabaseContract.Budget.CATEGORY + " TEXT,"
                + BudgetDatabaseContract.Budget.ALLOWANCE + " FLOAT(" + FLOAT_PRECISION.toString() + ")"
                + ")";
        private final String CREATE_TRANSACTION_TABLE =
                "CREATE TABLE " + BudgetDatabaseContract.Transaction.TABLE_NAME + "("
                + BudgetDatabaseContract.Transaction._ID + " INTEGER PRIMARY KEY,"
                + BudgetDatabaseContract.Transaction.EXPENDITURE + " FLOAT(" + FLOAT_PRECISION.toString() + ")"
                + BudgetDatabaseContract.Transaction.DATE + " DATE"  // Format YYYY-MM-DD
                + BudgetDatabaseContract.Transaction.BUDGET + " FOREIGN KEY REFERENCES "
                        + BudgetDatabaseContract.Budget.TABLE_NAME + "(" + BudgetDatabaseContract.Budget._ID + ")"
                + ")";

        private static final String DROP_BUDGET_TABLE =
                "DROP TABLE IF EXISTS " + BudgetDatabaseContract.Budget.TABLE_NAME;

        private static final String DROP_TRANSACTION_TABLE =
                "DROP TABLE IF EXISTS " + BudgetDatabaseContract.Transaction.TABLE_NAME;

        public BudgetDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_BUDGET_TABLE);
            db.execSQL(CREATE_TRANSACTION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TRANSACTION_TABLE);
            db.execSQL(DROP_BUDGET_TABLE);
            onCreate(db);
        }
    }
    // --- ---

    private BudgetDatabaseHelper helper;

    public BudgetDatabase(Context context) {
        this.helper = new BudgetDatabaseHelper(context);
    }

    public long insert(String tableName, DatabaseEntry entry) {
        // stub
        return 0;
    }

    public DatabaseEntry query(String tableName, HashMap<String, Object> conditions) {
        // stub
        return new DatabaseEntry();
    }

    public void delete(String tableName, HashMap<String, Object> conditions) {
        // stub
    }

    public int update(String tableName, HashMap<String, Object> changedValues) {
        // stub
        return 0;
    }
}
