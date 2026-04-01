package br.edu.satc.todolistcompose.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val complete: Boolean = false
)

// Extension functions
fun TaskEntity.toTaskData() = TaskData(id, title, description, complete)
fun TaskData.toTaskEntity() = TaskEntity(id, title, description, complete)
