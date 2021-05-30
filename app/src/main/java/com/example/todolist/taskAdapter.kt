package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import java.time.temporal.Temporal

class taskAdapter(): RecyclerView.Adapter<taskAdapter.taskView>() {

    private var taskList: ArrayList<taskclass> = ArrayList()
    private var onClickDeleteItem:((taskclass)->Unit)?=null
    private var onClickUpdateItem:((taskclass)->Unit)?=null
    private var onButtonClickUpdate:((taskclass)->Unit)?=null

    fun addTaskToList(item: ArrayList<taskclass>){
        this.taskList = item
    }

    fun deleteTaskOnClick(callback:(taskclass)->Unit){
        this.onClickDeleteItem = callback
    }

    fun updateTaskOnClick(callback:(taskclass)->Unit){
        this.onClickUpdateItem = callback
    }

    class taskView(var itemview: View): RecyclerView.ViewHolder(itemview){
        private var taskInserted = itemview.findViewById<TextView>(R.id.TaskText)
        private var dateCreated = itemview.findViewById<TextView>(R.id.dateCreated)
        var btnDelete = itemview.findViewById<Button>(R.id.deleteTask)
        var btnUpdate = itemview.findViewById<Button>(R.id.itemEdit)

        fun connectModelClass(taskC: taskclass){
            taskInserted.text = taskC.task.toString()
            dateCreated.text = taskC.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): taskView {
        return taskView(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: taskView, position: Int) {
        val taskNr = taskList[position]
        holder.connectModelClass(taskNr)
        holder.btnDelete.setOnClickListener {
            onClickDeleteItem?.invoke(taskNr)
        }
        holder.btnUpdate.setOnClickListener {
            onClickUpdateItem?.invoke(taskNr)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}