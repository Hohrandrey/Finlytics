package main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.application
import repository.FinanceRepository
import ui.App
import ui.components.AddCategoryDialog
import ui.components.OperationDialog
import utils.LoggingConfig
import viewmodel.FinanceViewModel

@Composable
@Preview
fun AppPreview() {
    val repository = remember { FinanceRepository() }
    val viewModel = remember { FinanceViewModel(repository) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            App(viewModel)

            if (viewModel.showOperationDialog) {
                OperationDialog(viewModel)
            }

            if (viewModel.showAddCategoryDialog) {
                AddCategoryDialog(viewModel)
            }
        }
    }
}

/**
 * Точка входа в приложение.
 * Инициализирует окно приложения и настраивает окружение.
 */
fun main() = application {
    // Логирование для корректной работы с UTF-8
    LoggingConfig.setupLogging()

    val windowState = rememberWindowState(width = 1440.dp, height = 1024.dp)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Finlytics - Менеджер личных финансов",
        state = windowState,
        undecorated = false,
        resizable = true
    ) {
        // Минимальный размер окна для удобства использования
        window.minimumSize = java.awt.Dimension(800, 600)
        AppPreview()
    }
}
