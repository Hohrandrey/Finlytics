package ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.NavigationBar
import viewmodel.FinanceViewModel

// Цветовая схема из styleguide.css
val DarkColor = Color(0xFF1E1E1E)
val DarkGreyColor = Color(0xFF2C2C2C)
val LightGreyColor = Color(0xFF444444)
val LightColor = Color(0xFFF4F4F4)
val BlueColor = Color(0xFF2176FF)
val RedColor = Color(0xFFEF4444)
val GreenColor = Color(0xFF4ADE80)
val YellowColor = Color(0xFFECB324)
val PurpleColor = Color(0xFF7300FF)
val RoseColor = Color(0xFFFF00A6)
val EmeraldColor = Color(0xFF00D9FB)
val LimeColor = Color(0xFFEAFF00)

/**
 * Экран "Настройки" в новом дизайне - управление категориями доходов и расходов.
 */
@Composable
fun SettingsScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkColor)
    ) {
        // Основной контент
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 74.dp) // Место для навигационной панели
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
                    onDeleteClick = { category -> viewModel.deleteCategory(category, true) },
                    modifier = Modifier.weight(1f),
                    isScrollable = true
                )

                // Правая панель - категории расходов
                CategoryPanel(
                    title = "Категории расходов",
                    count = state.expenseCategories.size,
                    categories = state.expenseCategories,
                    onAddClick = { viewModel.showAddCategory(false) },
                    onDeleteClick = { category -> viewModel.deleteCategory(category, false) },
                    modifier = Modifier.weight(1f),
                    isScrollable = true
                )
            }
        }

        // Навигационная панель
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-50).dp)
        ) {
            NavigationBar(viewModel)
        }
    }
}

/**
 * Панель категорий с дизайном из макета.
 *
 * @param title Заголовок панели
 * @param count Количество категорий
 * @param categories Список категорий
 * @param onAddClick Обработчик добавления
 * @param onDeleteClick Обработчик удаления
 * @param isScrollable Флаг прокрутки списка
 */
@Composable
fun CategoryPanel(
    title: String,
    count: Int,
    categories: List<String>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(40.dp))
            .background(DarkGreyColor)
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
                        color = LightColor.copy(alpha = 0.5f),
                        fontSize = 16.sp
                    )
                }
            } else {
                CategoryList(
                    categories = categories,
                    onDeleteClick = onDeleteClick,
                    isScrollable = isScrollable
                )
            }
        }
    }
}

/**
 * Заголовок панели категорий.
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
                color = LightColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )

            // Счетчик категорий
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(LightGreyColor)
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    color = LightColor,
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
 * Кнопка добавления новой категории.
 */
@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(BlueColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Иконка плюса (вертикальная линия)
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(20.dp)
                .background(LightColor)
        )

        // Иконка плюса (горизонтальная линия)
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(3.dp)
                .background(LightColor)
        )
    }
}

/**
 * Список категорий с возможностью прокрутки.
 */
@Composable
fun CategoryList(
    categories: List<String>,
    onDeleteClick: (String) -> Unit,
    isScrollable: Boolean
) {
    val scrollState = rememberScrollState()
    val lazyListState = rememberLazyListState()

    if (isScrollable) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    onDeleteClick = { onDeleteClick(category) }
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            categories.forEach { category ->
                CategoryItem(
                    category = category,
                    onDeleteClick = { onDeleteClick(category) }
                )
            }
        }
    }
}

/**
 * Элемент категории в списке.
 */
@Composable
fun CategoryItem(
    category: String,
    onDeleteClick: () -> Unit
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
                .background(LightGreyColor)
        )

        // Название категории
        Text(
            text = category,
            color = LightColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 25.dp)
        )

        // Кнопки действий
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 25.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка редактирования (заглушка)
            EditButton(onClick = { /* TODO: Реализовать редактирование */ })

            // Кнопка удаления
            DeleteButton(onClick = onDeleteClick)
        }
    }
}

/**
 * Кнопка редактирования категории.
 */
@Composable
fun EditButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Иконка редактирования (упрощенная версия)
        Box(
            modifier = Modifier
                .size(17.dp)
        ) {
            // Карандаш - прямоугольник с диагональной линией
            Box(
                modifier = Modifier
                    .size(12.dp, 10.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .border(
                        width = 1.dp,
                        color = LightColor.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(1.dp)
                    )
            )

            // Диагональная линия карандаша
            Box(
                modifier = Modifier
                    .width(7.dp)
                    .height(1.dp)
                    .background(LightColor.copy(alpha = 0.7f))
                    .rotate(45f)
                    .offset(x = 8.dp, y = 2.dp)
            )
        }
    }
}

/**
 * Кнопка удаления категории.
 */
@Composable
fun DeleteButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Иконка корзины (упрощенная версия)
        Column(
            modifier = Modifier.size(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Верхняя часть корзины (ручка)
            Box(
                modifier = Modifier
                    .width(13.dp)
                    .height(2.dp)
                    .background(LightColor.copy(alpha = 0.7f))
            )

            Spacer(modifier = Modifier.height(1.dp))

            // Основная часть корзины
            Box(
                modifier = Modifier
                    .width(15.dp)
                    .height(11.dp)
            ) {
                // Боковые стороны корзины
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = LightColor.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp)
                        )
                )

                // Горизонтальные линии в корзине
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(LightColor.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun Modifier.rotate(degrees: Float): Modifier = this.graphicsLayer {
    rotationZ = degrees
}