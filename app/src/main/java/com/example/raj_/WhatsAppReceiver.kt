package com.example.raj_

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

class WhatsAppReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val contact = intent?.getStringExtra("contact")


        val packageManager = context?.packageManager
        val whatsappIntent = Intent(Intent.ACTION_VIEW)
        val whatsappPackage = "com.whatsapp"

        whatsappIntent.data = Uri.parse("whatsapp://send?phone=$contact")

        if (packageManager?.resolveActivity(whatsappIntent, 0) != null) {
            context.startActivity(whatsappIntent)
        } else {

        }
    }

}
