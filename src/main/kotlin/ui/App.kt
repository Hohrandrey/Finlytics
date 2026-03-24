package ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import viewmodel.FinanceViewModel

/**
 * Главный компонент приложения, устанавливающий тему и стили.
 * Использует MaterialTheme для единообразного оформления всех компонентов.
 *
 * @param viewModel ViewModel для управления состоянием приложения
 */
@Composable
fun App(viewModel: FinanceViewModel) {
    MaterialTheme {
        AppNavigation(viewModel)
    }
}