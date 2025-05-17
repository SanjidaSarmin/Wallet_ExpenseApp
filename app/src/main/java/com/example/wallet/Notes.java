package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.Model.DatabaseHelper;
import com.example.wallet.Model.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Notes extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);

        dbHelper = new DatabaseHelper(this);

        // Load tasks from the database
        taskList = dbHelper.getAllTasks();

        taskAdapter = new TaskAdapter(taskList, dbHelper); // Pass dbHelper if adapter handles updates
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTasks.setAdapter(taskAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = editTextTask.getText().toString().trim();
                if (!taskText.isEmpty()) {
                    Task newTask = new Task(taskText); // Assuming constructor without ID
                    dbHelper.addTask(newTask); // Save to DB
                    taskList.clear();
                    taskList.addAll(dbHelper.getAllTasks()); // Reload tasks
                    taskAdapter.notifyDataSetChanged();
                    editTextTask.setText("");
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_note) {
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(Notes.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(Notes.this, History.class));
                return true;
            }
            return false;
        });
    }
}
