package ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import viewmodel.FinanceViewModel

/**
 * Главный компонент приложения, устанавливающий тему и стили.
 * Использует темную тему с фирменными цветами приложения Finlytics.
 *
 * @param viewModel ViewModel для управления состоянием приложения
 */
@Composable
fun App(viewModel: FinanceViewModel) {
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFF2176FF),        // Основной синий цвет
            primaryVariant = Color(0xFF1E88E5), // Темный вариант синего
            secondary = Color(0xFF4ADE80),      // Зеленый для доходов
            background = Color(0xFF121212),     // Темный фон
            surface = Color(0xFF1E1E1E),        // Цвет поверхностей (карточек)
            onPrimary = Color.White,                   // Текст на синем фоне
            onSecondary = Color.Black,                 // Текст на зеленом фоне
            onBackground = Color.White,                // Основной текст
            onSurface = Color.White                    // Текст на поверхностях
        )
    ) {
        AppNavigation(viewModel)
    }
}
