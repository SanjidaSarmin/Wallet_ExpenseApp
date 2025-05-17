package com.example.wallet;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallet.Model.DatabaseHelper;
import com.example.wallet.Model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private DatabaseHelper dbHelper;

    // Updated constructor to accept dbHelper
    public TaskAdapter(List<Task> taskList, DatabaseHelper dbHelper) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBoxTask;

        public TaskViewHolder(View itemView) {
            super(itemView);
            checkBoxTask = itemView.findViewById(R.id.checkBoxTask);
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);

        holder.checkBoxTask.setOnCheckedChangeListener(null); // prevent unwanted triggers
        holder.checkBoxTask.setChecked(currentTask.isDone());
        holder.checkBoxTask.setText(currentTask.getTaskText());

        holder.checkBoxTask.setPaintFlags(
                currentTask.isDone()
                        ? holder.checkBoxTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                        : holder.checkBoxTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
        );

        // ✅ Handle long press to delete
        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            Task taskToDelete = taskList.get(currentPosition);

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteTask(currentTask.getId()); // Delete from DB
                        taskList.remove(position);                // Remove from list
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true; // Important to return true
        });

        // Restore checked change listener
        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTask.setDone(isChecked);
            dbHelper.updateTask(currentTask);
            holder.itemView.post(() -> notifyItemChanged(holder.getAdapterPosition()));
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void addTask(Task task) {
        dbHelper.addTask(task); // Save to DB
        taskList.add(task);     // Add to list
        notifyItemInserted(taskList.size() - 1);
    }

}
