package com.example.todo_app.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater; // đổi xml -> view
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_app.AddNewTask;
import com.example.todo_app.MainActivity;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.R;
import com.example.todo_app.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
//Adapter: nguồn cấp dữ liệu cho ListView, RecyclerView
// RecyclerView.Adapter: Quản lý dữ liệu và cập nhật dữ liệu cần hiện thị vào View
    private List<ToDoModel> todoList; //list in java
    private DatabaseHandler db;
    private MainActivity activity;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity) { //constructor - ham tao
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //tao giao dien
//  onCreateViewHolder : tạo ra đối tượng ViewHolder, trong nó chứa View hiện thị dữ liệu
//  ánh xạ view và data -> đổ dữ liệu vào biến view (view chính là task_layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false); //inflate view layout
        return new ViewHolder(itemView); //trả về task = create ViewHolder for each item
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
// onBindViewHolder: chuyển dữ liệu phần tử vào ViewHolder
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask()); //set text for the checkbox (task)
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //event
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //checked -> true
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0; //true, false
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged(); //
    }

    //    update, delete
    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position); //tbao recycler view
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId()); // pass argument as id and task to the AddNewTask fragment
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //  => thể hiện cấu trúc view, thông qua ViewHolder để sắp xếp dữ liệu vào từng item view hiển thị lên màn hình
// RecyclerView.ViewHolder:  lớp dùng để gán / cập nhật dữ liệu vào các phần tử
        CheckBox task;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            // đối tượng hiển thị dữ liệu là view con checkbox
        }
    }
}
