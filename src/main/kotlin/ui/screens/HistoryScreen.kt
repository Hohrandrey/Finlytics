package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ui.components.NavigationBar
import viewmodel.FinanceViewModel
import java.time.format.DateTimeFormatter
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import models.Operation
import androidx.compose.runtime.LaunchedEffect

/**
 * Экран "История" - отображает список всех финансовых операций.
 * Операции сортируются по дате (сначала новые) и показываются в виде карточек.
 *
 * @param viewModel ViewModel с данными операций
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

    // Форматтер для отображения даты в удобном формате
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    // Состояние для хранения операции, которую пользователь хочет удалить
    var operationToDelete by remember { mutableStateOf<models.Operation?>(null) }

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
                        backgroundColor = MaterialTheme.colors.surface,
                        elevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Информация об операции
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        op.category,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (op.type == "Доход")
                                            MaterialTheme.colors.secondary
                                        else
                                            MaterialTheme.colors.error
                                    )
                                    Text(
                                        text = if (op.type == "Доход") "+${String.format("%.2f", op.amount)} ₽"
                                        else "-${String.format("%.2f", op.amount)} ₽",
                                        color = if (op.type == "Доход") MaterialTheme.colors.secondary
                                        else MaterialTheme.colors.error,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(op.date.format(formatter), fontSize = 12.sp)
                            }

                            // Кнопки действий
                            Row {
                                // Кнопка редактирования
                                IconButton(
                                    onClick = { viewModel.showEditOperation(op) },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Редактировать",
                                        tint = MaterialTheme.colors.primary
                                    )
                                }

                                // Кнопка удаления
                                IconButton(
                                    onClick = { operationToDelete = op },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Удалить",
                                        tint = MaterialTheme.colors.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
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
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                ) {
                    Text("Удалить", color = MaterialTheme.colors.onError)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { operationToDelete = null }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}
