package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.FinanceViewModel

/**
 * Диалоговое окно для добавления новой категории (доходов или расходов).
 * Позволяет пользователю ввести название новой категории.
 *
 * @param viewModel ViewModel для управления категориями
 */
@Composable
fun AddCategoryDialog(viewModel: FinanceViewModel) {
    var categoryName by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // Определяем тип категории для отображения в заголовке
    val categoryType = if (viewModel.isIncomeCategory) "доходов" else "расходов"

    AlertDialog(
        onDismissRequest = {
            viewModel.hideCategoryDialog()
            categoryName = ""
            error = ""
        },
        title = { Text("Добавить категорию $categoryType") },
        text = {
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                        error = ""
                    },
                    label = { Text("Название категории") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = error.isNotEmpty()
                )

                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Валидация: проверяем, что название не пустое
                    if (categoryName.trim().isEmpty()) {
                        error = "Введите название категории"
                        return@Button
                    }

                    // Сохраняем категорию через ViewModel
                    viewModel.saveCategory(categoryName.trim())
                    categoryName = ""
                },
                enabled = categoryName.isNotEmpty()
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.hideCategoryDialog()
                    categoryName = ""
                    error = ""
                }
            ) {
                Text("Отмена")
            }
        }
    )
}
