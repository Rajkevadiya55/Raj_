package com.example.raj_

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: Task)

    @Query("SELECT * FROM task")
    fun getTasks(): List<Task>

    @Delete
    fun deletetask(task: Task)

    @Update
    fun updatetask(task: Task)


}