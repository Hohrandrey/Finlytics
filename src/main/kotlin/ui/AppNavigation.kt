package ui

import androidx.compose.runtime.Composable
import ui.screens.HistoryScreen
import ui.screens.OverviewScreen
import ui.screens.SettingsScreen
import viewmodel.FinanceViewModel

/**
 * Компонент навигации между экранами приложения.
 * В зависимости от текущего экрана в ViewModel отображает соответствующий экран.
 *
 * @param viewModel ViewModel с информацией о текущем экране и данными
 */
@Composable
fun AppNavigation(viewModel: FinanceViewModel) {
    when (viewModel.currentScreen) {
        "Overview" -> OverviewScreen(viewModel)
        "History" -> HistoryScreen(viewModel)
        "Settings" -> SettingsScreen(viewModel)
    }
}
