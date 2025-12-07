package main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import repository.FinanceRepository
import ui.App
import ui.components.CategoryDialog
import ui.components.OperationDialog
import viewmodel.FinanceViewModel

@Composable
@Preview
fun AppPreview() {
    val repository = remember { FinanceRepository() }
    val viewModel = remember { FinanceViewModel(repository) }

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            App(viewModel)
            OperationDialog(viewModel)
            CategoryDialog(viewModel)
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Finlytics - Менеджер личных финансов",
        state = WindowState(width = 1200.dp, height = 800.dp),
        undecorated = false,
        resizable = true
    ) {
        window.minimumSize = java.awt.Dimension(800, 600)
        AppPreview()
    }
}
