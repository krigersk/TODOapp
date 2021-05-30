package com.example.todolist


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var addButton : Button
    private lateinit var refreshButton : Button
    private lateinit var updateButton : Button
    private lateinit var insertTask : EditText
    private lateinit var insertDate : TextView
    private lateinit var myRecyclerView : RecyclerView

    private var adapter: taskAdapter? = null

    private var taskNew:taskclass ?= null

    private lateinit var mySQLhelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mySQLhelper = DBHelper(this)

        inView()
        inRecyclerView()
        showTasks()

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val current: String = simpleDateFormat.format(Date())
        insertDate.text = current

        addButton.setOnClickListener {
            addTask()
            showTasks()
        }

        refreshButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        updateButton.setOnClickListener {
            updateTask()
        }

        adapter?.deleteTaskOnClick {
            deleteTask(it.id)
        }

        adapter?.updateTaskOnClick {
            insertTask.setText(it.task)
            insertDate.setText(it.date)
            taskNew = it
        }
    }

    private fun inRecyclerView(){
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = taskAdapter()
        myRecyclerView.adapter = adapter
    }

    private fun inView(){
        addButton = findViewById<Button>(R.id.mainAddBtn)
        updateButton = findViewById<Button>(R.id.mainUpdateBtn)
        refreshButton = findViewById<Button>(R.id.mainEditBtn)
        insertTask = findViewById<EditText>(R.id.mainInputTask)
        insertDate = findViewById<Button>(R.id.mainDateCreated)
        myRecyclerView = findViewById<RecyclerView>(R.id.taskList)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            showTasks()
        }
    }


    private fun addTask(){
        val taskInp = insertTask.text.toString()
        val current = insertDate.text.toString()
        if(taskInp!=""){
            val task = taskclass(task = taskInp, date = current)
            val check = mySQLhelper.insertTask(task)
            if(check>-1){
                insertTask.setText("")
                showTasks()
            }
        }
    }

    private fun showTasks(){
        val taskList = mySQLhelper.getAllRows()
        //adapter?.addTaskToList(taskList)
        adapter?.addTaskToList(taskList)
    }

    private fun deleteTask(id: Int){
        if(id==null) return
        else {
            mySQLhelper.deleteTask(id)
        }
        showTasks()
    }

    private fun updateTask(){
        val taskN = insertTask.text.toString()
        val dateN = insertDate.text.toString()

        val newTask = taskclass(id = taskNew!!.id, task = taskN, date = dateN)
        mySQLhelper.updateTask(newTask)
    }
}