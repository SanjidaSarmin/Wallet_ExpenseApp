package com.example.wallet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<Expense> historyList;

    public HistoryAdapter(List<Expense> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Expense expense = historyList.get(position);

        // expense.name = "2025-05"
        String monthYear = expense.getName();
        String formattedMonth = formatMonthYear(monthYear);

        holder.tvMonth.setText(formattedMonth);
        holder.tvTotal.setText("৳" + String.format(Locale.getDefault(), "%.2f", expense.getAmount()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvTotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }

    // "2025-05" → "May 2025"
    private String formatMonthYear(String raw) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            SimpleDateFormat output = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            Date date = input.parse(raw);
            return output.format(date);
        } catch (ParseException e) {
            return raw;
        }
    }
}
