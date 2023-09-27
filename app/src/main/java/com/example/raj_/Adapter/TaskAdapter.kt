package com.example.raj_.Adapter

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.raj_.*

class TaskAdapter(list: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

  private  var list = list
    lateinit var db: TaskDB
    lateinit var context: Context

    class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var contact = itemView.findViewById<TextView>(R.id.number)
        var box = itemView.findViewById<CheckBox>(R.id.box)
        var time = itemView.findViewById<TextView>(R.id.time)
        var date = itemView.findViewById<TextView>(R.id.date)
        var delete = itemView.findViewById<ImageView>(R.id.taskdelete)
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

        holder.itemView.setOnClickListener {
            // Handle regular click event here
            val intent = Intent(context, TaskDetailsActivity::class.java).apply {
                putExtra("number", task.contact)
                putExtra("amount", task.amount)
                putExtra("description", task.description)
                putExtra("time", formattedTime)
                Log.e("ddd", "onBindViewHolder: " + task.contact)
            }
            context.startActivity(intent)
        }

//new
//        holder.delete.setOnClickListener {
//            val alertDialogBuilder = AlertDialog.Builder(context)
//            alertDialogBuilder.setTitle("Delete Task")
//            alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
//            alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
//                Log.d("byb b 5 yj", "onBindViewHolder: $list");
//                val task = list[position]
//
//                // Cancel the alarm associated with the deleted task
//                cancelAlarmForTask(task)
//
//                // Delete the task from the database
//                db.taskDao().deletetask(task)
//                list = db.taskDao().getTasks()
//                notifyItemRemoved(position)
//            }
//            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
//                dialog.dismiss()
//            }
//            val alertDialog = alertDialogBuilder.create()
//            alertDialog.show()
//        }
//
//}
//    private fun cancelAlarmForTask(task: Task) {
//        Log.d("hvgyfhkns", "cancelAlarmForTask: "+task)
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        // Create an intent to identify the alarm you want to cancel
//        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.action = "com.example.raj_.ALARM_ACTION" // Replace with your unique action name
//
//        // Use the task's id as the request code for the PendingIntent
//        val requestCode = task.requestcode
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            requestCode,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        // Cancel the alarm
//        alarmManager.cancel(pendingIntent)
//
//        // Log the request code of the canceled alarm
//        Log.d("AlarmCancel", "Canceled alarm for task with ID: $requestCode")
//
//        // Don't forget to unregister the pendingIntent
//        pendingIntent.cancel()
//    }
//    -799318366   set
//    -799318375  bind
//    -799318375  delete
        holder.delete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Delete Task")
            alertDialogBuilder.setMessage("Are you sure you want to delete this task?")
            alertDialogBuilder.setPositiveButton("Delete") { _, _ ->
                val task = list[position]

                cancelAlarmForTask(task)
                db.taskDao().deletetask(task)
                list = db.taskDao().getTasks()

                notifyDataSetChanged()
            }
            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun cancelAlarmForTask(task: Task) {
        Log.e("dishii", "cancelAlarmForTask: " + task.alarmId)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("contact", task.contact)
            putExtra("amount", task.amount)
        }

        val requestCode = task.alarmId.toInt()

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel the alarm
        alarmManager.cancel(pendingIntent)
    }

}

