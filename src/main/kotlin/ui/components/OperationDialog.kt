package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.Operation
import viewmodel.FinanceViewModel
import java.time.LocalDate

@Composable
fun OperationDialog(viewModel: FinanceViewModel) {
    val state = viewModel.state.value
    if (!state.showOperationDialog) return

    var type by remember { mutableStateOf(state.editingOperation?.type ?: "Расход") }
    var amount by remember { mutableStateOf(state.editingOperation?.amount?.toString() ?: "") }
    var category by remember { mutableStateOf(state.editingOperation?.category ?: "") }
    var date by remember { mutableStateOf(state.editingOperation?.date ?: LocalDate.now()) }

    val categories = if (type == "Доход") state.incomeCategories else state.expenseCategories

    AlertDialog(
        onDismissRequest = { viewModel.hideOperationDialog() },
        title = { Text(if (state.editingOperation == null) "Новая операция" else "Редактирование") },
        text = {
            Column {
                Row {
                    RadioButton(
                        selected = type == "Доход",
                        onClick = { type = "Доход" }
                    )
                    Text("Доход")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = type == "Расход",
                        onClick = { type = "Расход" }
                    )
                    Text("Расход")
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Сумма") },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenuBox(
                    value = category,
                    onValueChange = { category = it },
                    label = "Категория",
                    items = categories
                )

                DatePicker(
                    date = date,
                    onDateChange = { date = it }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val sum = amount.toDoubleOrNull() ?: return@Button
                val op = Operation(
                    id = state.editingOperation?.id ?: 0,
                    type = type,
                    amount = sum,
                    category = category,
                    date = date
                )
                viewModel.saveOperation(op)
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.hideOperationDialog() }) {
                Text("Отмена")
            }
        }
    )
}