package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.AddCategoryDialog
import ui.components.NavigationBar
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*
import viewmodel.FinanceViewModel

/**
 * Экран настроек приложения.
 * Позволяет управлять категориями доходов и расходов.
 *
 * Основные функции:
 * - Просмотр списка категорий доходов и расходов
 * - Добавление новых категорий через диалоговое окно
 * - Удаление категорий с подтверждением
 * - Проверка наличия связанных операций перед удалением
 *
 * @param viewModel ViewModel для управления данными и навигацией
 */
@Composable
fun SettingsScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Состояния для диалогов
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<Pair<String, Boolean>?>(null) }

    /**
     * Обработчик удаления категории.
     * Открывает диалог подтверждения с проверкой наличия связанных операций.
     *
     * @param category Название категории для удаления
     * @param isIncome true для категории доходов, false для категории расходов
     */
    val onDeleteCategory = { category: String, isIncome: Boolean ->
        categoryToDelete = Pair(category, isIncome)
        showDeleteConfirmation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.DarkColor)
    ) {
        // Основной контент
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp, bottom = 124.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 55.dp)
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                // Левая панель - категории доходов
                CategoryPanel(
                    title = "Категории доходов",
                    count = state.incomeCategories.size,
                    categories = state.incomeCategories,
                    onAddClick = { viewModel.showAddCategory(true) },
                    onDeleteClick = { category -> onDeleteCategory(category, true) },
                    isIncome = true,
                    modifier = Modifier.weight(1f)
                )

                // Правая панель - категории расходов
                CategoryPanel(
                    title = "Категории расходов",
                    count = state.expenseCategories.size,
                    categories = state.expenseCategories,
                    onAddClick = { viewModel.showAddCategory(false) },
                    onDeleteClick = { category -> onDeleteCategory(category, false) },
                    isIncome = false,
                    modifier = Modifier.weight(1f)
                )
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

    // Диалог добавления категории
    if (viewModel.showAddCategoryDialog) {
        AddCategoryDialog(viewModel)
    }

    // Диалог подтверждения удаления категории
    if (showDeleteConfirmation && categoryToDelete != null) {
        val (categoryName, isIncome) = categoryToDelete!!
        val categoryType = if (isIncome) "доходов" else "расходов"
        val categoryColor = if (isIncome) AppColors.GreenColor else AppColors.RedColor

        // Проверяем, есть ли операции с этой категорией
        val hasOperations = if (isIncome) {
            state.operations.any { it.type == "Доход" && it.category == categoryName }
        } else {
            state.operations.any { it.type == "Расход" && it.category == categoryName }
        }

        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
                categoryToDelete = null
            },
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Вы уверены, что хотите удалить категорию?",
                        color = AppColors.LightColor,
                        fontSize = 14.sp
                    )

                    // Выделенная категория
                    Surface(
                        color = categoryColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "\"$categoryName\"",
                            color = categoryColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    if (hasOperations) {
                        // Предупреждение о наличии операций
                        Surface(
                            color = AppColors.RedColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    "⚠️ Внимание!",
                                    color = AppColors.RedColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Существуют операции с этой категорией. " +
                                            "Удаление категории невозможно, пока есть связанные операции.",
                                    color = AppColors.RedColor,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            "Эта категория не используется ни в одной операции и может быть безопасно удалена.",
                            color = AppColors.LightColor.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (!hasOperations) {
                            viewModel.deleteCategory(categoryName, isIncome)
                        }
                        showDeleteConfirmation = false
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (hasOperations) AppColors.LightGreyColor else AppColors.RedColor
                    ),
                    enabled = !hasOperations,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Удалить",
                        color = if (hasOperations) AppColors.LightColor.copy(alpha = 0.5f) else AppColors.LightColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.LightGreyColor),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Отмена", color = AppColors.LightColor)
                }
            },
            backgroundColor = AppColors.DarkGreyColor,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

