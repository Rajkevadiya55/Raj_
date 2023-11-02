package com.example.raj_
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.raj_.RepeatActivity

class AlarmReceiver : BroadcastReceiver() {

    private val TAG = "AlarmReceiver"

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: Alarm is Ringing")

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(5000)

        val contact = intent?.getStringExtra("contact")
        val amount = intent?.getStringExtra("amount")
        Log.d("ffff", "onReceive: $contact")
        Log.d("fff", "onReceive: $amount")
        showNotification(context, contact, amount)
        Log.d("ffff", "onReceive: $contact")
        Log.d("fff", "onReceive: $amount")
        Toast.makeText(context, "Alarm! Wake up! Wake up! $contact", Toast.LENGTH_LONG).show()

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
    @SuppressLint("ServiceCast", "MissingPermission", "UnspecifiedImmutableFlag")
    private fun showNotification(context: Context?, contact: String?, amount: String?) {
        val channelId = "alarm_channel"
        val repeataintent = Intent(context, RepeatActivity::class.java)
        Log.e(TAG, "showNotification1: $contact")
        Log.e(TAG, "showNotification2: $amount")
        repeataintent.putExtra("contact", contact)
        repeataintent.putExtra("amount", amount)
        Log.e(TAG, "showNotification1: $contact")
        Log.e(TAG, "showNotification2: $amount")
//        in this code, we use the requestCode from the Intent to generate a unique notification ID for each alarm. This should prevent the notifications from being overwritten by subsequent alarms
        val requestCode = repeataintent?.getIntExtra("requestCode", 0) ?: 0
        val repeatPendingIntent = PendingIntent.getActivity(
            context,
            requestCode, repeataintent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        Log.d(TAG, "showNotification: $contact")
        val phoneNumber = contact?.replace(Regex("[^0-9]"), "")
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        val callPendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            callIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )
        val whatsappIntent = Intent(Intent.ACTION_VIEW)
        whatsappIntent.data = Uri.parse("whatsapp://send?phone=$contact")
        val whatsappPendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            whatsappIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context!!, channelId)
            .setSmallIcon(R.drawable.notification_icon).setContentTitle("Alarm Notification")
            .setContentText("contact : $contact, Amount: $amount")
            .addAction(R.drawable.baseline_delete_24, "Repeat", repeatPendingIntent)
            .addAction(R.drawable.baseline_delete_24, "Call", callPendingIntent)
            .addAction(R.drawable.baseline_delete_24, "WhatsApp", whatsappPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(requestCode, notificationBuilder.build())
        }
    }
}
