package com.example.raj_

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class TaskDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)


        val number = intent.getStringExtra("number")
        val amount = intent.getStringExtra("amount")
        val description = intent.getStringExtra("description")
        val time = intent.getStringExtra("time")

        Log.e("DISHA", "Contact :" + number )
        Log.e("DISHA1", "Contact :" + amount )
        Log.e("DISHA2", "Contact :" + description )
        Log.e("DISHA3", "Contact :" + time )


        // Now you can use these values to populate your activity's views
        val contactTextView = findViewById<TextView>(R.id.txtcontact)
        val amountTextView = findViewById<TextView>(R.id.txtamount)
        val descriptionTextView = findViewById<TextView>(R.id.txtdesc)
        val timeTextView = findViewById<TextView>(R.id.txttime)


        contactTextView.setText(number)
        amountTextView.setText(amount)
        descriptionTextView.text = description
        timeTextView.text = time
    }

    }
