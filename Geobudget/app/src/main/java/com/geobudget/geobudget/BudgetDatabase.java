package com.geobudget.geobudget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            public static final String IS_INCOME = "is_income";
        }

        public class Payment implements BaseColumns {
            public static final String TABLE_NAME = "payment";
            public static final String EXPENDITURE = "expenditure";
            public static final String DATE = "date";
            public static final String BUDGET = "budget";
        }
    }
    // --- ---

    // --- DATABASE INTERACTION HELPER ---
    private class BudgetDatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 5;
        private static final String DATABASE_NAME = "Geobudget.db";
        private final Float FLOAT_PRECISION = (float) 24;
        private final String CREATE_BUDGET_TABLE =
                "CREATE TABLE " + BudgetDatabaseContract.Budget.TABLE_NAME + "("
                + BudgetDatabaseContract.Budget._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + BudgetDatabaseContract.Budget.CATEGORY + " TEXT,"
                + BudgetDatabaseContract.Budget.ALLOWANCE + " FLOAT(" + FLOAT_PRECISION.toString() + "),"
                + BudgetDatabaseContract.Budget.IS_INCOME + " INTEGER"
                + ")";
        private final String CREATE_TRANSACTION_TABLE =
                "CREATE TABLE " + BudgetDatabaseContract.Payment.TABLE_NAME + "("
                + BudgetDatabaseContract.Payment._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + BudgetDatabaseContract.Payment.EXPENDITURE + " FLOAT(" + FLOAT_PRECISION.toString() + "),"
                + BudgetDatabaseContract.Payment.DATE + " DATE,"  // Format YYYY-MM-DD
                + BudgetDatabaseContract.Payment.BUDGET + " INTEGER, FOREIGN KEY (" + BudgetDatabaseContract.Payment._ID + ") REFERENCES "
                        + BudgetDatabaseContract.Budget.TABLE_NAME + "(" + BudgetDatabaseContract.Budget._ID + ")"
                + ")";

        private static final String DROP_BUDGET_TABLE =
                "DROP TABLE IF EXISTS " + BudgetDatabaseContract.Budget.TABLE_NAME;

        private static final String DROP_TRANSACTION_TABLE =
                "DROP TABLE IF EXISTS " + BudgetDatabaseContract.Payment.TABLE_NAME;

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
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Income', 320, 1);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Living Costs', 80, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Leisure', 20, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Travel', 50, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Home', 70, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Giving', 20, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Family and Pets', 15, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Future needs', 10, 0);");
        db.execSQL("INSERT INTO budget (category, allowance, is_income) VALUES ('Debt Repayment', 40, 0);");
    }

    public void addTestTransaction() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (1,  5.5,  '2017-10-22', (SELECT _id FROM budget WHERE category = 'Living Costs'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (2,  14.3, '2017-10-21', (SELECT _id FROM budget WHERE category = 'Travel'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (3,  6.95, '2017-10-12', (SELECT _id FROM budget WHERE category = 'Home'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (4,  10,   '2017-10-17', (SELECT _id FROM budget WHERE category = 'Giving'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (5,  20,   '2017-10-20', (SELECT _id FROM budget WHERE category = 'Living Costs'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (6,  30,   '2017-10-11', (SELECT _id FROM budget WHERE category = 'Travel'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (7,  16.2, '2017-10-22', (SELECT _id FROM budget WHERE category = 'Living Costs'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (8,  5.3,  '2017-10-16', (SELECT _id FROM budget WHERE category = 'Family and Pets'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (9,  3.2,  '2017-10-11', (SELECT _id FROM budget WHERE category = 'Home'));");
        db.execSQL("INSERT INTO payment (_ID, expenditure, date, BUDGET) VALUES (10, 12.7, '2017-10-15', (SELECT _id FROM budget WHERE category = 'Leisure'));");    }

    public Budget getBudget(int id) {
        Cursor cur = helper.getReadableDatabase().rawQuery(String.format("SELECT category, allowance, is_income, (SELECT SUM(expenditure) FROM payment WHERE payment.budget = budget._id) FROM budget WHERE _id = %d;", id), null);

        Budget b = null;
        if (cur.moveToFirst()) {
            String category = cur.getString(0);
            float allowance = cur.getFloat(1);
            boolean isIncome = cur.getInt(2) != 0;
            float totalExpenditure = cur.getFloat(3);
            b = new Budget(id, category, allowance, totalExpenditure, isIncome);
        }

        cur.close();
        return b;
    }

    public ArrayList<Budget> getBudgets() {
        return getBudgets(false);
    }

    public ArrayList<Budget> getBudgets(boolean includeIncome) {
        String sql = "SELECT _id, category, allowance, is_income, (SELECT SUM(expenditure) FROM payment WHERE payment.budget = budget._id) FROM budget";

        if (!includeIncome) {
            sql += " WHERE is_income = 0";
        }

        Cursor cur = helper.getReadableDatabase().rawQuery(sql, null);
        ArrayList<Budget> l = new ArrayList<Budget>();

        while (cur.moveToNext()) {
            int id = cur.getInt(0);
            String category = cur.getString(1);
            float allowance = cur.getFloat(2);
            boolean isIncome = cur.getInt(3) != 0;
            float totalExpenditure = cur.getFloat(4);
            Budget b = new Budget(id, category, allowance, totalExpenditure, isIncome);
            l.add(b);
        }

        return l;
    }

    public void updateBudget(int id, Budget updatedBudget) {
        helper.getWritableDatabase().execSQL(
                "UPDATE budget"
                + " SET category = " + updatedBudget.getCategory()
                    + ", allowance = " + updatedBudget.getAllowance()
                + " WHERE _id = " + id
        );
    }

    public void addTransaction(Payment newPayment) {
        helper.getWritableDatabase().execSQL(
                "INSERT INTO payment (expenditure, date, budget) VALUES ("
                        + newPayment.getExpenditure()
                        + ", " + newPayment.getDate()
                        + ", " + newPayment.getBudget()
                + ")"
        );
    }

    public ArrayList<Payment> getPaymentsForBudget(Integer budgetId) {
        Cursor cur = helper.getReadableDatabase().rawQuery(
                "SELECT *"
                + " FROM payment"
                + " WHERE budget = " + budgetId.toString(),
                null
        );

        ArrayList<Payment> payments = new ArrayList<>();
        while(cur.moveToNext()) {
            Payment payment = new Payment(
                    cur.getInt(0),
                    cur.getFloat(1),
                    new Date(cur.getLong(2)),
                    cur.getInt(3)
            );

            payments.add(payment);
        }

        return payments;
    }
}
