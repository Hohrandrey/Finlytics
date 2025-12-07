package ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.components.NavigationBar
import viewmodel.FinanceViewModel

@Composable
fun SettingsScreen(viewModel: FinanceViewModel) {
    val state = viewModel.state.value

    Column(modifier = Modifier.fillMaxSize()) {
        NavigationBar(viewModel)

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text("Настройки категорий", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Категории доходов", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))

                    if (state.incomeCategories.isEmpty()) {
                        Text("Нет категорий", style = MaterialTheme.typography.caption)
                    } else {
                        LazyColumn {
                            items(state.incomeCategories) { cat ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(cat, modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = { viewModel.deleteCategory(cat, true) },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = MaterialTheme.colors.error
                                        )
                                    ) {
                                        Text("Удалить", color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.showAddCategory(true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Добавить категорию доходов")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Категории расходов", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))

                    if (state.expenseCategories.isEmpty()) {
                        Text("Нет категорий", style = MaterialTheme.typography.caption)
                    } else {
                        LazyColumn {
                            items(state.expenseCategories) { cat ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(cat, modifier = Modifier.weight(1f))
                                    Button(
                                        onClick = { viewModel.deleteCategory(cat, false) },
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = MaterialTheme.colors.error
                                        )
                                    ) {
                                        Text("Удалить", color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.showAddCategory(false) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Добавить категорию расходов")
                    }
                }
            }
        }
    }
}
