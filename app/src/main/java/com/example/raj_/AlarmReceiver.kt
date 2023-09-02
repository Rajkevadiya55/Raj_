package com.example.raj_

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService


class AlarmReceiver : BroadcastReceiver() {


    private val TAG = "AlarmReceiver"

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: Alarm is Ringing")

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(5000)
        val contact = intent?.getStringExtra("contact")
        val amount = intent?.getStringExtra("amount")

        showNotification(context,contact,amount)

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show()

        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }


        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()

        Handler(Looper.getMainLooper()).postDelayed(
            {
                ringtone.stop()
            }, 10000
        )
    }

    @SuppressLint("ServiceCast", "MissingPermission")
    private fun showNotification(context: Context?,contact: String?, amount: String?) {
        val channelId = "alarm_channel"
        val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Alarm Notification")
            .setContentText("contact : $contact,Amount: $amount")
            .setPriority(NotificationCompat.PRIORITY_HIGH)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(1234, notificationBuilder.build())
        }
    }



}






