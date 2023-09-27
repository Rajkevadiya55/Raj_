package com.example.raj_

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Task::class], version = 2)
abstract  class TaskDB:RoomDatabase() {
    companion object{

        fun init(context: Context):TaskDB{
            var db= Room.databaseBuilder(context,TaskDB::class.java,"task")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

            return db   
        }
    }

    abstract fun taskDao():TaskDao
}