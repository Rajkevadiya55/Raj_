package com.example.raj_

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.raj_.databinding.ActivityRepeatBinding

class RepeatActivity : AppCompatActivity() {

    lateinit var binding: ActivityRepeatBinding
    var requestCodeCounter=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepeatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.minit10.setOnClickListener {
            val contact = intent.getStringExtra("contact")
            val amount = intent.getStringExtra("amount")
            requestCodeCounter++
            setRepeatingAlarm(1 * 60 * 1000, contact, amount, requestCodeCounter) // 10 minutes
            Toast.makeText(this, "10 minutes", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.minit20.setOnClickListener {
            val contact = intent.getStringExtra("contact")
            val amount = intent.getStringExtra("amount")
            requestCodeCounter++
            setRepeatingAlarm(2 * 60 * 1000, contact, amount, requestCodeCounter) // 20 minutes
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