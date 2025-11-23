package ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.FinanceViewModel

@Composable
fun FilterSelector(viewModel: FinanceViewModel) {
    val periods = listOf("День", "Неделя", "Месяц", "Год", "Все время")

    Row {
        periods.forEach { period ->
            Button(
                onClick = { viewModel.applyFilter(period) },
                modifier = Modifier.width(120.dp)
            ) {
                Text(period)
            }
            Spacer(Modifier.width(8.dp))
        }
    }
}