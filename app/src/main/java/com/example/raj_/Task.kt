package com.example.raj_

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task")
data class Task(
    @ColumnInfo(name = "contact") var contact: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "amount") var amount: String,
    @ColumnInfo(name = "time") var time: String,

    ) {

    @PrimaryKey(autoGenerate = true)
    var id = 0
}
