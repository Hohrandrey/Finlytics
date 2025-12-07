package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.FinanceViewModel

@Composable
fun FilterSelector(viewModel: FinanceViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val periods = listOf("День", "Неделя", "Месяц", "Год", "Все время")

        periods.forEach { period ->
            Button(
                onClick = { viewModel.applyFilter(period) },
                modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
            ) {
                Text(period)
            }
        }
    }
}
