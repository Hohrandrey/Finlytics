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
import java.time.format.DateTimeParseException
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.LaunchedEffect
import ui.theme.AppColors

/**
 * Диалоговое окно для добавления или редактирования финансовой операции.
 * Поддерживает ввод всех параметров операции: тип, сумму, категорию и дату.
 *
 * @param viewModel ViewModel для управления операциями
 */
@Composable
fun OperationDialog(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Отладочная информация о редактируемой операции
    LaunchedEffect(viewModel.editingOperation) {
        println("\n=== OPERATION DIALOG ===")
        println("Режим: ${if (viewModel.editingOperation == null) "Добавление" else "Редактирование"}")
        println("Операция для редактирования: ${viewModel.editingOperation}")
        println("=========================\n")
    }

    var type by remember { mutableStateOf(viewModel.editingOperation?.type ?: "Расход") }
    var amount by remember { mutableStateOf(viewModel.editingOperation?.amount?.toString() ?: "") }
    var name by remember { mutableStateOf(viewModel.editingOperation?.name ?: "") }
    var category by remember { mutableStateOf(viewModel.editingOperation?.category ?: "") }
    var dateText by remember { mutableStateOf(viewModel.editingOperation?.date?.toString() ?: LocalDate.now().toString()) }
    var dateError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    val categories = if (type == "Доход") state.incomeCategories else state.expenseCategories

    // Преобразуем текст в LocalDate
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
        title = { Text(if (viewModel.editingOperation == null) "Новая операция" else "Редактирование операции") },
        text = {
            Column {
                // Выбор типа операции (Доход/Расход)
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
                            colors = RadioButtonDefaults.colors(AppColors.GreenColor),
                            onClick = {
                                type = "Доход"
                                amountError = ""
                                categoryError = ""
                            }
                        )
                        Text("Доход")
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
                            colors = RadioButtonDefaults.colors(AppColors.RedColor),
                            onClick = {
                                type = "Расход"
                                amountError = ""
                                categoryError = ""
                            }
                        )
                        Text("Расход")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Поле для ввода суммы
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
                        color = AppColors.RedColor,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Поле для ввода даты с валидацией
                OutlinedTextField(
                    value = dateText,
                    onValueChange = {
                        dateText = it
                        dateError = ""

                        // Проверка формата даты в реальном времени
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
                        color = AppColors.RedColor,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Быстрые кнопки для выбора стандартных дат
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
                                ButtonDefaults.buttonColors(backgroundColor = AppColors.BlueColor)
                            } else {
                                ButtonDefaults.buttonColors(backgroundColor = AppColors.LightGreyColor)
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
                                    AppColors.LightColor
                                } else {
                                    AppColors.LightColor
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Поле для ввода описания
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = { Text("Описание (необязательно)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // Выбор категории из списка
                if (categories.isNotEmpty()) {
                    Column {
                        Text("Категория:", style = MaterialTheme.typography.caption)
                        Spacer(Modifier.height(4.dp))

                        // Прокручиваемый список категорий
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
                                        ButtonDefaults.buttonColors(backgroundColor = AppColors.BlueColor)
                                    else
                                        ButtonDefaults.buttonColors(backgroundColor = AppColors.LightGreyColor)
                                ) {
                                    Text(
                                        item,
                                        color = if (category == item)
                                            AppColors.LightColor
                                        else
                                            AppColors.LightColor,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        if (categoryError.isNotEmpty()) {
                            Text(
                                text = categoryError,
                                color = AppColors.RedColor,
                                style = MaterialTheme.typography.caption,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                } else {
                    // Если категорий нет, предлагаем добавить новую
                    Column {
                        Text(
                            text = "Нет доступных категорий",
                            color = AppColors.RedColor,
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
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.BlueColor),
                onClick = {
                    // Валидация суммы
                    val sum = amount.toDoubleOrNull()
                    if (sum == null || sum <= 0) {
                        amountError = "Введите корректную сумму"
                        return@Button
                    }

                    // Валидация категории
                    if (category.isEmpty()) {
                        categoryError = "Выберите категорию"
                        return@Button
                    }

                    // Валидация даты
                    val parsedDate = try {
                        LocalDate.parse(dateText)
                    } catch (e: DateTimeParseException) {
                        dateError = "Неверный формат даты. Используйте ГГГГ-ММ-ДД"
                        return@Button
                    }

                    // Дополнительная проверка: дата не должна быть слишком далеко в будущем
                    if (parsedDate.isAfter(LocalDate.now().plusDays(1))) {
                        dateError = "Дата не может быть более чем на 1 день в будущем"
                        return@Button
                    }

                    val op = Operation(
                        id = viewModel.editingOperation?.id ?: 0,
                        type = type,
                        amount = sum,
                        category = category,
                        date = parsedDate,
                        name = name
                    )

                    viewModel.saveOperation(op)
                },
                enabled = amount.isNotEmpty() && category.isNotEmpty() && dateText.isNotEmpty() && selectedDate != null
            ) {
                Text("Сохранить", color = AppColors.LightColor)
            }
        },
        dismissButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.RedColor),
                onClick = {
                    viewModel.hideOperationDialog()
                    amountError = ""
                    categoryError = ""
                    dateError = ""
                }
            ) {
                Text("Отмена", color = AppColors.LightColor)
            }
        }
    )
}
