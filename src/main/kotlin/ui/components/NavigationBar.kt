package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import viewmodel.FinanceViewModel

/**
 * Панель навигации для переключения между основными экранами приложения.
 * Отображается на всех экранах в верхней части.
 *
 * @param viewModel ViewModel для управления навигацией
 */
@Composable
fun NavigationBar(viewModel: FinanceViewModel) {
    val currentScreen = viewModel.currentScreen

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Кнопка "Обзор"
            NavigationButton(
                text = "Обзор",
                isSelected = currentScreen == "Overview",
                onClick = { viewModel.navigateTo("Overview") }
            )

            // Кнопка "История"
            NavigationButton(
                text = "История",
                isSelected = currentScreen == "History",
                onClick = { viewModel.navigateTo("History") }
            )

            // Кнопка "Настройки"
            NavigationButton(
                text = "Настройки",
                isSelected = currentScreen == "Settings",
                onClick = { viewModel.navigateTo("Settings") }
            )
        }
    }
}

/**
 * Кнопка навигации в панели навигации.
 *
 * @param text Текст на кнопке
 * @param isSelected Флаг, указывающий выбрана ли данная кнопка
 * @param onClick Обработчик нажатия на кнопку
 */
@Composable
fun NavigationButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .widthIn(min = 120.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.primary
            else MaterialTheme.colors.surface
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
