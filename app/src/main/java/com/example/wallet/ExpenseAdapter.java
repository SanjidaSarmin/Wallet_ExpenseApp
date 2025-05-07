package com.example.wallet;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;
    private DatabaseHelper dbHelper;
    private OnDataChangedListener dataChangedListener;

    // Constructor
    public ExpenseAdapter(List<Expense> expenseList, Context context, OnDataChangedListener listener) {
        this.expenseList = expenseList;
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.dataChangedListener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.tvTitle.setText(expense.getName());
        holder.tvAmount.setText("৳" + String.format("%.2f", expense.getAmount()));
        holder.tvTimestamp.setText(expense.getTimestamp());

        // Show confirmation dialog before deleting
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Expense")
                    .setMessage("Are you sure you want to delete this expense?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        dbHelper.deleteExpense(expense.getId());
                        expenseList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, expenseList.size());

                        Toast.makeText(context, "Expense deleted", Toast.LENGTH_SHORT).show();

                        if (dataChangedListener != null) {
                            dataChangedListener.onDataChanged();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of expenses
        return expenseList.size();
    }

    // Method to update the expenses data
    public void updateExpenses(List<Expense> newExpenses) {
        this.expenseList = newExpenses;
        notifyDataSetChanged();
    }

    // ViewHolder class to hold references to the views
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        // Declare the TextViews to display expense data
        TextView tvTitle, tvAmount, tvTimestamp;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews by finding their IDs in the layout
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp); // New TextView for timestamp
        }
    }

    public interface OnDataChangedListener {
        void onDataChanged();
    }
}
