package com.example.wallet;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

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
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);
        holder.checkBoxTask.setText(currentTask.getTaskText());
        holder.checkBoxTask.setChecked(currentTask.isDone());

        // Strike-through if done
        if (currentTask.isDone()) {
            holder.checkBoxTask.setPaintFlags(holder.checkBoxTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.checkBoxTask.setPaintFlags(holder.checkBoxTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Handle checkbox toggle
        holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTask.setDone(isChecked);
            dbHelper.updateTask(currentTask); // Update DB
            notifyItemChanged(holder.getAdapterPosition());
        });

        // Handle long-press to delete
        holder.itemView.setOnLongClickListener(view -> {
            dbHelper.deleteTask(currentTask.getId()); // Delete from DB
            taskList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());
            return true;
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
