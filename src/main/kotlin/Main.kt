import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import repository.FinanceRepository
import ui.App
import ui.components.CategoryDialog
import ui.components.OperationDialog
import viewmodel.FinanceViewModel

@Composable
@Preview
fun App() {
    val repository = remember { FinanceRepository() }
    val viewModel = remember { FinanceViewModel(repository) }

    MaterialTheme {
        App(viewModel)
        OperationDialog(viewModel)
        CategoryDialog(viewModel)
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Finlytics") {
        App()
    }
}