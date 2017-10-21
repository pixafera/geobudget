package com.geobudget.geobudget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BudgetDatabase {

    // --- DATABASE SCHEMA ---
    private class BudgetDatabaseContract {

        private BudgetDatabaseContract() {}

        public class Budget implements BaseColumns {
            public static final String TABLE_NAME = "budget";
            public static final String CATEGORY = "category";
            public static final String ALLOWANCE = "allowance";
        }
    }
    // --- ---

    // --- DATABASE INTERACTION HELPER ---
    private class BudgetDatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Geobudget.db";
        private final Float ALLOWANCE_PRECISION = (float) 24;
        private final String CREATE_TABLE =
                "CREATE TABLE " + BudgetDatabaseContract.Budget.TABLE_NAME + "("
                        + BudgetDatabaseContract.Budget._ID + " INTEGER PRIMARY KEY,"
                        + BudgetDatabaseContract.Budget.CATEGORY + " TEXT,"
                        + BudgetDatabaseContract.Budget.ALLOWANCE + " FLOAT(" + ALLOWANCE_PRECISION.toString() + ")"
                        + ")";

        private static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + BudgetDatabaseContract.Budget.TABLE_NAME;

        public BudgetDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
        }
    }
    // --- ---

    private BudgetDatabaseHelper helper;

    public BudgetDatabase(Context context) {
        this.helper = new BudgetDatabaseHelper(context);
    }

    public long insert(String tableName, ArrayList values) {
        // stub
        return 0;
    }

    public ArrayList<String> query(String tableName, HashMap<String, Object> conditions) {
        // stub
        return new ArrayList<>();
    }

    public void delete(String tableName, HashMap<String, Object> conditions) {
        // stub
    }

    public int update(String tableName, HashMap<String, Object> changedValues) {
        // stub
        return 0;
    }
}
