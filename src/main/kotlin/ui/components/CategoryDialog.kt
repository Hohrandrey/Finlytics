package ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import viewmodel.FinanceViewModel

@Composable
fun CategoryDialog(viewModel: FinanceViewModel) {
    val state = viewModel.state.value
    if (!state.showCategoryDialog) return

    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { viewModel.hideCategoryDialog() },
        title = { Text("Новая категория") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название категории") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank()) {
                    viewModel.saveCategory(name.trim())
                }
            }) {
                Text("Добавить")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.hideCategoryDialog() }) {
                Text("Отмена")
            }
        }
    )
}