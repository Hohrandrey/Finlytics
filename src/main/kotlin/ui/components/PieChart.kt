package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Компонент для отображения круговой диаграммы распределения расходов по категориям.
 * Если данных нет, отображает сообщение об отсутствии данных.
 *
 * @param data Карта данных, где ключ - название категории, значение - сумма расходов
 * @param modifier Модификатор для настройки внешнего вида компонента
 */
@Composable
fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()

    // Если данных нет, показываем сообщение
    if (total == 0.0) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Нет данных",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
        }
        return
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Статистика расходов:",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Палитра цветов для различных категорий
        val colors = listOf(
            Color(0xFFEF5350), Color(0xFFEC407A), Color(0xFFAB47BC),
            Color(0xFF7E57C2), Color(0xFF5C6BC0), Color(0xFF42A5F5),
            Color(0xFF29B6F6), Color(0xFF26C6DA), Color(0xFF26A69A)
        )

        // Отображаем легенду диаграммы (цветной квадрат + название + процент)
        data.entries.forEachIndexed { index, (category, amount) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(colors[index % colors.size])
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = category,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${String.format("%.1f", (amount / total * 100))}%",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
