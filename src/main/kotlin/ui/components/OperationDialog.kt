package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import models.Operation
import viewmodel.FinanceViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException
import ui.theme.AppColors
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*

/**
 * Диалоговое окно для добавления или редактирования финансовой операции.
 * Поддерживает ввод всех параметров операции: тип, сумму, описание, категорию и дату.
 *
 * Функциональность:
 * - Выбор типа операции (Доход/Расход) с цветовой индикацией
 * - Ввод суммы с валидацией (положительное число)
 * - Выбор даты с быстрыми кнопками (Сегодня/Вчера/Завтра)
 * - Выбор категории из списка (динамически обновляется при изменении типа)
 * - Ввод описания (необязательно)
 * - Валидация всех полей перед сохранением
 *
 * Валидация:
 * - Сумма: положительное число, не может быть пустым или нулевым
 * - Категория: обязательно должна быть выбрана
 * - Дата: корректный формат ГГГГ-ММ-ДД, не более чем на 1 день в будущее
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
    val isIncome = type == "Доход"
    val typeColor = if (isIncome) AppColors.GreenColor else AppColors.RedColor

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
        title = {
            Text(
                if (viewModel.editingOperation == null) "Новая операция" else "Редактирование операции",
                color = AppColors.LightColor,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Выбор типа операции (Доход/Расход)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Тип операции",
                        color = AppColors.LightColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Кнопка "Доход"
                        TypeButton(
                            text = "Доход",
                            isSelected = type == "Доход",
                            color = AppColors.GreenColor,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                type = "Доход"
                                amountError = ""
                                categoryError = ""
                            }
                        )

                        // Кнопка "Расход"
                        TypeButton(
                            text = "Расход",
                            isSelected = type == "Расход",
                            color = AppColors.RedColor,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                type = "Расход"
                                amountError = ""
                                categoryError = ""
                            }
                        )
                    }
                }

                // Сумма и дата в одной строке
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Поле для ввода суммы
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Сумма (₽)",
                            color = AppColors.LightColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        OutlinedTextField(
                            value = amount,
                            onValueChange = {
                                amount = it
                                amountError = ""
                            },
                            placeholder = {
                                Text(
                                    "0.00",
                                    color = AppColors.LightColor.copy(alpha = 0.5f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isError = amountError.isNotEmpty(),
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = typeColor,
                                unfocusedBorderColor = AppColors.LightGreyColor,
                                cursorColor = typeColor,
                                textColor = AppColors.LightColor
                            )
                        )
                    }

                    // Поле для ввода даты
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Дата",
                            color = AppColors.LightColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        OutlinedTextField(
                            value = dateText,
                            onValueChange = {
                                dateText = it
                                dateError = ""
                                try {
                                    LocalDate.parse(it)
                                    dateError = ""
                                } catch (e: DateTimeParseException) {
                                    if (it.isNotEmpty() && it.length > 5) {
                                        dateError = "Формат: ГГГГ-ММ-ДД"
                                    }
                                }
                            },
                            placeholder = {
                                Text(
                                    "ГГГГ-ММ-ДД",
                                    color = AppColors.LightColor.copy(alpha = 0.5f)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isError = dateError.isNotEmpty(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = typeColor,
                                unfocusedBorderColor = AppColors.LightGreyColor,
                                cursorColor = typeColor,
                                textColor = AppColors.LightColor
                            )
                        )
                    }
                }

                // Отображение ошибок суммы и даты в одной строке
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Ошибка суммы
                    if (amountError.isNotEmpty()) {
                        Surface(
                            color = AppColors.RedColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = amountError,
                                color = AppColors.RedColor,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Ошибка даты
                    if (dateError.isNotEmpty()) {
                        Surface(
                            color = AppColors.RedColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = dateError,
                                color = AppColors.RedColor,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                // Быстрые кнопки для выбора дат
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Сегодня", "Вчера", "Завтра").forEach { label ->
                        QuickDateButton(
                            label = label,
                            isSelected = when (label) {
                                "Сегодня" -> selectedDate == LocalDate.now()
                                "Вчера" -> selectedDate == LocalDate.now().minusDays(1)
                                "Завтра" -> selectedDate == LocalDate.now().plusDays(1)
                                else -> false
                            },
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
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Поле для ввода описания
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Описание",
                        color = AppColors.LightColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = {
                            Text(
                                "Необязательно",
                                color = AppColors.LightColor.copy(alpha = 0.5f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = typeColor,
                            unfocusedBorderColor = AppColors.LightGreyColor,
                            cursorColor = typeColor,
                            textColor = AppColors.LightColor
                        )
                    )
                }

                // Выбор категории
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Категория",
                        color = AppColors.LightColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (categories.isNotEmpty()) {
                        // Список категорий
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(AppColors.LightGreyColor),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(categories) { item ->
                                CategoryItem(
                                    name = item,
                                    isSelected = category == item,
                                    onClick = {
                                        category = item
                                        categoryError = ""
                                    },
                                    color = typeColor
                                )
                            }
                        }

                        if (categoryError.isNotEmpty()) {
                            Surface(
                                color = AppColors.RedColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = categoryError,
                                    color = AppColors.RedColor,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    } else {
                        // Если категорий нет
                        Surface(
                            color = AppColors.RedColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "⚠️ Нет доступных категорий",
                                    color = AppColors.RedColor,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        viewModel.showAddCategory(type == "Доход")
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.BlueColor),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        "Добавить категорию",
                                        color = AppColors.LightColor,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Валидация суммы
                    val sum = amount.toDoubleOrNull()
                    if (sum == null || sum <= 0) {
                        amountError = "Введите сумму > 0"
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
                        dateError = "Неверный формат даты"
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
                        name = name.ifBlank { null }
                    )

                    viewModel.saveOperation(op)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = typeColor),
                enabled = amount.isNotEmpty() && category.isNotEmpty() && dateText.isNotEmpty() && selectedDate != null,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (viewModel.editingOperation == null) "Создать операцию" else "Сохранить изменения",
                    color = AppColors.LightColor,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.hideOperationDialog()
                    amountError = ""
                    categoryError = ""
                    dateError = ""
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.LightGreyColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Отмена", color = AppColors.LightColor)
            }
        },
        backgroundColor = AppColors.DarkGreyColor,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.widthIn(max = 800.dp)
    )
}

/**
 * Кнопка выбора типа операции (Доход/Расход).
 *
 * @param text Текст кнопки ("Доход" или "Расход")
 * @param isSelected Выбрана ли кнопка в данный момент
 * @param color Цвет кнопки при выборе (зелёный для доходов, красный для расходов)
 * @param modifier Модификатор для настройки внешнего вида
 * @param onClick Callback при нажатии на кнопку
 */
@Composable
private fun TypeButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) color else AppColors.LightGreyColor
        ),
        elevation = null
    ) {
        Text(
            text,
            color = if (isSelected) AppColors.LightColor else AppColors.LightColor,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

/**
 * Кнопка быстрого выбора даты (Сегодня, Вчера, Завтра).
 *
 * @param label Текст кнопки
 * @param isSelected Выбрана ли дата, соответствующая кнопке
 * @param onClick Callback при нажатии на кнопку
 * @param modifier Модификатор для настройки внешнего вида
 */
@Composable
private fun QuickDateButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) AppColors.BlueColor else AppColors.LightGreyColor
        ),
        elevation = null
    ) {
        Text(
            label,
            color = AppColors.LightColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

/**
 * Элемент списка категорий.
 *
 * Отображает название категории и иконку выбора при выделении.
 *
 * @param name Название категории
 * @param isSelected Выбрана ли категория
 * @param onClick Callback при нажатии на элемент
 * @param color Цвет выделения (зелёный для доходов, красный для расходов)
 */
@Composable
private fun CategoryItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = if (isSelected) color else AppColors.LightColor,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    imageVector = FinlyticsIconPack.Check,
                    contentDescription = "selected",
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}