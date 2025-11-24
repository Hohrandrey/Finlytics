package ui

import androidx.compose.runtime.Composable
import ui.screens.HistoryScreen
import ui.screens.OverviewScreen
import ui.screens.SettingsScreen
import viewmodel.FinanceViewModel

@Composable
fun AppNavigation(viewModel: FinanceViewModel) {
    when (viewModel.currentScreen) {
        "Overview" -> OverviewScreen(viewModel)
        "History" -> HistoryScreen(viewModel)
        "Settings" -> SettingsScreen(viewModel)
    }
}