package com.example.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpense extends AppCompatActivity {

    private EditText etName, etAmount;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        etName = findViewById(R.id.etName);
        etAmount = findViewById(R.id.etAmount);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (name.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // Send data back to MainActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("amount", amount);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}