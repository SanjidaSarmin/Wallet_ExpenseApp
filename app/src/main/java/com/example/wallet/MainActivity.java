package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerExpenses;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList;
    private TextView tvTotalSpent;
    private Button btnAddExpense;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerExpenses = findViewById(R.id.recyclerExpenses);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        dbHelper = new DatabaseHelper(this);
        expenseList = new ArrayList<>();

        adapter = new ExpenseAdapter(expenseList, this, this::updateTotalSpent);
        recyclerExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerExpenses.setAdapter(adapter);

        loadExpensesFromDatabase();
        updateTotalSpent();


        btnAddExpense.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddExpense.class);
            startActivityForResult(intent, 1);
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already on Home
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(MainActivity.this, History.class));
                return true;
            } else if (id == R.id.nav_note) {
                startActivity(new Intent(MainActivity.this, Note.class));
                return true;
            }
            return false;
        });

    }

    private void loadExpensesFromDatabase() {
        expenseList = dbHelper.getExpensesForCurrentMonth();
        adapter.updateExpenses(expenseList);
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

            loadExpensesFromDatabase();
            updateTotalSpent();
        }
    }
}
