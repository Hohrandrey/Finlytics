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
import ui.App
import ui.components.FilterSelector
import ui.components.NavigationBar
import ui.components.PieChart
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*
import viewmodel.FinanceViewModel
import kotlin.math.abs

/**
 * Экран "Обзор" - главный экран приложения, отображающий сводку финансов.
 * Переработан в соответствии с дизайном из Figma/CSS файлов.
 */
@Composable
fun OverviewScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Получаем реальные данные для отображения
    val totalIncome = state.totalIncome
    val totalExpenses = state.totalExpenses
    val balance = state.balance
    val expensesByCategory = state.expensesByCategory

    // Подготавливаем список категорий для отображения с процентами
    val categoryList = expensesByCategory.entries.sortedByDescending { it.value }

    // Состояния для фильтров
    var selectedFilter by remember { mutableStateOf("Расходы") }
    var selectedPeriod by remember { mutableStateOf("День") }
    var selectedDate by remember { mutableStateOf("29.09.2025") }

    // Отладочная информация о данных для диаграммы
    LaunchedEffect(expensesByCategory) {
        println("\n=== OVERVIEW SCREEN ===")
        println("Текущий баланс: ${balance} руб.")
        println("Общие доходы: ${totalIncome} руб.")
        println("Общие расходы: ${totalExpenses} руб.")
        println("Количество операций: ${state.operations.size}")
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
                                onClick = { selectedPeriod = "День" }
                            )
                            PeriodButton(
                                text = "Неделя",
                                isSelected = selectedPeriod == "Неделя",
                                onClick = { selectedPeriod = "Неделя" }
                            )
                            PeriodButton(
                                text = "Месяц",
                                isSelected = selectedPeriod == "Месяц",
                                onClick = { selectedPeriod = "Месяц" }
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
                                onClick = { selectedPeriod = "Год" }
                            )
                            PeriodButton(
                                text = "Всё время",
                                isSelected = selectedPeriod == "Всё время",
                                onClick = { selectedPeriod = "Всё время" }
                            )
                            PeriodButton(
                                text = "Интервал",
                                isSelected = selectedPeriod == "Интервал",
                                onClick = { selectedPeriod = "Интервал" }
                            )
                        }
                    }

                    // Настройки даты
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "День",
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
                                    selectedDate,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = AppColors.LightColor
                                )

                                Spacer(Modifier.weight(1f))

                                Row(
                                    modifier = Modifier.padding(end = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { /* Предыдущий день */ },
                                        modifier = Modifier
                                            .background(AppColors.BlueColor, RoundedCornerShape(5.dp))
                                            .size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = FinlyticsIconPack.Left,
                                            contentDescription = "previous_day",
                                            modifier = Modifier.size(20.dp),
                                            tint = AppColors.LightColor
                                        )
                                    }

                                    IconButton(
                                        onClick = { /* Следующий день */ },
                                        modifier = Modifier
                                            .background(AppColors.BlueColor, RoundedCornerShape(5.dp))
                                            .size(20.dp)
                                    ) {
                                        Icon(
                                            imageVector = FinlyticsIconPack.Right,
                                            contentDescription = "next_day",
                                            modifier = Modifier.size(20.dp),
                                            tint = AppColors.LightColor
                                        )
                                    }
                                }
                            }
                        }
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
                    // Диаграмма расходов (только если есть данные)
                    Box(
                        modifier = Modifier
                            .size(650.dp)
                            .align(Alignment.TopCenter)
                    ) {
                        if (expensesByCategory.isNotEmpty()) {
                            PieChart(
                                expensesByCategory,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Нет данных для диаграммы",
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

                // Список категорий расходов (правая часть)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = if (categoryList.isEmpty()) {
                        Arrangement.Center
                    } else {
                        Arrangement.spacedBy(25.dp)
                    }
                ) {
                    if (categoryList.isEmpty()) {
                        // Сообщение об отсутствии данных
                        Text(
                            "Нет данных о расходах",
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
                                    val normalizedPercentage = minOf(percentage, 100.0)
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