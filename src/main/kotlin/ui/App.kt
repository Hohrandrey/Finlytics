package ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import viewmodel.FinanceViewModel

@Composable
fun App(viewModel: FinanceViewModel) {
    MaterialTheme(
        colors = darkColors(
            primary = Color(0xFF2176FF),
            primaryVariant = Color(0xFF1E88E5),
            secondary = Color(0xFF4ADE80),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White
        )
    ) {
        AppNavigation(viewModel)
    }
}