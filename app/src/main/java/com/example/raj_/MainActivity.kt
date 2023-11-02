package com.example.raj_

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.raj_.Adapter.TaskAdapter
import com.example.raj_.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var db: TaskDB
    lateinit var binding: ActivityMainBinding
    lateinit var adapter: TaskAdapter

    private val PICK_CONTACT_REQUEST = 2
    // val RQS_1 = 1

    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var contactEdittext: EditText

    private val TAG = "MainActivity"
    private val READ_CONTACTS_PERMISSION_REQUEST = 1

    var repeat: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repeat = intent.getBooleanExtra("repeat", false)


        if (repeat)
            Toast.makeText(this, "Repeat Alarm", Toast.LENGTH_SHORT).show()

        initViews()
        initDB()
        setupRecyclerView()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST
            )
        }

    }
    private fun initViews() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        notificationManager = NotificationManagerCompat.from(this)

        binding.addtask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun initDB() {
        db = TaskDB.init(this)
    }

    private fun setupRecyclerView() {
        val list = db.taskDao().getTasks()
        adapter = TaskAdapter(list)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rcvrecycle.layoutManager = layoutManager
        binding.rcvrecycle.adapter = adapter
    }


    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(targetCal: Calendar, contact: String, amount: String, alarmId: Long) {
        val intent = Intent(baseContext, AlarmReceiver::class.java).apply {
            putExtra("contact", contact)
            putExtra("amount", amount)
        }
        Log.e("rajjjjj", "cancelAlarmForTask: " + alarmId)

        val requestCode = alarmId.toInt()

        val pendingIntent = PendingIntent.getBroadcast(
            baseContext,
            requestCode,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE
        )

        val alarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                targetCal.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, targetCal.timeInMillis, pendingIntent)
        }
    }

    private fun generateUniqueAlarmId(): Long {
        return System.currentTimeMillis()
    }

    private fun showAddTaskDialog() {
        val dialogView = layoutInflater.inflate(R.layout.add_task_dailog, null)
        val numberButton = dialogView.findViewById<Button>(R.id.btnnumber)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.edtDescription)
        val amountEditText = dialogView.findViewById<EditText>(R.id.edtamout)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.time)
        val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
        val requestcode = System.currentTimeMillis().toInt()
        contactEdittext = dialogView.findViewById<EditText>(R.id.set_contact)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val description = descriptionEditText.text.toString()
                val amount = amountEditText.text.toString()
                val contact = contactEdittext.text.toString()
                val hour = timePicker.hour
                val minute = timePicker.minute
                val year = datePicker.year
                val month = datePicker.month
                val day = datePicker.dayOfMonth
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                val time = String.format("%02d:%02d", hour, minute)
//                val requestcode = System.currentTimeMillis().toInt()


                if (contact.isNotBlank()) {


                    val alarmId = generateUniqueAlarmId()

                    val newTask = Task(contact, description, amount, time, selectedDate, alarmId)
                    db.taskDao().insertTask(newTask)
                    val calNow = Calendar.getInstance()
                    val calSet = calNow.clone() as Calendar
                    calSet[Calendar.YEAR] = year
                    calSet[Calendar.MONTH] = month
                    calSet[Calendar.DAY_OF_MONTH] = day
                    calSet[Calendar.HOUR_OF_DAY] = hour
                    calSet[Calendar.MINUTE] = minute
                    calSet[Calendar.SECOND] = 0
                    calSet[Calendar.MILLISECOND] = 0


                    if (calSet <= calNow) {
                        calSet.add(Calendar.DAY_OF_MONTH, 1)
                    }
                    if (calSet.compareTo(calNow) <= 0) {
                        calSet.add(Calendar.DATE, 1)
                    }

                    setAlarm(calSet, contact, amount, alarmId)
/*
                    setAlarm(calSet, contact, amount, requestcode)
*/
                    refreshActivity()
                    binding.rcvrecycle.scrollToPosition(0)
                    //  finish()

                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        numberButton.setOnClickListener {
            val contactPickerIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(contactPickerIntent, PICK_CONTACT_REQUEST)
        }

        dialog.show()
    }


    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {
            val contactUri = data?.data

            if (contactUri != null) {
                val cursor = contentResolver.query(contactUri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val displayName =
                            it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val contactId =
                            it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))

                        val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(contactId),
                            null
                        )

                        var phoneNumber: String? = null
                        phoneCursor?.use { phoneCursor ->
                            if (phoneCursor.moveToFirst()) {
                                phoneNumber = phoneCursor.getString(
                                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                )
                            }
                        }

                        if (!displayName.isNullOrEmpty()) {
                            if (!phoneNumber.isNullOrEmpty()) {
                                contactEdittext.setText("$displayName -\n $phoneNumber")
                            } else {
                                contactEdittext.setText(displayName)
                            }
                        }
                        2
                        phoneCursor?.close()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }
    }

    private fun refreshActivity() {

        recreate()
    }
}
