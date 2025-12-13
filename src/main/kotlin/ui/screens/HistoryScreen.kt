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
import java.time.format.DateTimeFormatter
import models.Operation
import viewmodel.FinanceViewModel
import ui.components.NavigationBar
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*

/**
 * Экран "История" - переписанный в соответствии с веб-дизайном
 */
@Composable
fun HistoryScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Отладочная информация об операциях
    LaunchedEffect(state.operations) {
        println("\n=== HISTORY SCREEN ===")
        println("Всего операций: ${state.operations.size}")
        println("Операций доходов: ${state.operations.count { it.type == "Доход" }}")
        println("Операций расходов: ${state.operations.count { it.type == "Расход" }}")
        println("Последние 3 операции:")
        state.operations.take(3).forEach { op ->
            println("  - ${op.type}: ${op.category} - ${op.amount} руб. (${op.date})")
        }
        println("======================\n")
    }

    // Состояния для фильтров
    var selectedFilter by remember { mutableStateOf("Все") }
    var selectedPeriod by remember { mutableStateOf("День") }
    var selectedDate by remember { mutableStateOf("29.09.2025") }
    var operationToDelete by remember { mutableStateOf<Operation?>(null) }

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

            // Основной контент
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 243.dp)
            ) {
                // Список операций
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(state.operations.sortedByDescending { it.date }) { operation ->
                        TransactionItem(
                            operation = operation,
                            onEditClick = { viewModel.showEditOperation(operation) },
                            onDeleteClick = { operationToDelete = operation }
                        )

                        // Разделитель (кроме последнего элемента)
                        if (operation != state.operations.sortedByDescending { it.date }.last()) {
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

        // Навигационная панель
        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(Alignment.BottomCenter)
        ) {
            NavigationBar(viewModel)
        }
    }

    // Диалог подтверждения удаления
    if (operationToDelete != null) {
        AlertDialog(
            onDismissRequest = { operationToDelete = null },
            title = { Text("Подтверждение удаления") },
            text = {
                Text("Вы уверены, что хотите удалить операцию?\n" +
                        "${operationToDelete!!.category} - " +
                        "${String.format("%.2f", operationToDelete!!.amount)} ₽")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteOperation(operationToDelete!!)
                        operationToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.RedColor)
                ) {
                    Text("Удалить", color = AppColors.LightColor)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { operationToDelete = null }
                ) {
                    Text("Отмена", color = AppColors.LightColor)
                }
            }
        )
    }
}

@Composable
fun FilterButton(
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
fun PeriodButton(
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
fun TransactionItem(
    operation: Operation,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val dateText = operation.date.format(formatter)

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
                        color = if (operation.type == "Доход") AppColors.GreenColor else AppColors.RedColor,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color = if (operation.type == "Доход")
                            AppColors.GreenColor.copy(alpha = 0.25f) // green с прозрачностью 40% 66
                        else
                            AppColors.RedColor.copy(alpha = 0.25f), // red с прозрачностью 40%
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = operation.category,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (operation.type == "Доход") AppColors.GreenColor else AppColors.RedColor
                )
            }

            // Описание
            Text(
                text = operation.name ?: "Без описания",
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
                    if (operation.type == "Доход") {
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