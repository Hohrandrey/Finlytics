package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import models.Operation
import viewmodel.FinanceViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun OperationDialog(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    var type by remember { mutableStateOf(viewModel.editingOperation?.type ?: "Расход") }
    var amount by remember { mutableStateOf(viewModel.editingOperation?.amount?.toString() ?: "") }
    var category by remember { mutableStateOf(viewModel.editingOperation?.category ?: "") }
    var dateText by remember { mutableStateOf(viewModel.editingOperation?.date?.toString() ?: LocalDate.now().toString()) }
    var dateError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    val categories = if (type == "Доход") state.incomeCategories else state.expenseCategories

    val selectedDate = remember(dateText) {
        try {
            LocalDate.parse(dateText)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    AlertDialog(
        onDismissRequest = {
            viewModel.hideOperationDialog()
            amountError = ""
            categoryError = ""
            dateError = ""
        },
        title = { Text(if (viewModel.editingOperation == null) "Новая операция" else "Редактирование") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            type = "Доход"
                            amountError = ""
                            categoryError = ""
                        }
                    ) {
                        RadioButton(
                            selected = type == "Доход",
                            onClick = {
                                type = "Доход"
                                amountError = ""
                                categoryError = ""
                            }
                        )
                        Text("Доход", color = MaterialTheme.colors.secondary)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            type = "Расход"
                            amountError = ""
                            categoryError = ""
                        }
                    ) {
                        RadioButton(
                            selected = type == "Расход",
                            onClick = {
                                type = "Расход"
                                amountError = ""
                                categoryError = ""
                            }
                        )
                        Text("Расход", color = MaterialTheme.colors.error)
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        amountError = ""
                    },
                    label = { Text("Сумма (руб.)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = amountError.isNotEmpty()
                )

                if (amountError.isNotEmpty()) {
                    Text(
                        text = amountError,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = dateText,
                    onValueChange = {
                        dateText = it
                        dateError = ""

                        try {
                            LocalDate.parse(it)
                            dateError = ""
                        } catch (e: DateTimeParseException) {
                            if (it.isNotEmpty()) {
                                if (it.length > 5) {
                                    dateError = "Формат даты: ГГГГ-ММ-ДД (например: ${LocalDate.now()})"
                                }
                            }
                        }
                    },
                    label = { Text("Дата (ГГГГ-ММ-ДД)") },
                    placeholder = { Text("Пример: ${LocalDate.now()}") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = dateError.isNotEmpty()
                )

                if (dateError.isNotEmpty()) {
                    Text(
                        text = dateError,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Сегодня", "Вчера", "Завтра").forEach { label ->
                        Button(
                            onClick = {
                                val newDate = when (label) {
                                    "Сегодня" -> LocalDate.now()
                                    "Вчера" -> LocalDate.now().minusDays(1)
                                    "Завтра" -> LocalDate.now().plusDays(1)
                                    else -> LocalDate.now()
                                }
                                dateText = newDate.toString()
                                dateError = ""
                            },
                            modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                            colors = if (selectedDate?.let {
                                    when (label) {
                                        "Сегодня" -> it == LocalDate.now()
                                        "Вчера" -> it == LocalDate.now().minusDays(1)
                                        "Завтра" -> it == LocalDate.now().plusDays(1)
                                        else -> false
                                    }
                                } == true) {
                                ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                            } else {
                                ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
                            }
                        ) {
                            Text(
                                label,
                                color = if (selectedDate?.let {
                                        when (label) {
                                            "Сегодня" -> it == LocalDate.now()
                                            "Вчера" -> it == LocalDate.now().minusDays(1)
                                            "Завтра" -> it == LocalDate.now().plusDays(1)
                                            else -> false
                                        }
                                    } == true) {
                                    MaterialTheme.colors.onPrimary
                                } else {
                                    MaterialTheme.colors.onSurface
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (selectedDate != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                val newDate = selectedDate.minusDays(1)
                                dateText = newDate.toString()
                            },
                            modifier = Modifier.weight(1f).padding(end = 2.dp)
                        ) {
                            Text("-1 день")
                        }

                        Button(
                            onClick = {
                                val newDate = selectedDate.plusDays(1)
                                dateText = newDate.toString()
                            },
                            modifier = Modifier.weight(1f).padding(start = 2.dp)
                        ) {
                            Text("+1 день")
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (categories.isNotEmpty()) {
                    Column {
                        Text("Категория:", style = MaterialTheme.typography.caption)
                        Spacer(Modifier.height(4.dp))

                        LazyColumn(
                            modifier = Modifier.heightIn(max = 200.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(categories) { item ->
                                Button(
                                    onClick = {
                                        category = item
                                        categoryError = ""
                                    },
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
                                            MaterialTheme.colors.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        if (categoryError.isNotEmpty()) {
                            Text(
                                text = categoryError,
                                color = MaterialTheme.colors.error,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                } else {
                    Column {
                        Text(
                            text = "Нет доступных категорий",
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.showAddCategory(type == "Доход")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Добавить категорию ${if (type == "Доход") "доходов" else "расходов"}")
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
                        amountError = "Введите корректную сумму"
                        return@Button
                    }

                    if (category.isEmpty()) {
                        categoryError = "Выберите категорию"
                        return@Button
                    }

                    val parsedDate = try {
                        LocalDate.parse(dateText)
                    } catch (e: DateTimeParseException) {
                        dateError = "Неверный формат даты. Используйте ГГГГ-ММ-ДД"
                        return@Button
                    }

                    if (parsedDate.isAfter(LocalDate.now().plusDays(1))) {
                        dateError = "Дата не может быть более чем на 1 день в будущем"
                        return@Button
                    }

                    val op = Operation(
                        id = viewModel.editingOperation?.id ?: 0,
                        type = type,
                        amount = sum,
                        category = category,
                        date = parsedDate
                    )

                    viewModel.saveOperation(op)
                },
                enabled = amount.isNotEmpty() && category.isNotEmpty() && dateText.isNotEmpty() && selectedDate != null
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.hideOperationDialog()
                    amountError = ""
                    categoryError = ""
                    dateError = ""
                }
            ) {
                Text("Отмена")
            }
        }
    )
}
