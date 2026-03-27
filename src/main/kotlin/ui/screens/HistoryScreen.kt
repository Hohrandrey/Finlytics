package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import models.Operation
import ui.components.NavigationBar
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*
import viewmodel.FinanceViewModel

/**
 * Экран "История" приложения.
 * Отображает список всех финансовых операций с возможностью фильтрации.
 *
 * Поддерживает:
 * - Фильтрацию по типу операций (Все/Доходы/Расходы)
 * - Фильтрацию по временному периоду (День/Неделя/Месяц/Год/Всё время)
 * - Редактирование операции
 * - Удаление операции с подтверждением (единый стиль с остальными диалогами)
 *
 * @param viewModel ViewModel для управления данными
 */
@Composable
fun HistoryScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Состояния для фильтров
    var selectedFilter by remember { mutableStateOf("Все") }
    var selectedPeriod by remember { mutableStateOf("Всё время") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var operationToDelete by remember { mutableStateOf<Operation?>(null) }

    // Форматирование даты для отображения
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val monthYearFormatter = DateTimeFormatter.ofPattern("MM.yyyy")
    val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

    // Функция для фильтрации операций
    val filteredOperations = remember(state.operations, selectedFilter, selectedPeriod, selectedDate) {
        filterOperations(
            operations = state.operations,
            filterType = selectedFilter,
            period = selectedPeriod,
            selectedDate = selectedDate
        )
    }

    // Отладочная информация об операциях
    LaunchedEffect(state.operations, filteredOperations) {
        println("\n=== HISTORY SCREEN ===")
        println("Всего операций в базе: ${state.operations.size}")
        println("Отфильтровано операций: ${filteredOperations.size}")
        println("Фильтр: $selectedFilter, Период: $selectedPeriod")
        println("Операций доходов: ${filteredOperations.count { it.type == "Доход" }}")
        println("Операций расходов: ${filteredOperations.count { it.type == "Расход" }}")
        if (filteredOperations.isNotEmpty()) {
            println("Последние 3 отфильтрованные операции:")
            filteredOperations.take(3).forEach { op ->
                println("  - ${op.type}: ${op.category} - ${op.amount} руб. (${op.date})")
            }
        }
        println("======================\n")
    }

    // Функция для получения отображаемой даты в зависимости от периода
    val displayDateText = remember(selectedPeriod, selectedDate) {
        when (selectedPeriod) {
            "День" -> selectedDate.format(dateFormatter)
            "Неделя" -> {
                val weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
                val weekEnd = weekStart.plusDays(6)
                "${weekStart.format(dateFormatter)} - ${weekEnd.format(dateFormatter)}"
            }
            "Месяц" -> selectedDate.format(monthYearFormatter)
            "Год" -> selectedDate.format(yearFormatter)
            else -> ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.DarkColor)
    ) {
        // Контент страницы
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
        ) {
            // Секция настроек
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(193.dp)
                    .background(AppColors.DarkGreyColor, RoundedCornerShape(30.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 40.dp, end = 40.dp, top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Фильтр по типу
                    Column(
                        modifier = Modifier.width(390.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            "Параметр отображения",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.LightColor
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Кнопка "Все"
                            FilterButton(
                                text = "Все",
                                isSelected = selectedFilter == "Все",
                                isAll = true,
                                modifier = Modifier.width(85.dp).height(98.dp),
                                onClick = { selectedFilter = "Все" }
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                // Кнопка "Доходы"
                                FilterButton(
                                    text = "Доходы",
                                    isSelected = selectedFilter == "Доходы",
                                    icon = true,
                                    modifier = Modifier.width(290.dp).height(42.dp),
                                    onClick = { selectedFilter = "Доходы" }
                                )

                                // Кнопка "Расходы"
                                FilterButton(
                                    text = "Расходы",
                                    isSelected = selectedFilter == "Расходы",
                                    icon = true,
                                    isIncome = false,
                                    modifier = Modifier.width(290.dp).height(42.dp),
                                    onClick = { selectedFilter = "Расходы" }
                                )
                            }
                        }
                    }

                    // Период времени
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            "Период времени",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.LightColor
                        )

                        // Кнопки периодов
                        Row(
                            modifier = Modifier.width(350.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PeriodButton(
                                text = "День",
                                isSelected = selectedPeriod == "День",
                                onClick = {
                                    selectedPeriod = "День"
                                    selectedDate = LocalDate.now()
                                }
                            )
                            PeriodButton(
                                text = "Неделя",
                                isSelected = selectedPeriod == "Неделя",
                                onClick = {
                                    selectedPeriod = "Неделя"
                                    selectedDate = LocalDate.now()
                                }
                            )
                            PeriodButton(
                                text = "Месяц",
                                isSelected = selectedPeriod == "Месяц",
                                onClick = {
                                    selectedPeriod = "Месяц"
                                    selectedDate = LocalDate.now()
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.width(350.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PeriodButton(
                                text = "Год",
                                isSelected = selectedPeriod == "Год",
                                onClick = {
                                    selectedPeriod = "Год"
                                    selectedDate = LocalDate.now()
                                }
                            )
                            PeriodButton(
                                text = "Всё время",
                                isSelected = selectedPeriod == "Всё время",
                                onClick = {
                                    selectedPeriod = "Всё время"
                                }
                            )
                        }
                    }

                    // Настройки даты
                    if (selectedPeriod != "Всё время") {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            Text(
                                selectedPeriod,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = AppColors.LightColor
                            )

                            // Поле ввода даты
                            Box(
                                modifier = Modifier
                                    .width(390.dp)
                                    .height(42.dp)
                                    .background(AppColors.LightGreyColor, RoundedCornerShape(10.dp))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(Modifier.width(16.dp))

                                    Icon(
                                        imageVector = FinlyticsIconPack.Date,
                                        contentDescription = "date",
                                        modifier = Modifier.size(18.dp),
                                        tint = AppColors.LightColor
                                    )

                                    Spacer(Modifier.width(18.dp))

                                    Text(
                                        displayDateText,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.LightColor
                                    )

                                    Spacer(Modifier.weight(1f))

                                    // Кнопки навигации
                                    Row(
                                        modifier = Modifier.padding(end = 6.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                selectedDate = when (selectedPeriod) {
                                                    "День" -> selectedDate.minusDays(1)
                                                    "Неделя" -> selectedDate.minusWeeks(1)
                                                    "Месяц" -> selectedDate.minusMonths(1)
                                                    "Год" -> selectedDate.minusYears(1)
                                                    else -> selectedDate
                                                }
                                            },
                                            modifier = Modifier
                                                .background(AppColors.BlueColor, RoundedCornerShape(5.dp))
                                                .size(20.dp)
                                        ) {
                                            Icon(
                                                imageVector = FinlyticsIconPack.Left,
                                                contentDescription = "previous_period",
                                                modifier = Modifier.size(20.dp),
                                                tint = AppColors.LightColor
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                selectedDate = when (selectedPeriod) {
                                                    "День" -> selectedDate.plusDays(1)
                                                    "Неделя" -> selectedDate.plusWeeks(1)
                                                    "Месяц" -> selectedDate.plusMonths(1)
                                                    "Год" -> selectedDate.plusYears(1)
                                                    else -> selectedDate
                                                }
                                            },
                                            modifier = Modifier
                                                .background(AppColors.BlueColor, RoundedCornerShape(5.dp))
                                                .size(20.dp)
                                        ) {
                                            Icon(
                                                imageVector = FinlyticsIconPack.Right,
                                                contentDescription = "next_period",
                                                modifier = Modifier.size(20.dp),
                                                tint = AppColors.LightColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Пустой колонка для выравнивания, когда поле даты скрыто
                        Spacer(modifier = Modifier.width(390.dp))
                    }
                }
            }

            // Основной контент
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 243.dp)
            ) {
                if (filteredOperations.isEmpty()) {
                    // Сообщение, если операций нет
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Нет операций за выбранный период",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppColors.LightGreyColor
                        )
                    }
                } else {
                    // Список операций
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(filteredOperations) { operation ->
                            TransactionItem(
                                operation = operation,
                                onEditClick = { viewModel.showEditOperation(operation) },
                                onDeleteClick = { operationToDelete = operation }
                            )

                            // Разделитель (кроме последнего элемента)
                            if (operation != filteredOperations.last()) {
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp),
                                    color = AppColors.LightGreyColor
                                )
                            }
                        }
                    }
                }
            }
        }

        // Навигационная панель
        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter)
        ) {
            NavigationBar(viewModel)
        }
    }

    // Диалог подтверждения удаления (обновленное оформление)
    if (operationToDelete != null) {
        val operation = operationToDelete!!
        val isIncome = operation.type == "Доход"
        val typeColor = if (isIncome) AppColors.GreenColor else AppColors.RedColor
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        AlertDialog(
            onDismissRequest = { operationToDelete = null },
            title = {
                Text(
                    "Подтверждение удаления",
                    color = AppColors.LightColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Вы уверены, что хотите удалить эту операцию?",
                        color = AppColors.LightColor,
                        fontSize = 14.sp
                    )

                    // Карточка с информацией об операции
                    Surface(
                        color = typeColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Тип и категория
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Иконка типа операции
                                Icon(
                                    imageVector = if (isIncome) FinlyticsIconPack.Income else FinlyticsIconPack.Expenses,
                                    contentDescription = operation.type,
                                    tint = typeColor,
                                    modifier = Modifier.size(20.dp)
                                )

                                Text(
                                    operation.type,
                                    color = typeColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    "•",
                                    color = typeColor.copy(alpha = 0.5f),
                                    fontSize = 12.sp
                                )

                                Text(
                                    operation.category,
                                    color = typeColor,
                                    fontSize = 14.sp
                                )
                            }

                            // Сумма
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    String.format("%.2f", operation.amount),
                                    color = AppColors.LightColor,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "₽",
                                    color = AppColors.LightColor,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Divider(
                                color = typeColor.copy(alpha = 0.15f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Описание (если есть)
                            if (!operation.name.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        operation.name,
                                        color = AppColors.LightColor,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            // Дата
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = FinlyticsIconPack.Date,
                                    contentDescription = "date",
                                    tint = AppColors.LightColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    operation.date.format(dateFormatter),
                                    color = AppColors.LightColor,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    // Предупреждение
                    Surface(
                        color = AppColors.RedColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Это действие нельзя отменить. Операция будет удалена безвозвратно.",
                                color = AppColors.RedColor,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteOperation(operation)
                        operationToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.RedColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Удалить",
                        color = AppColors.LightColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { operationToDelete = null },
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
}

private fun filterOperations(
    operations: List<Operation>,
    filterType: String,
    period: String,
    selectedDate: LocalDate
): List<Operation> {
    // Фильтрация по типу операции
    val filteredByType = when (filterType) {
        "Доходы" -> operations.filter { it.type == "Доход" }
        "Расходы" -> operations.filter { it.type == "Расход" }
        else -> operations // "Все"
    }

    // Фильтрация по периоду времени
    return when (period) {
        "День" -> {
            filteredByType.filter { it.date == selectedDate }
        }
        "Неделя" -> {
            val weekStart = selectedDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            val weekEnd = weekStart.plusDays(6)
            filteredByType.filter {
                val opDate = it.date
                opDate in weekStart..weekEnd
            }
        }
        "Месяц" -> {
            val monthStart = selectedDate.withDayOfMonth(1)
            val monthEnd = selectedDate.with(TemporalAdjusters.lastDayOfMonth())
            filteredByType.filter {
                val opDate = it.date
                opDate in monthStart..monthEnd
            }
        }
        "Год" -> {
            val yearStart = selectedDate.withDayOfYear(1)
            val yearEnd = selectedDate.with(TemporalAdjusters.lastDayOfYear())
            filteredByType.filter {
                val opDate = it.date
                opDate in yearStart..yearEnd
            }
        }
        else -> filteredByType
    }.sortedByDescending { it.date }
}

@Composable
private fun FilterButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    icon: Boolean = false,
    isAll: Boolean = false,
    isIncome: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (!isSelected) AppColors.LightGreyColor
            else if (isAll) AppColors.BlueColor
            else if (isIncome) AppColors.GreenColor
            else AppColors.RedColor
        ),
        elevation = null
    ) {
        if (icon) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isIncome) {
                    Icon(
                        imageVector = FinlyticsIconPack.Income,
                        contentDescription = "income",
                        modifier = Modifier.size(22.dp),
                        tint = AppColors.LightColor
                    )
                } else {
                    Icon(
                        imageVector = FinlyticsIconPack.Expenses,
                        contentDescription = "expenses",
                        modifier = Modifier.size(22.dp),
                        tint = AppColors.LightColor
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = AppColors.LightColor
                )
            }
        } else {
            Text(
                text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = AppColors.LightColor
            )
        }
    }
}

@Composable
private fun PeriodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.defaultMinSize(minWidth = 70.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) AppColors.BlueColor else AppColors.LightGreyColor
        ),
        elevation = null
    ) {
        Text(
            text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = AppColors.LightColor
        )
    }
}

