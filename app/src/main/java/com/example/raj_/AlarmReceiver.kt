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


class AlarmReceiver : BroadcastReceiver() {
    private val TAG = "AlarmReceiver"

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: Alarm is Ringing")

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(5000)

        showNotification(context)

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
    }, 10000)
}
    @SuppressLint("ServiceCast", "MissingPermission")
    private fun showNotification(context: Context?) {
        val channelId = "alarm_channel"
        val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Alarm Notification")
            .setContentText("Alarm! Wake up! Wake up!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Create a notification channel for devices running Android Oreo and above
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
            notify(123, notificationBuilder.build())
        }
    }

    companion object {
        @SuppressLint("ScheduleExactAlarm")
        fun setRepeatingAlarm(context: Context) {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            }

            val intervalMillis: Long = 24 * 60 * 60 * 10000 // 24 hours
            val initialMillis = System.currentTimeMillis()+ intervalMillis

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    initialMillis,
                    alarmIntent
                )
            } else {
                alarmMgr.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    initialMillis,
                    intervalMillis,
                    alarmIntent
                )
            }
        }


    }
}
   /* @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.e(TAG, "onReceive: Alarm is Ringing")

        var vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(5000);

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        var ringtone = RingtoneManager.getRingtone(context, alarmUri);

        ringtone.play();


    }
}
*/





