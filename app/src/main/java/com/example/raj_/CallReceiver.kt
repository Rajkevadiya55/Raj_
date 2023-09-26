package com.example.raj_

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri


class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val contact = intent?.getStringExtra("contact")
        if (!contact.isNullOrEmpty()) {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:$contact")
            context?.startActivity(callIntent)
        } else {

        }
    }
}
