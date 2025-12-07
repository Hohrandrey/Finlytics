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

/**
 * Экран "Обзор" - главный экран приложения, отображающий сводку финансов.
 * Показывает баланс, статистику доходов/расходов и диаграмму расходов по категориям.
 *
 * @param viewModel ViewModel с данными для отображения
 */
@Composable
fun OverviewScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        NavigationBar(viewModel)

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Finlytics", fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            // Компонент для выбора периода фильтрации
            FilterSelector(viewModel)

            Spacer(Modifier.height(24.dp))

            // Карточка с балансом и статистикой
            Card(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxWidth()
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
                    Row {
                        Text(
                            "Доходы: ${String.format("%.2f", state.totalIncome)} ₽",
                            modifier = Modifier.weight(1f)
                        )
                        Text("Расходы: ${String.format("%.2f", state.totalExpenses)} ₽")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Расходы по категориям", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(16.dp))

            // Диаграмма распределения расходов
            PieChart(
                state.expensesByCategory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))

            // Кнопка для добавления новой операции
            Button(
                onClick = { viewModel.showAddOperation() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Добавить операцию", fontSize = 16.sp)
            }
        }
    }
}
