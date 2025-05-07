package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<Expense> expenseList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch expenses grouped by month
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        expenseList = databaseHelper.getExpensesGroupedByMonth(); // Make sure this is the correct method

        // Set up the adapter
        historyAdapter = new HistoryAdapter(expenseList);
        recyclerView.setAdapter(historyAdapter);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_history) {
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(History.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_note) {
                startActivity(new Intent(History.this, Note.class));
                return true;
            }
            return false;
        });

    }
}