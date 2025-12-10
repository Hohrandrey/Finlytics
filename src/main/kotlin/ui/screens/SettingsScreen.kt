package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ui.components.NavigationBar
import viewmodel.FinanceViewModel
import androidx.compose.runtime.LaunchedEffect

/**
 * Экран "Настройки" - управление категориями доходов и расходов.
 * Позволяет просматривать, добавлять и удалять категории.
 *
 * @param viewModel ViewModel с данными категорий
 */
@Composable
fun SettingsScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Отладочная информация о категориях
    LaunchedEffect(state.incomeCategories, state.expenseCategories) {
        println("\n=== SETTINGS SCREEN ===")
        println("Категорий доходов: ${state.incomeCategories.size}")
        println("Категорий расходов: ${state.expenseCategories.size}")
        println("Список категорий доходов: ${state.incomeCategories}")
        println("Список категорий расходов: ${state.expenseCategories}")
        println("========================\n")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        NavigationBar(viewModel)

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text("Управление категориями", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            // Секция категорий доходов
            CategorySection(
                title = "Категории доходов",
                count = state.incomeCategories.size,
                categories = state.incomeCategories,
                onAddClick = { viewModel.showAddCategory(true) },
                onDeleteClick = { category -> viewModel.deleteCategory(category, true) },
                isIncome = true
            )

            Spacer(Modifier.height(24.dp))

            // Секция категорий расходов
            CategorySection(
                title = "Категории расходов",
                count = state.expenseCategories.size,
                categories = state.expenseCategories,
                onAddClick = { viewModel.showAddCategory(false) },
                onDeleteClick = { category -> viewModel.deleteCategory(category, false) },
                isIncome = false
            )
        }
    }
}

/**
 * Отображает секцию с категориями определенного типа (доходы/расходы).
 *
 * @param title Заголовок секции
 * @param count Количество категорий в секции
 * @param categories Список категорий
 * @param onAddClick Обработчик нажатия на кнопку добавления категории
 * @param onDeleteClick Обработчик удаления категории
 * @param isIncome Флаг, указывающий тип категорий (true - доходы, false - расходы)
 */
@Composable
fun CategorySection(
    title: String,
    count: Int,
    categories: List<String>,
    onAddClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
    isIncome: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Text(
                        "Количество: $count",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
                // Кнопка добавления новой категории
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("+ Добавить")
                }
            }

            Spacer(Modifier.height(12.dp))

            if (categories.isEmpty()) {
                Text(
                    "Нет категорий. Нажмите 'Добавить' чтобы создать первую.",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                // Список категорий с возможностью прокрутки
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onDelete = { onDeleteClick(category) },
                            isIncome = isIncome
                        )
                    }
                }
            }
        }
    }
}

/**
 * Отображает одну категорию в списке с кнопкой удаления.
 *
 * @param category Название категории
 * @param onDelete Обработчик удаления категории
 * @param isIncome Флаг типа категории (влияет на цвет фона)
 */
@Composable
fun CategoryItem(
    category: String,
    onDelete: () -> Unit,
    isIncome: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        backgroundColor = if (isIncome)
            MaterialTheme.colors.secondary.copy(alpha = 0.1f)  // Светло-зеленый для доходов
        else
            MaterialTheme.colors.error.copy(alpha = 0.1f),     // Светло-красный для расходов
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.onSurface
            )

            // Кнопка удаления категории
            Button(
                onClick = onDelete,
                modifier = Modifier.width(100.dp).height(32.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.error
                )
            ) {
                Text("Удалить", fontSize = 12.sp, color = Color.White)
            }
        }
    }
}
