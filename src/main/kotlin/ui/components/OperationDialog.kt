package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    var selectedDate by remember { mutableStateOf(state.editingOperation?.date ?: LocalDate.now()) }

    val categories = if (type == "Доход") state.incomeCategories else state.expenseCategories

    LaunchedEffect(type, categories) {
        if (category.isEmpty() && categories.isNotEmpty()) {
            category = categories[0]
        }
    }

    AlertDialog(
        onDismissRequest = { viewModel.hideOperationDialog() },
        title = { Text(if (state.editingOperation == null) "Новая операция" else "Редактирование") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == "Доход",
                            onClick = { type = "Доход" }
                        )
                        Text("Доход", color = MaterialTheme.colors.secondary)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == "Расход",
                            onClick = { type = "Расход" }
                        )
                        Text("Расход", color = MaterialTheme.colors.error)
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = it
                        }
                    },
                    label = { Text("Сумма (руб.)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = amount.isNotEmpty() && amount.toDoubleOrNull() == null
                )

                Spacer(Modifier.height(16.dp))

                if (categories.isNotEmpty()) {
                    Text("Категория:", style = MaterialTheme.typography.caption)
                    Spacer(Modifier.height(4.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { item ->
                            Button(
                                onClick = { category = item },
                                modifier = Modifier.fillMaxWidth(),
                                colors = if (category == item)
                                    ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                                else
                                    ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
                            ) {
                                Text(
                                    item,
                                    color = if (category == item)
                                        MaterialTheme.colors.onPrimary
                                    else
                                        MaterialTheme.colors.onSurface
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                } else {
                    Text(
                        text = "Сначала создайте категории в настройках",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption
                    )
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedDate.toString(),
                    onValueChange = {},
                    label = { Text("Дата (ГГГГ-ММ-ДД)") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Сегодня", "Вчера", "Завтра").forEach { label ->
                        Button(
                            onClick = {
                                selectedDate = when (label) {
                                    "Сегодня" -> LocalDate.now()
                                    "Вчера" -> LocalDate.now().minusDays(1)
                                    "Завтра" -> LocalDate.now().plusDays(1)
                                    else -> LocalDate.now()
                                }
                            }
                        ) {
                            Text(label)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sum = amount.toDoubleOrNull()
                    if (sum == null || sum <= 0) {
                        return@Button
                    }

                    if (category.isEmpty()) {
                        return@Button
                    }

                    val op = Operation(
                        id = state.editingOperation?.id ?: 0,
                        type = type,
                        amount = sum,
                        category = category,
                        date = selectedDate
                    )
                    viewModel.saveOperation(op)
                },
                enabled = amount.isNotEmpty() && category.isNotEmpty()
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.hideOperationDialog() }
            ) {
                Text("Отмена")
            }
        }
    )
}
