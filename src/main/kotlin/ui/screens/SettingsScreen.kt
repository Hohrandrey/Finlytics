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
import ui.components.NavigationBar
import ui.theme.AppColors
import ui.theme.icons.FinlyticsIconPack
import ui.theme.icons.finlyticsiconpack.*
import viewmodel.FinanceViewModel


@Composable
fun SettingsScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

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
                    onDeleteClick = { category -> viewModel.deleteCategory(category, true) },
                    modifier = Modifier.weight(1f)
                )

                // Правая панель - категории расходов
                CategoryPanel(
                    title = "Категории расходов",
                    count = state.expenseCategories.size,
                    categories = state.expenseCategories,
                    onAddClick = { viewModel.showAddCategory(false) },
                    onDeleteClick = { category -> viewModel.deleteCategory(category, false) },
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
}

@Composable
fun CategoryPanel(
    title: String,
    count: Int,
    categories: List<String>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
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
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

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

@Composable
fun CategoryList(
    categories: List<String>,
    onDeleteClick: (String) -> Unit
) {
    val lazyListState = rememberLazyListState()

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
}

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

@Composable
fun EditButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.YellowColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = FinlyticsIconPack.Edit,
            contentDescription = "edit",
            modifier = Modifier.size(28.dp),
            tint = AppColors.LightColor
        )
    }
}

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