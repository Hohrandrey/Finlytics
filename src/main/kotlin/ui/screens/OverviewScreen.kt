package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ui.components.FilterSelector
import ui.components.NavigationBar
import ui.components.PieChart
import viewmodel.FinanceViewModel
import androidx.compose.runtime.LaunchedEffect
import javax.swing.Box

/**
 * Экран "Обзор" - главный экран приложения, отображающий сводку финансов.
 * Показывает баланс, статистику доходов/расходов и диаграмму расходов по категориям.
 *
 * @param viewModel ViewModel с данными для отображения
 */
@Composable
fun OverviewScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    // Отладочная информация о данных для диаграммы
    LaunchedEffect(state.expensesByCategory) {
        println("\n=== OVERVIEW SCREEN ===")
        println("Текущий баланс: ${state.balance} руб.")
        println("Общие доходы: ${state.totalIncome} руб.")
        println("Общие расходы: ${state.totalExpenses} руб.")
        println("Количество операций: ${state.operations.size}")
        println("Категорий расходов для диаграммы: ${state.expensesByCategory.size}")
        println("Данные для диаграммы: ${state.expensesByCategory}")
        println("========================\n")
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Finlytics", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            FilterSelector(viewModel)

            Spacer(Modifier.height(24.dp))

            // Карточка с балансом и статистикой
            Card(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Баланс", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = String.format("%.2f ₽", state.balance),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.balance >= 0) MaterialTheme.colors.secondary
                        else MaterialTheme.colors.error
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Доходы: ${String.format("%.2f", state.totalIncome)} ₽",
                            color = MaterialTheme.colors.secondary
                        )
                        Text(
                            "Расходы: ${String.format("%.2f", state.totalExpenses)} ₽",
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Расходы по категориям",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.onSurface  // Явно указываем цвет текста
            )
            Spacer(Modifier.height(16.dp))

            // Диаграмма распределения расходов
            Card(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                elevation = 4.dp
            ) {
                PieChart(
                    state.expensesByCategory,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.showAddOperation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
            ) {
                Text("Добавить операцию", fontSize = 16.sp, color = MaterialTheme.colors.onPrimary)
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
