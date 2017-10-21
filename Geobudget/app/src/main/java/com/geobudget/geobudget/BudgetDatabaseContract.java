package com.geobudget.geobudget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class BudgetDatabaseContract {
    private static final BudgetDatabaseContract ourInstance = new BudgetDatabaseContract();

    static BudgetDatabaseContract getInstance() {
        return ourInstance;
    }

    private BudgetDatabaseContract() {}

    // --- DATABASE SCHEMA ---
    public static class Budget implements BaseColumns {
        public static final String TABLE_NAME = "budget";
        public static final String CATEGORY = "category";
        public static final String ALLOWANCE = "allowance";
    }

    private static final Float ALLOWANCE_PRECISION = (float) 24;

    private static final String CREATE_TABLE =
            "CREATE TABLE " + Budget.TABLE_NAME + "("
            + Budget._ID + " INTEGER PRIMARY KEY,"
            + Budget.CATEGORY + " TEXT,"
            + Budget.ALLOWANCE + " FLOAT(" + ALLOWANCE_PRECISION.toString() + ")"
            +")";

    private static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + Budget.TABLE_NAME;

    public class BudgetDatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Geobudget.db";

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
}
