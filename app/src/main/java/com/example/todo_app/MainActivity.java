package com.example.todo_app;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todo_app.Adapter.ToDoAdapter;
import com.example.todo_app.Model.ToDoModel;
import com.example.todo_app.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList; //collection of task objs

    @Override
    protected void onCreate(Bundle savedInstanceState) { //call when activity has been created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //load UI

        db = new DatabaseHandler(this);
        db.openDatabase();

            //set up recycler view + setAdapter
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setLayoutManager: quy định vị trí các phần tử trong RecyclerView hiện thị
        tasksAdapter = new ToDoAdapter(db,MainActivity.this); //tao adapter
        tasksRecyclerView.setAdapter(tasksAdapter); 

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

//        add new task
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                //show the dialog fragment
            }
        }); //event when click on the float button
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){ //if the dialog in the bottom is closed, then...
        taskList = db.getAllTasks();
        Collections.reverse(taskList); //
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged(); //thông báo cho RecyclerView rằng dữ liệu thay đổi
    }
}