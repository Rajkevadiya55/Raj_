package com.example.raj_.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.raj_.R
import com.example.raj_.Task
import com.example.raj_.TaskDB
import com.example.raj_.TaskDetailsActivity

class TaskAdapter(list: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    var list = list
    lateinit var db: TaskDB
    lateinit var context: Context

    class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var contact = itemView.findViewById<TextView>(R.id.number)
        var box = itemView.findViewById<CheckBox>(R.id.box)
        var time = itemView.findViewById<TextView>(R.id.time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        context = parent.context

        return TaskHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        db = TaskDB.init(context)
        val task = list[position]

        holder.contact.text = list.get(position).contact
        holder.time.text = list.get(position).time
        val timeArray = task.time.split(":")
        val hour = timeArray[0].toInt()
        val minute = timeArray[1].toInt()
        val timeFormat = if (hour < 12) "AM" else "PM"
        val formattedHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
        val formattedTime = String.format("%02d:%02d %s", formattedHour, minute, timeFormat)
        holder.time.text = formattedTime
        holder.time.text


        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra("number", task.contact)
                putExtra("amount", task.amount)
                putExtra("description", task.description)
                putExtra("time", formattedTime)

            }
            context.startActivity(intent)
        }


    }
}