package br.edu.satc.todolistcompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.satc.todolistcompose.data.TaskViewModel
import br.edu.satc.todolistcompose.data.toTaskData
import br.edu.satc.todolistcompose.ui.components.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (tasks.isEmpty()) {
            Text(
                text = "Nenhuma tarefa ainda!",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { taskEntity ->
                    val task = taskEntity.toTaskData()
                    TaskCard(
                        taskData = task,
                        onTaskCheckedChange = { viewModel.toggleTask(task) }
                    )
                }
            }
        }
        NewTask(
            modifier = Modifier.align(Alignment.BottomEnd),
            onSaveTask = { title, description -> viewModel.addTask(title, description) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTask(
    modifier: Modifier = Modifier,
    onSaveTask: (String, String) -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()

    ExtendedFloatingActionButton(
        onClick = { showBottomSheet = true },
        icon = { Icon(Icons.Filled.Add, "Nova Tarefa") },
        text = { Text("Nova Tarefa") },
        modifier = modifier.padding(16.dp)
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nova Tarefa", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            onSaveTask(taskTitle, taskDescription)
                            taskTitle = ""
                            taskDescription = ""
                            showBottomSheet = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Salvar")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
