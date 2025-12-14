package ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppColors
import kotlin.math.*

/**
 * Компонент для отображения круговой диаграммы распределения расходов по категориям.
 * Обновленная версия, которая отображает настоящую круговую диаграмму (pie chart).
 *
 * @param data Карта данных, где ключ - название категории, значение - сумма расходов
 * @param modifier Модификатор для настройки внешнего вида компонента
 * @param colors Список цветов для сегментов диаграммы (по умолчанию использует палитру из дизайна)
 */
@Composable
fun PieChart(
    data: Map<String, Double>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        AppColors.IncomeColor1,
        AppColors.IncomeColor2,
        AppColors.IncomeColor3,
        AppColors.IncomeColor4,
        AppColors.IncomeColor5,
        AppColors.IncomeColor6,
        AppColors.IncomeColor7,
        AppColors.IncomeColor8,
        AppColors.ExpensesColor1,
        AppColors.ExpensesColor2,
        AppColors.ExpensesColor3,
        AppColors.ExpensesColor4,
        AppColors.ExpensesColor5,
        AppColors.ExpensesColor6,
        AppColors.ExpensesColor7,
        AppColors.ExpensesColor8
    )
) {
    println("\n=== PIE CHART ===")
    println("Полученные данные: $data")
    println("Количество категорий: ${data.size}")
    println("Данные не пустые: ${data.isNotEmpty()}")

    // Фильтруем данные, оставляя только категории с положительными значениями
    val filteredData = data.filterValues { it > 0 }
        .toList()
        .sortedByDescending { it.second }
    val total = filteredData.sumOf { it.second }

    println("Отфильтрованные данные: $filteredData")
    println("Общая сумма расходов: $total")

    if (filteredData.isNotEmpty()) {
        println("Детализация по категориям:")
        filteredData.forEach { (category, amount) ->
            val percentage = (amount / total * 100)
            println("  - $category: $amount руб. (${"%.1f".format(percentage)}%)")
        }
    }
    println("==================\n")

    // Если данных нет, показываем сообщение
    if (total == 0.0 || filteredData.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Иконка пустой диаграммы (круг)
                Canvas(
                    modifier = Modifier.size(100.dp)
                ) {
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        style = Stroke(width = 4f)
                    )
                }
                Text(
                    "Нет данных по расходам",
                    color = Color.LightGray.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
        }
        return
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Основная круговая диаграмма
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            val canvasSize = min(size.width, size.height)
            val radius = canvasSize / 2 - 20
            val centerOffset = Offset(size.width / 2, size.height / 2)

            // Вычисляем углы для каждого сегмента
            var startAngle = -90f // Начинаем сверху (12 часов)

            filteredData.forEachIndexed { index, (_, amount) ->
                val sweepAngle = (amount / total * 360).toFloat()

                // Рисуем сегмент
                drawArc(
                    color = colors.getOrElse(index) { colors.get(8 - index) },
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(
                        centerOffset.x - radius,
                        centerOffset.y - radius
                    ),
                    size = Size(radius * 2, radius * 2)
                )

                startAngle += sweepAngle
            }

            val centerCircleRadius = radius * 0.9f

            drawCircle(
                color = AppColors.DarkColor,
                center = centerOffset,
                radius = centerCircleRadius
            )
        }
    }
}