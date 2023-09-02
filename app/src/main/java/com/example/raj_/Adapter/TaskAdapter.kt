package com.example.raj_.Adapter

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.raj_.AlarmReceiver
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
        var date = itemView.findViewById<TextView>(R.id.date)
      //  val menu: ImageView = itemView.findViewById(R.id.mmenu)


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

        val timeArray = task.time.split(":")
        val hour = timeArray[0].toInt()
        val minute = timeArray[1].toInt()
        val timeFormat = if (hour < 12) "AM" else "PM"
        val formattedHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
        val formattedTime = String.format("%02d:%02d %s", formattedHour, minute, timeFormat)
        holder.time.text = formattedTime

        // Format and display the date
        if (holder.date != null) {
            val dateArray = task.date.split("-")
            val year = dateArray[0].toInt()
            val month = dateArray[1].toInt()
            val day = dateArray[2].toInt()
            val formattedDate = String.format("%02d/%02d/%04d", month, day, year)
            holder.date.text = formattedDate
        }

        // Handle item click to show task details
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra("number", task.contact)
                putExtra("amount", task.amount)
                putExtra("description", task.description)
                putExtra("time", formattedTime)
            }
            context.startActivity(intent)
        }

        // Handle menu click to show popup menu
     /*   holder.menu.setOnClickListener { view ->
            showPopupMenu(view, position)
        }*/
    }

 /*   @SuppressLint("MissingInflatedId")
    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.item_menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this information?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val task = list[position]
                            db.taskDao().deletetask(task)
                            list = list.filterIndexed { index, _ -> index != position }
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }*/


}


