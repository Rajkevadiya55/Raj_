package com.example.raj_

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.raj_.databinding.ActivityRepeatBinding
import java.util.Calendar
import java.util.Locale

class RepeatActivity : AppCompatActivity() {

    lateinit var binding: ActivityRepeatBinding
    lateinit var btnDatePicker: Button
    lateinit var btnTimePicker: Button
    lateinit var txtDate: EditText
    lateinit var txtTime: EditText
    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var mHour: Int = 0
    var mMinute: Int = 0

    var requestCodeCounter=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepeatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnDatePicker = findViewById(R.id.btndate)

        binding.minit10.setOnClickListener {
            val contact = intent.getStringExtra("contact")
            val amount = intent.getStringExtra("amount")
            requestCodeCounter++
            setRepeatingAlarm(10 * 60 * 1000, contact, amount, requestCodeCounter) // 10 minutes
            Toast.makeText(this, "10 minutes", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.minit20.setOnClickListener {
            val contact = intent.getStringExtra("contact")
            val amount = intent.getStringExtra("amount")
            requestCodeCounter++
            setRepeatingAlarm(20 * 60 * 1000, contact, amount, requestCodeCounter) // 20 minutes
            Toast.makeText(this, "20 minutes", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.minit30.setOnClickListener {
            val contact = intent.getStringExtra("contact")
            val amount = intent.getStringExtra("amount")
            requestCodeCounter++
            setRepeatingAlarm(3 * 60 * 1000, contact, amount, requestCodeCounter) // 30 minutes
            Toast.makeText(this, "30 minutes", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.minit1h.setOnClickListener {
            val contact = intent.getStringExtra("contact")
            val amount = intent.getStringExtra("amount")
            requestCodeCounter++
            setRepeatingAlarm(60 * 60 * 1000, contact, amount, requestCodeCounter) // 1 hour
            Toast.makeText(this, "1 hour", Toast.LENGTH_SHORT).show()
            finish()
        }


        btnDatePicker.setOnClickListener {
            DateTimePickerDialog()
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun DateTimePickerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.item_date_time, null)
        val timePicker1 = dialogView.findViewById<TimePicker>(R.id.timepicker2)
        val datePicker1 = dialogView.findViewById<DatePicker>(R.id.datePicker2)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Select Date and Time")
            .setView(dialogView)
            .setPositiveButton("Set") { _, _ ->
                val hour = timePicker1.hour
                val minute = timePicker1.minute
                val year = datePicker1.year
                val month = datePicker1.month
                val day = datePicker1.dayOfMonth

                val calendar = Calendar.getInstance()
                calendar.set(year, month, day, hour, minute, 0)
                val alarmTimeMillis = calendar.timeInMillis

                val contact = intent.getStringExtra("contact")
                val amount = intent.getStringExtra("amount")

                requestCodeCounter++
                setAlarm(alarmTimeMillis, contact, amount, requestCodeCounter)
                Toast.makeText(this, "Alarm set for selected date and time", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(alarmTimeMillis: Long, contact: String?, amount: String?, requestCode: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent1 = Intent(this, AlarmReceiver::class.java)

        intent1.putExtra("contact", contact)
        intent1.putExtra("amount", amount)

        val notificationId = System.currentTimeMillis().toInt() // Generate a unique ID

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            notificationId,
            intent1,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                alarmTimeMillis,
                pendingIntent
            )
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun setRepeatingAlarm(
        intervalMillis: Long,
        contact: String?,
        amount: String?,
        requestCode: Int
    ) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent1 = Intent(this, AlarmReceiver::class.java)

        intent1.putExtra("contact", contact)
        intent1.putExtra("amount", amount)

        val notificationId = System.currentTimeMillis().toInt() // Generate a unique ID


        val pendingIntent = PendingIntent.getBroadcast(
            this,
            notificationId,
            intent1,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        val triggerAtMillis = System.currentTimeMillis() + intervalMillis

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }
}