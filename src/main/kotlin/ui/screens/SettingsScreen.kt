package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodel.FinanceViewModel

@Composable
fun SettingsScreen(viewModel: FinanceViewModel) {
    val state = viewModel.state.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            Button(onClick = { viewModel.currentScreen = "Overview" }) { Text("Обзор") }
            Spacer(Modifier.width(12.dp))
            Button(onClick = { viewModel.currentScreen = "History" }) { Text("История") }
        }

        Spacer(Modifier.height(24.dp))
        Text("Категории", fontSize = 24.sp, fontWeight = MaterialTheme.typography.h5.fontWeight)

        Spacer(Modifier.height(16.dp))

        // Доходы
        Text("Доходы", fontWeight = FontWeight.SemiBold)
        LazyColumn {
            items(state.incomeCategories) { cat ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(cat, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.deleteCategory(cat, true) }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
        Button(onClick = { viewModel.showAddCategory(true) }) {
            Text("Добавить категорию доходов")
        }

        Spacer(Modifier.height(24.dp))

        // Расходы
        Text("Расходы", fontWeight = FontWeight.SemiBold)
        LazyColumn {
            items(state.expenseCategories) { cat ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(cat, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.deleteCategory(cat, false) }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            }
        }
        Button(onClick = { viewModel.showAddCategory(false) }) {
            Text("Добавить категорию расходов")
        }
    }
}