package br.edu.satc.todolistcompose.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).taskDao()
    private val prefs = application.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)

    val tasks = dao.getAllTasks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // SharedPreferences: salva quantas tasks foram criadas (contador)
    var taskCount: Int
        get() = prefs.getInt("task_count", 0)
        private set(value) { prefs.edit().putInt("task_count", value).apply() }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            dao.insertTask(TaskEntity(title = title, description = description))
            taskCount++
        }
    }

    fun toggleTask(task: TaskData) {
        viewModelScope.launch {
            dao.updateTask(task.toTaskEntity().copy(complete = !task.complete))
        }
    }
}
