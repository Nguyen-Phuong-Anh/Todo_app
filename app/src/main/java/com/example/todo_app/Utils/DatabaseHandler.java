package com.example.todo_app.Utils;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo_app.Model.ToDoModel;

//import net.penguincoders.doit.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1; //phien ban
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)"; //sql query

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    } //ham tao vs ten va phien ban
// initializes the database with the specified name and version.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }  //create table

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed -> xoa bang
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again -> tao bang moi
        onCreate(db);
    } //cap nhat bang

    public void openDatabase() {
        db = this.getWritableDatabase(); //cho phep them, sua... vao db
    } //mo ket noi db: them, sua, xoa

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues(); //represent a row with value as objs with format as column-value
        //hang -> bat dau du lieu vao tung cot ->
        cv.put(TASK, task.getTask()); //doi tuong = format ten cot- du lieu
        cv.put(STATUS, 0); // 0 - false -> ...
        db.insert(TODO_TABLE, null, cv); //insert new row = them hang vao trong bang
    } //them task vao trong bang toDoListDatabase

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>(); //mang
        Cursor cur = null;  // Initialize a Cursor object to query the database ~ pointer to iterate over the rows
        //con tro -> duyet qua tung hang trong bang
        db.beginTransaction(); //bat dau....
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){  //trong bang co du lieu
                if(cur.moveToFirst()){  //chuyen con tro len dau bang
                    do{
                        ToDoModel task = new ToDoModel(); //
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task); //add each task (obj) to the array
                    }
                    while(cur.moveToNext()); ///chuyen con tro toi hang tiep theo
                }
            }
        }
        finally {
            db.endTransaction(); //dong...
            assert cur != null; //
            cur.close(); //dong con tro
        }
        return taskList; //tra ve mang
    }

    public void updateStatus(int id, int status){
            ContentValues cv = new ContentValues();
            cv.put(STATUS, status);
            db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    } //cap nhat trang thai

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    } //cap nhat task

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}