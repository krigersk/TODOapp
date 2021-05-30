package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.security.AccessControlContext

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "Tasks_to_do"
        private const val DB_VERSION = 1
        private const val TAB_NAME = "todo_tab"
        private const val ID = "id"
        private const val TASK = "task"
        private const val DATE = "date"
        private const val STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTodoTab = ("CREATE TABLE IF NOT EXISTS "+ TAB_NAME + "("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TASK + " TEXT, "+
                DATE + " TEXT, "+
                STATUS + " INTEGER)")
        db?.execSQL(createTodoTab)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS "+TAB_NAME)
        onCreate(db)
    }

    fun insertTask(model: taskclass):Long{
        val db = this.writableDatabase
        val contValues = ContentValues()
        contValues.put(ID, model.id)
        contValues.put(TASK, model.task)
        contValues.put(DATE, model.date)

        val insertProgress = db.insert(TAB_NAME, null, contValues)
        db.close()
        return insertProgress
    }

    fun updateTask(taskInp: taskclass){
        val db = this.writableDatabase
        val contValues = ContentValues()
        contValues.put(ID, taskInp.id)
        contValues.put(TASK, taskInp.task)
        contValues.put(DATE, taskInp.date)
        db.update(TAB_NAME, contValues, "ID=${taskInp.id}", null)
        db.close()
    }

    fun deleteTask(ident: Int){
        val db = this.writableDatabase

        val contValues = ContentValues()
        contValues.put(ID, ident)
        db.delete(TAB_NAME, "ID=$ident", null)
        db.close()
    }

    fun getAllRows(): ArrayList<taskclass>{
        val taskList: ArrayList<taskclass> = ArrayList()
        val select = "SELECT * FROM $TAB_NAME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(select, null)
        }catch(error: Exception){
            error.printStackTrace()
            db.execSQL(select)
            return ArrayList()
        }

        var id: Int
        var task: String
        var date: String
        var status: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                task = cursor.getString(cursor.getColumnIndex("task"))
                date = cursor.getString(cursor.getColumnIndex("date"))

                val taskCreated = taskclass(id = id, task = task, date = date)
                taskList.add(taskCreated)
            }while(cursor.moveToNext())
        }
        cursor.close()
        return taskList
    }

}