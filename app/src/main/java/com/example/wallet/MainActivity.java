package com.example.wallet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.Model.DatabaseHelper;
import com.example.wallet.Model.Expense;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.view.View;


public class MainActivity extends AppCompatActivity {

        private RecyclerView recyclerExpenses;
        private ExpenseAdapter adapter;
        private List<Expense> expenseList;
        private TextView tvTotalSpent;
        private Button btnAddExpense;
        private DatabaseHelper dbHelper;
        private BottomNavigationView bottomNavigationView;

    private TextView tvMonthPicker;
    private String selectedYearMonth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            recyclerExpenses = findViewById(R.id.recyclerExpenses);
            tvTotalSpent = findViewById(R.id.tvTotalSpent);
            btnAddExpense = findViewById(R.id.btnAddExpense);
            tvMonthPicker = findViewById(R.id.tvMonthPicker);

            dbHelper = new DatabaseHelper(this);
            expenseList = new ArrayList<>();

            adapter = new ExpenseAdapter(expenseList, this, this::updateTotalSpent);
            recyclerExpenses.setLayoutManager(new LinearLayoutManager(this));
            recyclerExpenses.setAdapter(adapter);

            // Default to current month
            Calendar cal = Calendar.getInstance();
            selectedYearMonth = String.format("%04d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
            tvMonthPicker.setText(getDisplayMonth(selectedYearMonth));

            loadExpensesByMonth(selectedYearMonth); // load expenses for current month

            tvMonthPicker.setOnClickListener(v -> showMonthPicker());

            updateTotalSpent();

            btnAddExpense.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, AddExpense.class);
                startActivityForResult(intent, 1);
            });

            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    return true;
                } else if (id == R.id.nav_history) {
                    startActivity(new Intent(MainActivity.this, History.class));
                    return true;
                } else if (id == R.id.nav_note) {
                    startActivity(new Intent(MainActivity.this, Notes.class));
                    return true;
                }
                return false;
            });
        }

    private void showMonthPicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Use selectedYear and selectedMonth only
                    String monthYear = String.format("%04d-%02d", selectedYear, selectedMonth + 1);
                    // Now load expenses for this monthYear
                    loadExpensesByMonth(monthYear);
                }, year, month, c.get(Calendar.DAY_OF_MONTH));

        // Hide the day picker spinner
        try {
            java.lang.reflect.Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(datePickerDialog);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        if ("mDaySpinner".equals(datePickerField.getName()) || "mDayPicker".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            if (dayPicker instanceof View) {
                                ((View) dayPicker).setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        datePickerDialog.show();
    }


    private String getDisplayMonth(String yearMonth) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        String[] parts = yearMonth.split("-");
        return months[Integer.parseInt(parts[1]) - 1] + " " + parts[0];
    }

    private void loadExpensesByMonth(String yearMonth) {
        selectedYearMonth = yearMonth;
        tvMonthPicker.setText(getDisplayMonth(selectedYearMonth));
        expenseList = dbHelper.getExpensesByMonth(selectedYearMonth);
        adapter.updateExpenses(expenseList);
        updateTotalSpent();
    }


    private void updateTotalSpent() {
            double total = 0.0;
            for (Expense e : expenseList) {
                total += e.getAmount();
            }
            tvTotalSpent.setText("Total Spent: ৳" + String.format("%.2f", total));
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                double amount = data.getDoubleExtra("amount", 0.0);

                Expense newExpense = new Expense(name, amount);
                dbHelper.addExpense(newExpense);

                loadExpensesByMonth(selectedYearMonth);
                updateTotalSpent();
            }
        }
    }