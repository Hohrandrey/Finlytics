package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ui.components.NavigationBar
import viewmodel.FinanceViewModel
import java.time.format.DateTimeFormatter

/**
 * Экран "История" - отображает список всех финансовых операций.
 * Операции сортируются по дате (сначала новые) и показываются в виде карточек.
 *
 * @param viewModel ViewModel с данными операций
 */
@Composable
fun HistoryScreen(viewModel: FinanceViewModel) {
    val state by viewModel.state.collectAsState()
    // Форматтер для отображения даты в удобном формате
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Column(modifier = Modifier.fillMaxSize()) {
        NavigationBar(viewModel)

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text("История операций", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            // Ленивый список для эффективного отображения большого числа операций
            LazyColumn {
                items(state.operations.sortedByDescending { it.date }) { op ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        backgroundColor = MaterialTheme.colors.surface
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(op.category, fontWeight = FontWeight.SemiBold)
                                Text(op.date.format(formatter), fontSize = 12.sp)
                            }
                            Text(
                                text = if (op.type == "Доход") "+${op.amount} ₽" else "-${op.amount} ₽",
                                color = if (op.type == "Доход") MaterialTheme.colors.secondary
                                else MaterialTheme.colors.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
