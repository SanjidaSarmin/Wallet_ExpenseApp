package com.example.wallet;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "wallet_db";
    private static final int DATABASE_VERSION = 1;

    // Expense table details
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database table when the app is first run
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrade the database if needed (e.g., if a new version is released)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Insert a new expense into the database
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, expense.getName());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        // Insert the expense into the database
        db.insert(TABLE_EXPENSES, null, values);
        db.close(); // Close the database connection
    }

    // Get all expenses from the database
    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Select all expenses
        String SELECT_ALL_EXPENSES = "SELECT * FROM " + TABLE_EXPENSES;
        Cursor cursor = db.rawQuery(SELECT_ALL_EXPENSES, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                    @SuppressLint("Range") String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));

                    // Create an Expense object and add it to the list
                    Expense expense = new Expense(id, name, amount, timestamp);
                    expenseList.add(expense);
                } while (cursor.moveToNext());
            }
            cursor.close(); // Close the cursor
        }
        db.close(); // Close the database connection
        return expenseList;
    }

    // Get the total amount spent from all expenses
    public double getTotalSpent() {
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_TOTAL_SPENT = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_EXPENSES;
        Cursor cursor = db.rawQuery(SELECT_TOTAL_SPENT, null);

        if (cursor != null) {
            cursor.moveToFirst();
            double total = cursor.getDouble(0); // Get the sum of amounts
            cursor.close();
            db.close(); // Close the database connection
            return total;
        }
        db.close();
        return 0;
    }

    public List<Expense> getExpensesForCurrentMonth() {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the current year and month
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 0-based index

        String sql = "SELECT * FROM expenses WHERE strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ?";
        String[] args = {
                String.valueOf(year),
                String.format("%02d", month) // Format month to "01", "02", etc.
        };

        Cursor cursor = db.rawQuery(sql, args);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                expenses.add(new Expense(id, name, amount, timestamp));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenses;
    }



    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Expense> getExpensesGroupedByMonth() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get all months from the expenses table
        String SELECT_GROUPED_BY_MONTH = "SELECT strftime('%Y-%m', timestamp) AS month, SUM(amount) AS total " +
                "FROM expenses GROUP BY month ORDER BY month DESC";

        Cursor cursor = db.rawQuery(SELECT_GROUPED_BY_MONTH, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String month = cursor.getString(cursor.getColumnIndex("month"));
                    @SuppressLint("Range") double totalAmount = cursor.getDouble(cursor.getColumnIndex("total"));

                    // Add the month and total to the expense list as if it's an expense object
                    Expense expense = new Expense();
                    expense.setName(month); // Storing the month as the name
                    expense.setAmount(totalAmount); // Storing the total amount spent in that month
                    expenseList.add(expense);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return expenseList;
    }




}