@Composable
private fun TransactionItem(
    operation: Operation,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val dateText = operation.date.format(formatter)
    val isIncome = operation.type == "Доход"
    val typeColor = if (isIncome) AppColors.GreenColor else AppColors.RedColor

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 20.dp)
    ) {
        // Дата слева
        Text(
            text = "[ $dateText ]",
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = AppColors.LightColor,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 0.dp, y = 4.dp)
        )

        // Контент операции
        Column(
            modifier = Modifier
                .width(1120.dp)
                .align(Alignment.CenterStart)
                .offset(x = 150.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Категория с цветным фоном
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(32.dp)
                    .border(
                        width = 1.dp,
                        color = typeColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = typeColor.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = operation.category,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = typeColor
                )
            }

            // Описание
            Text(
                text = if (operation.name == "" || operation.name == null) "Без описания" else operation.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = AppColors.LightColor
            )

            // Сумма
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Иконка плюса/минуса
                Box(
                    modifier = Modifier.size(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isIncome) {
                        Icon(
                            imageVector = FinlyticsIconPack.Plus,
                            contentDescription = "plus",
                            modifier = Modifier.size(14.dp),
                            tint = AppColors.LightColor
                        )
                    } else {
                        Icon(
                            imageVector = FinlyticsIconPack.Minus,
                            contentDescription = "minus",
                            modifier = Modifier.size(14.dp),
                            tint = AppColors.LightColor
                        )
                    }
                }

                Text(
                    text = String.format("%.2f", operation.amount).replace('.', ','),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.LightColor
                )

                // Символ рубля
                Text(
                    text = "₽",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = AppColors.LightColor
                )
            }
        }

        // Кнопки действий справа
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Кнопка редактирования
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.YellowColor, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = FinlyticsIconPack.Edit,
                        contentDescription = "edit",
                        modifier = Modifier.size(50.dp),
                        tint = AppColors.LightColor
                    )
                }
            }

            // Кнопка удаления
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColors.RedColor, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = FinlyticsIconPack.Delete,
                        contentDescription = "delete",
                        modifier = Modifier.size(50.dp),
                        tint = AppColors.LightColor
                    )
                }
            }
        }
    }
}