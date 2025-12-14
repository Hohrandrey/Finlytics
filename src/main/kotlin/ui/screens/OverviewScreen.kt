package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import ui.components.NavigationBar
import ui.components.PieChart
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*
import viewmodel.FinanceViewModel
import kotlin.math.min

/**
 * Экран "Обзор" - главный экран приложения, отображающий сводку финансов.
 * Переработан в соответствии с дизайном из Figma/CSS файлов.
 */
@Composable
fun OverviewScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Состояния для фильтров
    var selectedFilter by remember { mutableStateOf("Расходы") } // По умолчанию показываем расходы
    var selectedPeriod by remember { mutableStateOf("Всё время") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

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

    // Вычисляем статистику на основе отфильтрованных операций
    val totalIncome = filteredOperations.filter { it.type == "Доход" }.sumOf { it.amount }
    val totalExpenses = filteredOperations.filter { it.type == "Расход" }.sumOf { it.amount }
    val balance = totalIncome - totalExpenses

    // Группируем расходы по категориям для диаграммы
    val expensesByCategory = if (selectedFilter == "Расходы" || selectedFilter == "Все") {
        filteredOperations
            .filter { it.type == "Расход" }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { op -> op.amount } }
    } else {
        emptyMap()
    }

    // Подготавливаем список категорий для отображения с процентами
    val categoryList = expensesByCategory.entries.sortedByDescending { it.value }

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

    // Отладочная информация о данных для диаграммы
    LaunchedEffect(filteredOperations, expensesByCategory) {
        println("\n=== OVERVIEW SCREEN ===")
        println("Фильтр: $selectedFilter, Период: $selectedPeriod")
        println("Всего операций: ${state.operations.size}")
        println("Отфильтровано операций: ${filteredOperations.size}")
        println("Текущий баланс: ${balance} руб.")
        println("Общие доходы: ${totalIncome} руб.")
        println("Общие расходы: ${totalExpenses} руб.")
        println("Категорий расходов для диаграммы: ${expensesByCategory.size}")
        println("Данные для диаграммы: $expensesByCategory")
        println("========================\n")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.DarkColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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

                        // Кнопка "Доходы"
                        FilterButton(
                            text = "Доходы",
                            isSelected = selectedFilter == "Доходы",
                            icon = true,
                            modifier = Modifier.width(390.dp).height(42.dp),
                            onClick = { selectedFilter = "Доходы" }
                        )

                        // Кнопка "Расходы"
                        FilterButton(
                            text = "Расходы",
                            isSelected = selectedFilter == "Расходы",
                            icon = true,
                            isIncome = false,
                            modifier = Modifier.width(390.dp).height(42.dp),
                            onClick = { selectedFilter = "Расходы" }
                        )
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
                            modifier = Modifier.width(390.dp),
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
                            modifier = Modifier.width(390.dp),
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
                            PeriodButton(
                                text = "Интервал",
                                isSelected = selectedPeriod == "Интервал",
                                onClick = {
                                    selectedPeriod = "Интервал"
                                    selectedDate = LocalDate.now()
                                }
                            )
                        }
                    }

                    // Настройки даты (скрываем для "Всё время")
                    if (selectedPeriod != "Всё время") {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                selectedPeriod, // Подпись соответствует выбранному периоду
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

                                    // Кнопки навигации показываем только для День/Неделя/Месяц/Год
                                    if (selectedPeriod != "Интервал") {
                                        Row(
                                            modifier = Modifier.padding(end = 6.dp),
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    // Предыдущий период
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
                                                    // Следующий период
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
                        }
                    } else {
                        // Пустой колонка для выравнивания, когда поле даты скрыто
                        Spacer(modifier = Modifier.width(390.dp))
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            // Основной контент: диаграмма и статистика (аналог data из CSS)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(90.dp)
            ) {
                // Диаграмма и статистика (левая часть)
                Box(
                    modifier = Modifier.size(650.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    // Диаграмма (показываем только для фильтра "Расходы")
                    Box(
                        modifier = Modifier
                            .size(650.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        if (selectedFilter == "Расходы" && expensesByCategory.isNotEmpty()) {
                            PieChart(
                                expensesByCategory,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp)
                            )
                        } else if (selectedFilter == "Расходы") {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    if (filteredOperations.isEmpty()) "Нет данных" else "Нет расходов за выбранный период",
                                    fontSize = 20.sp,
                                    color = AppColors.LightColor.copy(alpha = 0.5f)
                                )
                            }
                        } else {
                            // Для фильтра "Доходы" показываем сообщение
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Диаграмма доступна только для расходов",
                                    fontSize = 20.sp,
                                    color = AppColors.LightColor.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }

                    // Блок статистики (доходы/расходы/баланс)
                    Column(
                        modifier = Modifier
                            .width(280.dp)
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(25.dp)
                    ) {
                        // Доходы
                        Surface(
                            color = AppColors.GreenColor.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(2.dp, AppColors.GreenColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(90.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    // Иконка доходов
                                    Icon(
                                        imageVector = FinlyticsIconPack.Income,
                                        contentDescription = "income",
                                        modifier = Modifier.size(22.dp),
                                        tint = AppColors.GreenColor
                                    )
                                    Text(
                                        "Доходы",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AppColors.GreenColor
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        String.format("%.2f", totalIncome),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.GreenColor
                                    )
                                    // Иконка валюты
                                    Text(
                                        text = "₽",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.GreenColor
                                    )
                                }
                            }
                        }

                        // Расходы
                        Surface(
                            color = AppColors.RedColor.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(2.dp, AppColors.RedColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(90.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    // Иконка расходов
                                    Icon(
                                        imageVector = FinlyticsIconPack.Expenses,
                                        contentDescription = "expenses",
                                        modifier = Modifier.size(22.dp),
                                        tint = AppColors.RedColor
                                    )
                                    Text(
                                        "Расходы",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AppColors.RedColor
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        String.format("%.2f", totalExpenses),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.RedColor
                                    )
                                    // Иконка валюты
                                    Text(
                                        text = "₽",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.RedColor
                                    )
                                }
                            }
                        }

                        // Баланс
                        Surface(
                            color = AppColors.YellowColor.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(2.dp, AppColors.YellowColor)
                        ) {
                            Column(
                                modifier = Modifier
                                    .height(90.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    // Иконка кошелька
                                    Icon(
                                        imageVector = FinlyticsIconPack.Wallet,
                                        contentDescription = "wallet",
                                        modifier = Modifier.size(20.dp),
                                        tint = AppColors.YellowColor
                                    )
                                    Text(
                                        "Баланс",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = AppColors.YellowColor
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Spacer(Modifier.weight(1f))
                                    Text(
                                        String.format("%.2f", balance),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.YellowColor
                                    )
                                    // Иконка валюты
                                    Text(
                                        text = "₽",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.YellowColor
                                    )
                                }
                            }
                        }
                    }
                }

                // Список категорий расходов (правая часть) - показываем только для фильтра "Расходы"
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = if (selectedFilter != "Расходы" || categoryList.isEmpty()) {
                        Arrangement.Center
                    } else {
                        Arrangement.spacedBy(25.dp)
                    }
                ) {
                    if (selectedFilter != "Расходы") {
                        // Сообщение для фильтра "Доходы"
                        Text(
                            "Список категорий доступен только для расходов",
                            fontSize = 20.sp,
                            color = AppColors.LightColor.copy(alpha = 0.5f),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else if (categoryList.isEmpty()) {
                        // Сообщение об отсутствии данных
                        Text(
                            if (filteredOperations.isEmpty()) "Нет данных" else "Нет расходов за выбранный период",
                            fontSize = 20.sp,
                            color = AppColors.LightColor.copy(alpha = 0.5f),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        // Отображаем реальные категории расходов

                        // Карта градиентов для прогресс-баров (соответствует CSS)
                        val gradients = listOf(
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.BlueColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.EmeraldColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.GreenColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.LimeColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.YellowColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.RedColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.RoseColor)),
                            Brush.horizontalGradient(listOf(AppColors.DarkColor, AppColors.PurpleColor))
                        )

                        categoryList.forEachIndexed { index, (category, amount) ->
                            // Вычисляем процент от общей суммы расходов
                            val percentage = if (totalExpenses > 0) {
                                (amount / totalExpenses) * 100
                            } else {
                                0.0
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(67.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Название категории
                                    Text(
                                        category,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.LightColor
                                    )

                                    Spacer(Modifier.weight(1f))

                                    // Сумма
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            String.format("%.2f", amount),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = AppColors.LightColor
                                        )
                                        // Иконка валюты
                                        Text(
                                            text = "₽",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = AppColors.LightColor
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                Box(modifier = Modifier.height(35.dp)) {
                                    // Прогресс-бар с реальным процентом
                                    // Используем minOf чтобы процент не превышал 100%
                                    val normalizedPercentage = min(percentage, 100.0)
                                    val progressWidth = normalizedPercentage / 100.0

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(35.dp)
                                            .background(
                                                AppColors.DarkColor,
                                                shape = RoundedCornerShape(9.dp)
                                            )
                                    ) {
                                        // Градиентная часть прогресс-бара
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .fillMaxWidth(fraction = progressWidth.toFloat())
                                                .height(35.dp)
                                                .background(
                                                    gradients.getOrElse(index) { gradients.last() },
                                                    shape = RoundedCornerShape(9.dp)
                                                )
                                        )
                                    }

                                    // Процент
                                    Text(
                                        String.format("%.2f", percentage) + " %",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = AppColors.LightColor,
                                        modifier = Modifier
                                            .align(Alignment.CenterEnd)
                                            .padding(end = 8.dp)
                                    )
                                }
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
}

/**
 * Функция фильтрации операций по типу и периоду (такая же как в HistoryScreen)
 */
private fun filterOperations(
    operations: List<models.Operation>,
    filterType: String,
    period: String,
    selectedDate: LocalDate
): List<models.Operation> {
    // 1. Фильтрация по типу операции
    val filteredByType = when (filterType) {
        "Доходы" -> operations.filter { it.type == "Доход" }
        "Расходы" -> operations.filter { it.type == "Расход" }
        else -> operations // "Все" - хотя на главной странице нет такого фильтра
    }

    // 2. Фильтрация по периоду времени
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
        "Интервал" -> {
            // Для интервала используем selectedDate как начальную дату
            // В реальном приложении нужно добавить выбор конечной даты
            // Показываем операции только за выбранный день
            filteredByType.filter { it.date == selectedDate }
        }
        else -> filteredByType // "Всё время"
    }.sortedByDescending { it.date } // Сортируем по дате (новые сверху)
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