/**
 * Панель управления категориями определенного типа (доходы или расходы).
 *
 * Отображает заголовок с количеством категорий, кнопку добавления
 * и список категорий с возможностью удаления каждой.
 *
 * @param title Заголовок панели
 * @param count Количество категорий
 * @param categories Список названий категорий
 * @param onAddClick Callback при нажатии на кнопку добавления
 * @param onDeleteClick Callback при удалении категории (передаётся название категории)
 * @param isIncome Тип категории (для цветового оформления)
 * @param modifier Модификатор для настройки внешнего вида
 */
@Composable
fun CategoryPanel(
    title: String,
    count: Int,
    categories: List<String>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
    isIncome: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(40.dp))
            .background(AppColors.DarkGreyColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
        ) {
            // Заголовок панели
            PanelHeader(
                title = title,
                count = count,
                onAddClick = onAddClick
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Список категорий
            if (categories.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нет категорий",
                        color = AppColors.LightColor.copy(alpha = 0.5f),
                        fontSize = 16.sp
                    )
                }
            } else {
                CategoryList(
                    categories = categories,
                    onDeleteClick = onDeleteClick,
                    isIncome = isIncome
                )
            }
        }
    }
}

/**
 * Заголовок панели категорий.
 *
 * Отображает название панели, счётчик категорий и кнопку добавления.
 *
 * @param title Название панели
 * @param count Количество категорий
 * @param onAddClick Callback при нажатии на кнопку добавления
 */
@Composable
fun PanelHeader(
    title: String,
    count: Int,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Text(
                text = title,
                color = AppColors.LightColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )

            // Счетчик категорий
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.LightGreyColor)
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    color = AppColors.LightColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.sp
                )
            }
        }

        // Кнопка добавления
        AddButton(onClick = onAddClick)
    }
}

/**
 * Кнопка добавления категории.
 *
 * Отображает синюю кнопку с иконкой плюса.
 *
 * @param onClick Callback при нажатии на кнопку
 */
@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.BlueColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = FinlyticsIconPack.Add,
            contentDescription = "add",
            modifier = Modifier.size(28.dp),
            tint = AppColors.LightColor
        )
    }
}

/**
 * Список категорий с поддержкой прокрутки.
 *
 * Отображает список категорий с возможностью удаления каждой.
 *
 * @param categories Список названий категорий
 * @param onDeleteClick Callback при удалении категории
 * @param isIncome Тип категории (для определения цвета)
 */
@Composable
fun CategoryList(
    categories: List<String>,
    onDeleteClick: (String) -> Unit,
    isIncome: Boolean
) {
    val lazyListState = rememberLazyListState()
    val categoryColor = if (isIncome) AppColors.GreenColor else AppColors.RedColor

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                onDeleteClick = { onDeleteClick(category) },
                categoryColor = categoryColor
            )
        }
    }
}

/**
 * Элемент списка категорий.
 *
 * Отображает название категории и кнопку удаления.
 *
 * @param category Название категории
 * @param onDeleteClick Callback при нажатии на кнопку удаления
 * @param categoryColor Цвет для выделения категории (зелёный для доходов, красный для расходов)
 */
@Composable
fun CategoryItem(
    category: String,
    onDeleteClick: () -> Unit,
    categoryColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        // Фон элемента
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp))
                .background(AppColors.LightGreyColor)
        )

        // Название категории
        Text(
            text = category,
            color = AppColors.LightColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 25.dp)
        )

        // Кнопка удаления (справа)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 15.dp)
        ) {
            DeleteButton(onClick = onDeleteClick)
        }
    }
}

/**
 * Кнопка удаления категории.
 *
 * Отображает красную кнопку с иконкой корзины.
 *
 * @param onClick Callback при нажатии на кнопку
 */
@Composable
fun DeleteButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.RedColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = FinlyticsIconPack.Delete,
            contentDescription = "delete",
            modifier = Modifier.size(28.dp),
            tint = AppColors.LightColor
        )
    }
}