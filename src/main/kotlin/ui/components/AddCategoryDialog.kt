package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppColors
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
    val categoryColor = if (viewModel.isIncomeCategory) AppColors.GreenColor else AppColors.RedColor

    AlertDialog(
        onDismissRequest = {
            viewModel.hideCategoryDialog()
            categoryName = ""
            error = ""
        },
        title = {
            Text(
                "Добавить категорию $categoryType",
                color = AppColors.LightColor,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Пояснительный текст
                Text(
                    "Введите название новой категории:",
                    color = AppColors.LightColor.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )

                // Поле ввода
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                        error = ""
                    },
                    label = {
                        Text(
                            "Название категории",
                            color = if (error.isNotEmpty()) AppColors.RedColor else AppColors.LightColor.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = error.isNotEmpty(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = categoryColor,
                        unfocusedBorderColor = AppColors.LightGreyColor,
                        cursorColor = categoryColor,
                        textColor = AppColors.LightColor
                    )
                )

                // Сообщение об ошибке
                if (error.isNotEmpty()) {
                    Surface(
                        color = AppColors.RedColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = error,
                            color = AppColors.RedColor,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                // Подсказка
                Text(
                    "Название должно быть уникальным и не может быть пустым",
                    color = AppColors.LightColor.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
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
                colors = ButtonDefaults.buttonColors(backgroundColor = categoryColor),
                enabled = categoryName.isNotEmpty(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Добавить",
                    color = AppColors.LightColor,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.hideCategoryDialog()
                    categoryName = ""
                    error = ""
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = AppColors.LightGreyColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Отмена",
                    color = AppColors.LightColor
                )
            }
        },
        backgroundColor = AppColors.DarkGreyColor,
        shape = RoundedCornerShape(16.dp)
    )
}