package com.geobudget.geobudget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;

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
                + BudgetDatabaseContract.Budget._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + BudgetDatabaseContract.Budget.CATEGORY + " TEXT,"
                + BudgetDatabaseContract.Budget.ALLOWANCE + " FLOAT(" + FLOAT_PRECISION.toString() + ")"
                + ")";
        private final String CREATE_TRANSACTION_TABLE =
                "CREATE TABLE " + BudgetDatabaseContract.Transaction.TABLE_NAME + "("
                + BudgetDatabaseContract.Transaction._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + BudgetDatabaseContract.Transaction.EXPENDITURE + " FLOAT(" + FLOAT_PRECISION.toString() + "),"
                + BudgetDatabaseContract.Transaction.DATE + " DATE,"  // Format YYYY-MM-DD
                + BudgetDatabaseContract.Transaction.BUDGET + " INTEGER, FOREIGN KEY (" + BudgetDatabaseContract.Transaction._ID + ") REFERENCES "
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

    public void addTestBudgets() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.rawQuery("INSERT INTO budget (category, allowance) VALUES ('Food', 80);", null);
        db.rawQuery("INSERT INTO budget (category, allowance) VALUES ('Entertainment', 20);",null);
        db.rawQuery("INSERT INTO budget (category, allowance) VALUES ('Fuel', 50);", null);
    }

    public Budget getBudget(int id) {
        Cursor cur = helper.getReadableDatabase().rawQuery(String.format("SELECT category, allowance, (SELECT SUM(expenditure) FROM \"transaction\" WHERE \"transaction\".budget = budget._id) FROM budget WHERE _id = %d;", id), null);

        Budget b = null;
        if (cur.moveToFirst()) {
            String category = cur.getString(0);
            float allowance = cur.getFloat(1);
            float totalExpenditure = cur.getFloat(2);
            b = new Budget(id, category, allowance, totalExpenditure);
        }

        cur.close();
        return b;
    }

    public ArrayList<Budget> getBudgets() {
        Cursor cur = helper.getReadableDatabase().rawQuery("SELECT _id, category, allowance, (SELECT SUM(expenditure) FROM \"transaction\" WHERE \"transaction\".budget = budget._id) FROM budget", null);
        ArrayList<Budget> l = new ArrayList<Budget>();

        while (cur.moveToNext()) {
            int id = cur.getInt(0);
            String category = cur.getString(1);
            float allowance = cur.getFloat(2);
            float totalExpenditure = cur.getFloat(3);
            Budget b = new Budget(id, category, allowance, totalExpenditure);
            l.add(b);
        }

        return l;
    }

    public void updateBudget(int id, Budget updatedBudget) {
        helper.getWritableDatabase().rawQuery(
                "UPDATE budget"
                + " SET category = " + updatedBudget.getCategory()
                    + ", allowance = " + updatedBudget.getAllowance()
                + " WHERE _id = " + id,
                null
        );
    }

    public void addTransaction(Transaction newTransaction) {
        helper.getWritableDatabase().rawQuery(
                "INSERT INTO transaction (expenditure, date, budget) VALUES ("
                        + newTransaction.getExpenditure()
                        + ", " + newTransaction.getDate()
                        + ", " + newTransaction.getBudget()
                + ")",
                null
        );
    }

    public ArrayList<Transaction> getTransactionsForBudget(Integer budgetId) {
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT _id, expenditure, date, budget"
                + " FROM transaction"
                + " WHERE transaction.budget = " + budgetId.toString(),
                null
        );

        ArrayList<Transaction> transactions = new ArrayList<>();
        while(cur.moveToNext()) {
            Transaction transaction = new Transaction(
                    cur.getInt(0),
                    cur.getFloat(1),
                    new Date(cur.getLong(2)*1000),
                    cur.getInt(3)
            );
        }

        return transactions;
    }
}
